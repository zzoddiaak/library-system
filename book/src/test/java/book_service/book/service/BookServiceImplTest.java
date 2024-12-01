package book_service.book.service;

import book_service.book.config.security.RestTemplateAuthInterceptor;
import book_service.book.dto.DtoMapper;
import book_service.book.dto.books.BookCreateRequestDTO;
import book_service.book.dto.books.BookFullResponseDTO;
import book_service.book.entity.Book;
import book_service.book.exeption.book.BookNotFoundException;
import book_service.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private DtoMapper dtoMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateAuthInterceptor authInterceptor;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookCreateRequestDTO bookCreateRequestDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookCreateRequestDTO = new BookCreateRequestDTO();
        bookCreateRequestDTO.setIsbn(123456789L);
        bookCreateRequestDTO.setTitle("Test Book");
        bookCreateRequestDTO.setAuthor("Test Author");
    }

    @Test
    public void testCreateBook() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.existsByIsbn(123456789L)).thenReturn(false);
        when(dtoMapper.convertToEntity(bookCreateRequestDTO, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        BookFullResponseDTO responseDTO = new BookFullResponseDTO();
        when(dtoMapper.convertToDto(book, BookFullResponseDTO.class)).thenReturn(responseDTO);

        BookFullResponseDTO createdBook = bookService.createBook(bookCreateRequestDTO);

        assertNotNull(createdBook);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testUpdateBook() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setIsbn(123456789L);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        when(bookRepository.existsByIsbn(987654321L)).thenReturn(false);

        BookCreateRequestDTO updateRequest = new BookCreateRequestDTO();
        updateRequest.setIsbn(987654321L);
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Updated Author");

        BookFullResponseDTO updatedBookDTO = new BookFullResponseDTO();
        updatedBookDTO.setId(1L);
        updatedBookDTO.setTitle("Updated Title");
        updatedBookDTO.setAuthor("Updated Author");

        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(dtoMapper.convertToDto(existingBook, BookFullResponseDTO.class)).thenReturn(updatedBookDTO);

        BookFullResponseDTO updatedBook = bookService.updateBook(1L, updateRequest);

        assertNotNull(updatedBook);
        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals("Updated Author", updatedBook.getAuthor());

        verify(bookRepository, times(1)).save(existingBook);
    }


    @Test
    public void testDeleteBook() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteBookNotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
    }

}
