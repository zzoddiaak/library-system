package library_service.library.service;

import library_service.library.config.security.RestTemplateAuthInterceptor;
import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.entity.Library;
import library_service.library.exeption.book.MissingReturnTimeException;
import library_service.library.exeption.book.TimeOrderViolationException;
import library_service.library.exeption.library.LibraryNotFoundException;
import library_service.library.repository.LibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LibraryServiceTest {

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateAuthInterceptor authInterceptor;

    @InjectMocks
    private LibraryService libraryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testUpdateBookStatus_BorrowTimeAfterReturnTime() {
        LibraryUpdateRequest request = new LibraryUpdateRequest();
        request.setBorrowTime(LocalDateTime.now().plusDays(1));
        request.setReturnTime(LocalDateTime.now());

        Library library = new Library();
        library.setId(1L);
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(library));

        assertThrows(TimeOrderViolationException.class, () -> libraryService.updateBookStatus(1L, request));
    }

    @Test
    public void testUpdateBookStatus_MissingReturnTime() {
        LibraryUpdateRequest request = new LibraryUpdateRequest();
        request.setBorrowTime(LocalDateTime.now());

        Library library = new Library();
        library.setId(1L);
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(library));

        assertThrows(MissingReturnTimeException.class, () -> libraryService.updateBookStatus(1L, request));
    }

    @Test
    public void testDeleteBook() {
        Library library = new Library();
        library.setBookId(1L);
        when(libraryRepository.findByBookId(1L)).thenReturn(Optional.of(library));

        libraryService.deleteByBookId(1L);

        verify(libraryRepository, times(1)).delete(library);
    }

    @Test
    public void testDeleteBook_NotFound() {
        when(libraryRepository.findByBookId(1L)).thenReturn(Optional.empty());

        assertThrows(LibraryNotFoundException.class, () -> libraryService.deleteByBookId(1L));
    }

}
