package book_service.book.service.api;

import book_service.book.dto.books.BookCreateRequestDTO;
import book_service.book.dto.books.BookFullResponseDTO;
import java.util.List;

public interface BookService {

    List<BookFullResponseDTO> getAllBooks();

    BookFullResponseDTO getBookById(Long id);

    BookFullResponseDTO getBookByIsbn(Long isbn);

    BookFullResponseDTO createBook(BookCreateRequestDTO bookDto);

    BookFullResponseDTO updateBook(Long id, BookCreateRequestDTO bookDto);

    void deleteBook(Long id);
}
