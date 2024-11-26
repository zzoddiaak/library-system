package book_service.book.controller;

import book_service.book.dto.books.BookFullDTO;
import book_service.book.service.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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
    public List<BookFullDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/auth/{id}")
    public BookFullDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/isbn/{isbn}")
    public BookFullDTO getBookByIsbn(@PathVariable Long isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @PostMapping
    public BookFullDTO createBook(@RequestBody BookFullDTO bookDto) {
        return bookService.createBook(bookDto);
    }

    @PutMapping("/{id}")
    public BookFullDTO updateBook(@PathVariable Long id, @RequestBody BookFullDTO bookDto) {
        return bookService.updateBook(id, bookDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
