package library_service.library.service;

import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryFullDTO;
import library_service.library.entity.Library;
import library_service.library.repository.api.LibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetFreeBooks() {
        Library library1 = new Library();
        library1.setBookId(1L);
        library1.setBorrowTime(null);
        library1.setReturnTime(null);

        Library library2 = new Library();
        library2.setBookId(2L);
        library2.setBorrowTime(null);
        library2.setReturnTime(null);

        List<Library> libraries = Arrays.asList(library1, library2);

        when(libraryRepository.findFreeBooks()).thenReturn(libraries);

        List<LibraryFullDTO> freeBooks = libraryService.getFreeBooks();

        assertNotNull(freeBooks);
        assertEquals(2, freeBooks.size());
        assertEquals(1L, freeBooks.get(0).getBookId());
        assertEquals(2L, freeBooks.get(1).getBookId());

        verify(libraryRepository, times(1)).findFreeBooks();
    }

    @Test
    void testAddBook() {
        LibraryRequest request = new LibraryRequest();
        request.setBookId(1L);

        Library library = new Library();
        library.setBookId(1L);

        doNothing().when(libraryRepository).save(any(Library.class));

        libraryService.addBook(request);

        verify(libraryRepository, times(1)).save(any(Library.class));
    }

    @Test
    void testUpdateBookStatus() {
        Long bookId = 1L;
        LibraryUpdateRequest request = new LibraryUpdateRequest();
        request.setBorrowTime(LocalDateTime.now().minusDays(1));
        request.setReturnTime(LocalDateTime.now().plusDays(7));

        Library library = new Library();
        library.setBookId(1L);
        library.setBorrowTime(null);
        library.setReturnTime(null);

        when(libraryRepository.findById(bookId)).thenReturn(library);

        libraryService.updateBookStatus(bookId, request);

        verify(libraryRepository, times(1)).save(library);

        assertEquals(request.getBorrowTime(), library.getBorrowTime());
        assertEquals(request.getReturnTime(), library.getReturnTime());
    }
}
