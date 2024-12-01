package book_service.book.controller;

import book_service.book.dto.books.BookCreateRequestDTO;
import book_service.book.dto.books.BookFullResponseDTO;
import book_service.book.service.BookServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookServiceImpl bookService;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllBooks() throws Exception {
        BookFullResponseDTO book1 = new BookFullResponseDTO(1L, 123456789L, "Title1", "Genre1", "Description1", "Author1");
        BookFullResponseDTO book2 = new BookFullResponseDTO(2L, 987654321L, "Title2", "Genre2", "Description2", "Author2");

        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testGetBookById() throws Exception {
        BookFullResponseDTO book = new BookFullResponseDTO(1L, 123456789L, "Title1", "Genre1", "Description1", "Author1");

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title1"));
    }

    @Test
    void testCreateBook() throws Exception {
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(123456789L, "Genre1", "Description1", "Author1", "Title1");
        BookFullResponseDTO createdBook = new BookFullResponseDTO(1L, 123456789L, "Title1", "Genre1", "Description1", "Author1");

        when(bookService.createBook(any(BookCreateRequestDTO.class))).thenReturn(createdBook);

        mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Title1"));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(123456789L, "Updated Genre", "Updated Description", "Updated Author", "Updated Title");
        BookFullResponseDTO updatedBook = new BookFullResponseDTO(1L, 123456789L, "Updated Title", "Updated Genre", "Updated Description", "Updated Author");

        when(bookService.updateBook(eq(1L), any(BookCreateRequestDTO.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
