package mate.academy.bookstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.CreateBookRequestDto;
import mate.academy.bookstore.model.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "database/books/add-two-default-books.sql"
            ));
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "database/books/remove-all-books.sql"
            ));
        }
    }

    @Test
    @DisplayName("Create a new book")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createBook_ValidRequestDto_Success() throws Exception {
        // given
        Book bookA = getBookA();
        CreateBookRequestDto createBookRequestDto = bookRequestDto(bookA);
        BookDto expected = bookResponseDto(bookA);
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);
        // when
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        // then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser
    @DisplayName("Get all available books on page")
    void findAllBooks_ValidData_Success() throws Exception {
        // given
        Book bookA = getBookA();
        Book bookB = getBookB();
        List<BookDto> expected = Arrays.asList(
                bookResponseDto(bookA),
                bookResponseDto(bookB)
        );
        // when
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList(), "id");
    }

    @Test
    @WithMockUser
    @DisplayName("Get book by id 1")
    void findById_ValidBookId_Success() throws Exception {
        // given
        Book bookA = getBookA();
        BookDto expected = bookResponseDto(bookA);
        // when
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/{bookId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        BookDto actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @DisplayName("Delete book by ID")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBook_BookDeletedById_Success() throws Exception {
        mockMvc.perform(delete("/books/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("Search book by author, expected 1 book")
    void searchBookByAuthor_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/search")
                                .param("author", "Author A"))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), new TypeReference<>() {});
        assertNotNull(actual);
        assertEquals("Author A", actual.get(0).getAuthor());
    }

    @Test
    @WithMockUser
    @DisplayName("Search book by title, expected 1 book")
    void searchBookByTitle_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/search")
                                .param("title", "Book A"))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), new TypeReference<>() {});
        assertNotNull(actual);
        assertEquals("Book A", actual.get(0).getTitle());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update mist book with id 1")
    void updateBook_Ok() throws Exception {
        // given
        Long bookId = 1L;
        Book book = getBookA();
        CreateBookRequestDto createBookRequestDto = bookRequestDto(book);
        BookDto expected = bookResponseDto(book);
        String request = objectMapper.writeValueAsString(createBookRequestDto);
        // when
        MvcResult mvcResult = mockMvc.perform(
                        put("/books/{id}", bookId)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        BookDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    private Book getBookA() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book A");
        book.setAuthor("Author A");
        book.setIsbn("aaa111");
        book.setPrice(BigDecimal.valueOf(11.11));
        book.setDescription("Description Book A");
        book.setCoverImage("http://example.com/bookA.jpg");
        return book;
    }

    private Book getBookB() {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Book B");
        book.setAuthor("Author B");
        book.setIsbn("bbb222");
        book.setPrice(BigDecimal.valueOf(22.22));
        book.setDescription("Description Book B");
        book.setCoverImage("http://example.com/bookB.jpg");
        return book;
    }

    private BookDto bookResponseDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        return bookDto;
    }

    private CreateBookRequestDto bookRequestDto(Book book) {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(book.getTitle());
        requestDto.setAuthor(book.getAuthor());
        requestDto.setPrice(book.getPrice());
        requestDto.setDescription(book.getDescription());
        requestDto.setCoverImage(book.getCoverImage());
        requestDto.setCategoryIds(Set.of());
        return requestDto;
    }
}
