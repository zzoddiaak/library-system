package book_service.book.service;

import book_service.book.config.security.RestTemplateAuthInterceptor;
import book_service.book.dto.LibraryRequest;
import book_service.book.dto.books.BookCreateRequestDTO;
import book_service.book.dto.books.BookFullResponseDTO;
import book_service.book.entity.Book;
import book_service.book.exeption.book.BookNotFoundException;
import book_service.book.repository.BookRepository;
import book_service.book.dto.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private DtoMapper dtoMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private RestTemplateAuthInterceptor authInterceptor;

    private Book testBook;

    private BookCreateRequestDTO bookCreateRequestDTO;

    @BeforeEach
    void setUp() {
        testBook = new Book(1L, 1234567890L, "Test Book", "Fiction", "Test Description", "Test Author");
        bookCreateRequestDTO = new BookCreateRequestDTO(1234567890L, "Test Book", "Fiction", "Test Description", "Test Author");

        lenient().when(authInterceptor.createAuthEntity(any())).thenReturn(new HttpEntity<>(new LibraryRequest(testBook.getId())));
    }


    @Test
    void testGetBookByIsbn_Success() {
        when(bookRepository.findByIsbn(1234567890L)).thenReturn(Optional.of(testBook));
        when(dtoMapper.convertToDto(testBook, BookFullResponseDTO.class)).thenReturn(new BookFullResponseDTO(1L, 1234567890L, "Test Book", "Fiction", "Test Description", "Test Author"));

        BookFullResponseDTO response = bookService.getBookByIsbn(1234567890L);


        assertNotNull(response);
        assertEquals("Test Book", response.getTitle());
        verify(bookRepository, times(1)).findByIsbn(1234567890L);
    }


    @Test
    void testCreateBook_Success() {
        when(bookRepository.existsByIsbn(1234567890L)).thenReturn(false);
        when(dtoMapper.convertToEntity(bookCreateRequestDTO, Book.class)).thenReturn(testBook);
        when(dtoMapper.convertToDto(testBook, BookFullResponseDTO.class)).thenReturn(new BookFullResponseDTO(1L, 1234567890L, "Test Book", "Fiction", "Test Description", "Test Author"));

        BookFullResponseDTO response = bookService.createBook(bookCreateRequestDTO);

        assertNotNull(response);
        assertEquals("Test Book", response.getTitle());
        verify(bookRepository, times(1)).save(testBook);
        verify(restTemplate, times(1)).postForEntity(eq("http://localhost:8081/api/library"), any(HttpEntity.class), eq(Void.class));
    }



    @Test
    void testUpdateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(dtoMapper.convertToDto(testBook, BookFullResponseDTO.class)).thenReturn(new BookFullResponseDTO(1L, 1234567890L, "Updated Book", "Fiction", "Updated Description", "Test Author"));

        BookFullResponseDTO response = bookService.updateBook(1L, bookCreateRequestDTO);


        assertNotNull(response);
        assertEquals("Updated Book", response.getTitle());
        verify(bookRepository, times(1)).save(testBook);
    }


    @Test
    void testDeleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
        verify(restTemplate, times(1)).exchange(eq("http://localhost:8081/api/library/book/1"), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Void.class));
    }


}
