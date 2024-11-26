package library_service.library.repository.impl;

import library_service.library.entity.Library;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LibraryRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Library> typedQuery;

    @InjectMocks
    private LibraryRepositoryImpl libraryRepositoryImpl;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testFindFreeBooks() {
        Library library1 = new Library();
        library1.setBookId(1L);
        library1.setReturnTime(null);

        Library library2 = new Library();
        library2.setBookId(2L);
        library2.setReturnTime(null);

        List<Library> libraries = Arrays.asList(library1, library2);

        when(entityManager.createQuery(anyString(), eq(Library.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(libraries);

        List<Library> result = libraryRepositoryImpl.findFreeBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getBookId());
        assertEquals(2L, result.get(1).getBookId());

        verify(entityManager, times(1)).createQuery(anyString(), eq(Library.class));
        verify(typedQuery, times(1)).getResultList();
    }
}
