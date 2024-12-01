package library_service.library.controller;

import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryFullDTO;
import library_service.library.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LibraryControllerTest {

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController libraryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();
    }

    @Test
    void testGetFreeBooks() throws Exception {
        LibraryFullDTO libraryFullDTO = new LibraryFullDTO();
        libraryFullDTO.setBookId(123L);
        libraryFullDTO.setTitle("Test Book");

        when(libraryService.getFreeBooks()).thenReturn(Collections.singletonList(libraryFullDTO));

        mockMvc.perform(get("/api/library/free-books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].bookId").value(123L))
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }


    @Test
    void testAddBookToLibrary() throws Exception {
        doNothing().when(libraryService).addBook(any(LibraryRequest.class));

        mockMvc.perform(post("/api/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":123}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateBookStatus() throws Exception {
        doNothing().when(libraryService).updateBookStatus(eq(1L), any(LibraryUpdateRequest.class));

        mockMvc.perform(put("/api/library/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"borrowTime\":\"2023-01-01T10:00:00\",\"returnTime\":\"2023-01-10T10:00:00\"}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBookByBookId() throws Exception {
        doNothing().when(libraryService).deleteByBookId(123L);

        mockMvc.perform(delete("/api/library/book/123"))
                .andExpect(status().isNoContent());
    }
}
