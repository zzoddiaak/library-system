package book_service.book.service;

import book_service.book.dto.books.BookFullDTO;
import book_service.book.entity.Books;
import book_service.book.exeption.book.BookNotFoundException;
import book_service.book.repository.api.BookRepository;
import book_service.book.service.BookServiceImpl;
import book_service.book.dto.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private DtoMapper dtoMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookServiceImpl bookService;

    private Books book;
    private BookFullDTO bookFullDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Books(1L, 123456789L, "Test Book", "Fiction", "Description", "Author");
        bookFullDTO = new BookFullDTO(1L, 123456789L, "Test Book", "Fiction", "Description", "Author");
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
        when(dtoMapper.convertToDto(book, BookFullDTO.class)).thenReturn(bookFullDTO);

        var result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(book);
        when(dtoMapper.convertToDto(book, BookFullDTO.class)).thenReturn(bookFullDTO);

        BookFullDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }

    @Test
    void testGetBookByIsbn() {
        when(bookRepository.findByIsbn(123456789L)).thenReturn(book);
        when(dtoMapper.convertToDto(book, BookFullDTO.class)).thenReturn(bookFullDTO);

        BookFullDTO result = bookService.getBookByIsbn(123456789L);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }




    @Test
    void testUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(book);
        doNothing().when(bookRepository).save(book);
        when(dtoMapper.convertToDto(book, BookFullDTO.class)).thenReturn(bookFullDTO);

        BookFullDTO updatedBook = bookService.updateBook(1L, bookFullDTO);

        assertNotNull(updatedBook);
        assertEquals("Test Book", updatedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);  // Проверяем, что метод deleteById был вызван один раз
    }

    @Test
    void testDeleteBook_BookNotFound() {
        doThrow(new BookNotFoundException("Книга не найдена")).when(bookRepository).deleteById(1L);

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
        assertEquals("Книга не найдена", exception.getMessage());
    }
}
