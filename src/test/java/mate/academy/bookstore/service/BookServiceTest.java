package mate.academy.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.CreateBookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @DisplayName("Find existing book by valid id")
    @Test
    void findById_ValidBookId_ReturnBookDto() {
        //given
        Long bookId = 1L;
        Book book = getBookA();
        BookDto expected = bookToResponseDto(book);
        //when
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.findById(bookId);
        //then
        assertEquals(expected, actual);
    }

    @DisplayName("Find book by negative id")
    @Test
    void findById_InvalidNegativeBookId_ThrowEntityNotFoundException() {
        //given
        Long invalidId = -10L;
        when(bookRepository.findById(invalidId))
                .thenReturn(Optional.empty());
        //when
        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> bookService.findById(invalidId));
        //then
        assertEquals("Can't find book by id = "
                + invalidId, exception.getMessage());
    }

    @DisplayName("Find a book by a non-existent book ID")
    @Test
    void findById_InvalidNonExistentBookId_ThrowEntityNotFoundException() {
        //given
        Long invalidId = 100L;
        when(bookRepository.findById(invalidId))
                .thenReturn(Optional.empty());
        //when
        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> bookService.findById(invalidId));
        //then
        String expected = "Can't find book by id = " + invalidId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify save() method works")
    void save_ValidCreateBookRequestDto_ReturnBookDto() {
        // given
        Book book = getBookA();
        CreateBookRequestDto requestDto = bookToRequestDto(book);
        BookDto expected = bookToResponseDto(book);
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        // when
        BookDto actual = bookService.save(requestDto);
        // then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    void findAll_ValidPageable_ReturnAllBooks() {
        // given
        Book book = getBookA();
        BookDto bookDto = bookToResponseDto(book);
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(
                books, pageable, books.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        // when
        List<BookDto> booksDto = bookService.findAll(pageable);
        // then
        Assertions.assertEquals(1, booksDto.size());
        Assertions.assertNotNull(booksDto.get(0));
        Assertions.assertEquals(1L, booksDto.get(0).getId());
    }

    @Test
    @DisplayName("Verify getAllByName() method works")
    void getAllByName_ExistingTitle_ReturnsBook() {
        // given
        Book book = getBookA();
        List<Book> books = new ArrayList<>();
        books.add(book);
        BookDto bookDto = bookToResponseDto(book);
        when(bookRepository
                .findAllByNameContainsIgnoreCase(bookDto.getTitle())).thenReturn(books);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        // when
        List<BookDto> booksDto = bookService.getAllByName(bookDto.getTitle());
        // then
        assertEquals(1, booksDto.size());
        assertEquals(bookDto, booksDto.get(0));
    }

    @Test
    @DisplayName("Verify deleteById() method is working with valid book ID")
    void deleteById_ValidId_CategoryHasBeenDeleted() {
        Book book = getBookA();
        assertDoesNotThrow(() -> bookService.deleteById(book.getId()));
    }

    @Test
    @DisplayName("Update a book by ID")
    void update_VerifyExistingBook_ReturnUpdatedBookDto() {
        //given
        Book updatedBook = getBookA();
        BookDto expected = bookToResponseDto(updatedBook);
        Long existingBookId = anyLong();
        CreateBookRequestDto createBookRequestDto = bookToRequestDto(updatedBook);
        when(bookRepository.findById(existingBookId)).thenReturn(Optional.of(updatedBook));
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expected);
        //when
        BookDto actual = bookService.update(existingBookId, createBookRequestDto);
        //then
        assertEquals(expected, actual);
    }

    @DisplayName("Update a book by non existing ID")
    @Test
    void update_WithNonExistingId_ShouldThrowException() {
        //given
        Book updatedBook = getBookA();
        CreateBookRequestDto createBookRequestDto = bookToRequestDto(updatedBook);
        Long testId = 100L;
        Mockito.when(bookRepository.findById(testId)).thenReturn(Optional.empty());
        //when
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(testId, createBookRequestDto)
        );
        String actual = exception.getMessage();
        String expected = "Can't find book by id " + testId;
        //then
        Assertions.assertEquals(expected, actual);
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
        book.setDeleted(false);
        return book;
    }

    private BookDto bookToResponseDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        return bookDto;
    }

    private CreateBookRequestDto bookToRequestDto(Book book) {
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
