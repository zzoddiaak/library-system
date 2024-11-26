package book_service.book.repository.impl;

import book_service.book.entity.Books;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Books> typedQuery;

    @InjectMocks
    private BookRepositoryImpl bookRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testFindByIsbn_WhenBookFound() {
        Books book = new Books();
        book.setIsbn(123456789L);
        book.setTitle("Test Book");

        when(entityManager.createQuery(anyString(), eq(Books.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("isbn"), eq(123456789L))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(book));  // Возвращаем список с одним элементом

        Books result = bookRepository.findByIsbn(123456789L);

        assertNotNull(result);
        assertEquals(123456789L, result.getIsbn());
        assertEquals("Test Book", result.getTitle());

        verify(entityManager).createQuery(anyString(), eq(Books.class));
        verify(typedQuery).setParameter(eq("isbn"), eq(123456789L));
        verify(typedQuery).getResultList();
    }

    @Test
    void testFindByIsbn_WhenBookNotFound() {
        when(entityManager.createQuery(anyString(), eq(Books.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("isbn"), eq(123456789L))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList());

        Books result = bookRepository.findByIsbn(123456789L);

        assertNull(result);

        verify(entityManager).createQuery(anyString(), eq(Books.class));
        verify(typedQuery).setParameter(eq("isbn"), eq(123456789L));
        verify(typedQuery).getResultList();
    }
}
