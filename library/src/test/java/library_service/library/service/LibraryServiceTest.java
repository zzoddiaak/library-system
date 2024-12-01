package library_service.library.service;

import library_service.library.config.security.RestTemplateAuthInterceptor;
import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryBookDTO;
import library_service.library.dto.library.LibraryFullDTO;
import library_service.library.entity.Library;
import library_service.library.exeption.book.MissingBorrowTimeException;
import library_service.library.exeption.book.MissingReturnTimeException;
import library_service.library.exeption.book.TimeOrderViolationException;
import library_service.library.exeption.library.LibraryNotFoundException;
import library_service.library.repository.LibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LibraryServiceTest {

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateAuthInterceptor authInterceptor;

    @InjectMocks
    private LibraryService libraryService;

    private Library testLibrary;
    private LibraryRequest libraryRequest;
    private LibraryUpdateRequest libraryUpdateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testLibrary = Library.builder()
                .id(1L)
                .bookId(123L)
                .borrowTime(null)
                .returnTime(null)
                .build();

        libraryRequest = new LibraryRequest();
        libraryRequest.setBookId(123L);

        libraryUpdateRequest = new LibraryUpdateRequest();
    }

    @Test
    void testGetFreeBooks() {
        when(libraryRepository.findFreeBooks()).thenReturn(Collections.singletonList(testLibrary));
        when(authInterceptor.createAuthEntity(any())).thenReturn(new HttpEntity<>(null));

        LibraryBookDTO bookDTO = new LibraryBookDTO();
        bookDTO.setId(123L);
        bookDTO.setTitle("Test Book");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(LibraryBookDTO.class)))
                .thenReturn(ResponseEntity.ok(bookDTO));

        List<LibraryFullDTO> result = libraryService.getFreeBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(123L, result.get(0).getBookId());
        verify(libraryRepository, times(1)).findFreeBooks();
    }

    @Test
    void testAddBook() {
        libraryService.addBook(libraryRequest);
        verify(libraryRepository, times(1)).save(any(Library.class));
    }

    @Test
    void testUpdateBookStatus_Success() {
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(testLibrary));

        libraryUpdateRequest.setBorrowTime(LocalDateTime.now());
        libraryUpdateRequest.setReturnTime(LocalDateTime.now().plusDays(1));

        libraryService.updateBookStatus(1L, libraryUpdateRequest);

        verify(libraryRepository, times(1)).save(any(Library.class));
    }

    @Test
    void testUpdateBookStatus_BorrowTimeAfterReturnTime() {
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(testLibrary));

        libraryUpdateRequest.setBorrowTime(LocalDateTime.now());
        libraryUpdateRequest.setReturnTime(LocalDateTime.now().minusDays(1));

        assertThrows(TimeOrderViolationException.class, () ->
                libraryService.updateBookStatus(1L, libraryUpdateRequest)
        );
    }

    @Test
    void testUpdateBookStatus_MissingReturnTime() {
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(testLibrary));

        libraryUpdateRequest.setBorrowTime(LocalDateTime.now());
        libraryUpdateRequest.setReturnTime(null);

        assertThrows(MissingReturnTimeException.class, () ->
                libraryService.updateBookStatus(1L, libraryUpdateRequest)
        );
    }

    @Test
    void testUpdateBookStatus_MissingBorrowTime() {
        when(libraryRepository.findById(1L)).thenReturn(Optional.of(testLibrary));

        libraryUpdateRequest.setBorrowTime(null);
        libraryUpdateRequest.setReturnTime(LocalDateTime.now());

        assertThrows(MissingBorrowTimeException.class, () ->
                libraryService.updateBookStatus(1L, libraryUpdateRequest)
        );
    }

    @Test
    void testDeleteByBookId_Success() {
        when(libraryRepository.findByBookId(123L)).thenReturn(Optional.of(testLibrary));

        libraryService.deleteByBookId(123L);

        verify(libraryRepository, times(1)).delete(testLibrary);
    }

    @Test
    void testDeleteByBookId_LibraryNotFound() {
        when(libraryRepository.findByBookId(123L)).thenReturn(Optional.empty());

        assertThrows(LibraryNotFoundException.class, () ->
                libraryService.deleteByBookId(123L)
        );
    }
}
