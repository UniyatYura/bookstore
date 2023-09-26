package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.BookSearchParameters;
import mate.academy.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    List<BookDto> getAllByName(String name);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters);

    void update(Long id, CreateBookRequestDto createBookRequestDto);
}