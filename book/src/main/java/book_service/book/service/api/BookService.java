package book_service.book.service.api;

import book_service.book.dto.books.BookFullDTO;
import java.util.List;

public interface BookService {

    List<BookFullDTO> getAllBooks();

    BookFullDTO getBookById(Long id);

    BookFullDTO getBookByIsbn(Long isbn);

    BookFullDTO createBook(BookFullDTO bookDto);

    BookFullDTO updateBook(Long id, BookFullDTO bookDto);

    void deleteBook(Long id);
}
