package library_service.library.controller;


import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryFullDTO;
import library_service.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping("/free-books")
    public ResponseEntity<List<LibraryFullDTO>> getFreeBooks() {
        List<LibraryFullDTO> freeBooks = libraryService.getFreeBooks();
        return ResponseEntity.ok(freeBooks);
    }

    @PostMapping
    public ResponseEntity<Void> addBookToLibrary(@RequestBody LibraryRequest request) {
        libraryService.addBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBookStatus(@PathVariable Long id,
                                                 @RequestBody LibraryUpdateRequest request) {
        libraryService.updateBookStatus(id, request);
        return ResponseEntity.noContent().build();
    }
}
