package mate.academy.bookstore;

import java.math.BigDecimal;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book kobzar = new Book();
            kobzar.setTitle("Kobzar");
            kobzar.setAuthor("Shevcenko");
            kobzar.setDescription("The book was written in 1840");
            kobzar.setCoverImage("coverImageKobzar.jpeg");
            kobzar.setPrice(BigDecimal.valueOf(500));
            kobzar.setIsbn("978-966-429-192-4");
            bookService.save(kobzar);
            System.out.println(bookService.findAll());
        };
    }
}
