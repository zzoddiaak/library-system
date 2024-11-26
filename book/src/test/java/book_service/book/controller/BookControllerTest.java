package book_service.book.controller;

import book_service.book.dto.books.BookFullDTO;
import book_service.book.service.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookServiceImpl bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void testGetAllBooks() throws Exception {
        BookFullDTO book1 = new BookFullDTO(1L, 123456789L, "Book One", "Fiction", "Description of Book One", "Author One");
        BookFullDTO book2 = new BookFullDTO(2L, 987654321L, "Book Two", "Non-fiction", "Description of Book Two", "Author Two");
        List<BookFullDTO> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById() throws Exception {
        BookFullDTO book = new BookFullDTO(1L, 123456789L, "Book One", "Fiction", "Description of Book One", "Author One");

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/auth/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Book One"))
                .andExpect(jsonPath("$.genre").value("Fiction"))
                .andExpect(jsonPath("$.description").value("Description of Book One"))
                .andExpect(jsonPath("$.author").value("Author One"));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testCreateBook() throws Exception {
        BookFullDTO newBook = new BookFullDTO(null, 1122334455L, "New Book", "Fantasy", "Description of New Book", "New Author");
        BookFullDTO createdBook = new BookFullDTO(1L, 1122334455L, "New Book", "Fantasy", "Description of New Book", "New Author");

        when(bookService.createBook(any(BookFullDTO.class))).thenReturn(createdBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\": 1122334455, \"title\": \"New Book\", \"genre\": \"Fantasy\", \"description\": \"Description of New Book\", \"author\": \"New Author\"}"))
                .andExpect(status().isOk())  // Статус ответа 200 OK
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.genre").value("Fantasy"))
                .andExpect(jsonPath("$.description").value("Description of New Book"))
                .andExpect(jsonPath("$.author").value("New Author"));

        verify(bookService, times(1)).createBook(any(BookFullDTO.class));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookFullDTO updatedBook = new BookFullDTO(1L, 123456789L, "Updated Book", "Science Fiction", "Updated Description", "Updated Author");

        when(bookService.updateBook(eq(1L), any(BookFullDTO.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\": 123456789, \"title\": \"Updated Book\", \"genre\": \"Science Fiction\", \"description\": \"Updated Description\", \"author\": \"Updated Author\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.genre").value("Science Fiction"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.author").value("Updated Author"));

        verify(bookService, times(1)).updateBook(eq(1L), any(BookFullDTO.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }
}
