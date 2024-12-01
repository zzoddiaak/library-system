package book_service.book.controller;

import book_service.book.dto.books.BookCreateRequestDTO;
import book_service.book.dto.books.BookFullResponseDTO;
import book_service.book.service.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookService;

    @GetMapping
    public List<BookFullResponseDTO> getAllBooks() {
        return bookService.getAllBooks();
    }


    @GetMapping("/{id}")
    public BookFullResponseDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/isbn/{isbn}")
    public BookFullResponseDTO getBookByIsbn(@PathVariable Long isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @PostMapping
    public BookFullResponseDTO createBook(@RequestBody BookCreateRequestDTO bookDto) {
        return bookService.createBook(bookDto);
    }

    @PutMapping("/{id}")
    public BookFullResponseDTO updateBook(@PathVariable Long id, @RequestBody BookCreateRequestDTO bookDto) {
        return bookService.updateBook(id, bookDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

