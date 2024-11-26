package library_service.library.controller;

import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryFullDTO;
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
        LibraryFullDTO book1 = new LibraryFullDTO(1L, null, null);
        LibraryFullDTO book2 = new LibraryFullDTO(2L, null, null);
        List<LibraryFullDTO> books = Arrays.asList(book1, book2);

        when(libraryService.getFreeBooks()).thenReturn(books);

        mockMvc.perform(get("/api/library/free-books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].bookId").value(1))
                .andExpect(jsonPath("$[1].bookId").value(2));

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
