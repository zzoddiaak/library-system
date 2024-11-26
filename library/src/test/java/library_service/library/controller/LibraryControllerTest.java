package library_service.library.controller;

import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryBookDTO;
import library_service.library.dto.library.LibraryFullDTO;
import library_service.library.entity.Library;
import library_service.library.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LibraryControllerTest {

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController libraryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();
    }

    @Test
    void testGetFreeBooks() throws Exception {
        Library library1 = new Library(1L, 101L, LocalDateTime.now(), null);
        Library library2 = new Library(2L, 102L, LocalDateTime.now(), null);

        LibraryBookDTO bookDetails1 = new LibraryBookDTO(
                101L, 9781234567897L, "Book Title 1", "Genre 1", "Description 1", "Author 1");
        LibraryBookDTO bookDetails2 = new LibraryBookDTO(
                102L, 9781234567898L, "Book Title 2", "Genre 2", "Description 2", "Author 2");


        LibraryFullDTO dto1 = new LibraryFullDTO(library1, bookDetails1);
        LibraryFullDTO dto2 = new LibraryFullDTO(library2, bookDetails2);

        List<LibraryFullDTO> books = Arrays.asList(dto1, dto2);

        when(libraryService.getFreeBooks()).thenReturn(books);

        mockMvc.perform(get("/api/library/free-books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].bookId").value(101))
                .andExpect(jsonPath("$[0].title").value("Book Title 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].bookId").value(102))
                .andExpect(jsonPath("$[1].title").value("Book Title 2"));

        verify(libraryService, times(1)).getFreeBooks();
    }

    @Test
    void testAddBookToLibrary() throws Exception {
        LibraryRequest request = new LibraryRequest();
        request.setBookId(1L);

        mockMvc.perform(post("/api/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\": 1}"))
                .andExpect(status().isCreated());

        verify(libraryService, times(1)).addBook(request);
    }

    @Test
    void testUpdateBookStatus() throws Exception {
        Long bookId = 1L;

        LocalDateTime borrowTime = LocalDateTime.now().minusDays(1);
        LocalDateTime returnTime = LocalDateTime.now().plusDays(7);

        LibraryUpdateRequest request = new LibraryUpdateRequest();
        request.setBorrowTime(borrowTime);
        request.setReturnTime(returnTime);

        mockMvc.perform(put("/api/library/{id}", bookId)
                        .contentType("application/json")
                        .content("{\"borrowTime\": \"" + borrowTime + "\", \"returnTime\": \"" + returnTime + "\"}"))
                .andExpect(status().isNoContent());

        verify(libraryService, times(1)).updateBookStatus(eq(bookId), any(LibraryUpdateRequest.class));
    }
}
