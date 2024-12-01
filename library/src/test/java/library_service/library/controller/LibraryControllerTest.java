package library_service.library.controller;

import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryFullDTO;
import library_service.library.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class LibraryControllerTest {

    @Mock
    private LibraryService libraryService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        LibraryController libraryController = new LibraryController(libraryService);
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();
    }

    @Test
    public void testGetFreeBooks() throws Exception {
        LibraryFullDTO book1 = new LibraryFullDTO();
        LibraryFullDTO book2 = new LibraryFullDTO();
        when(libraryService.getFreeBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/library/free-books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }




    @Test
    public void testUpdateBookStatus() throws Exception {
        LibraryUpdateRequest request = new LibraryUpdateRequest();
        request.setBorrowTime(java.time.LocalDateTime.of(2024, 12, 1, 18, 0));
        request.setReturnTime(java.time.LocalDateTime.of(2024, 12, 2, 18, 0));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .put("/api/library/1")
                        .contentType("application/json")
                        .content("{\"borrowTime\":\"2024-12-01T18:00:00\", \"returnTime\":\"2024-12-02T18:00:00\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(libraryService, times(1)).updateBookStatus(1L, request);
    }


    @Test
    public void testDeleteBookByBookId() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete("/api/library/book/1")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(libraryService, times(1)).deleteByBookId(1L);
    }

}
