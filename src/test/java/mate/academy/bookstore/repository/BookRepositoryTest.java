package mate.academy.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setName("Drama");
        category.setDescription("Description category drama");
        categoryRepository.save(category);
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("1111");
        book1.setPrice(BigDecimal.valueOf(50));
        book1.setDescription("Description 1");
        book1.setCoverImage("Image 1");
        book1.getCategories().add(category);
        bookRepository.save(book1);
        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("2222");
        book2.setPrice(BigDecimal.valueOf(100));
        book2.setDescription("Description 2");
        book2.setCoverImage("Image 2");
        book2.getCategories().add(category);
        bookRepository.save(book2);
    }

    @Test
    @DisplayName("Find book by title ignoring case when book title is not present")
    void findAllByTitleContainsIgnoreCase_BookTitleNotPresent_ExpectedZeroBooks() {
        List<Book> actual = bookRepository.findAllByNameContainsIgnoreCase("Bible");
        assertEquals(0, actual.size());
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Find book by title ignoring case when book title is present")
    void findAllByTitleContainsIgnoreCase_BookTitleIsPresent_ExpectedOneBooks() {
        List<Book> actual = bookRepository.findAllByNameContainsIgnoreCase("book 1");
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("Find book by title ignoring case")
    void findAllByTitleContainsIgnoreCase_BookTitleIsPresent_ExpectedBookTitle() {
        List<Book> list = bookRepository.findAllByNameContainsIgnoreCase("book 2");
        String expected = "Book 2";
        String actual = list.get(0).getTitle();
        assertEquals(expected, actual);
    }
}
