package library_service.library.service;

import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryBookDTO;
import library_service.library.dto.library.LibraryFullDTO;
import library_service.library.entity.Library;
import library_service.library.repository.api.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final RestTemplate restTemplate;

    public List<LibraryFullDTO> getFreeBooks() {
        List<Library> freeBooks = libraryRepository.findFreeBooks();
        return freeBooks.stream()
                .map(library -> {
                    Long bookId = library.getBookId();
                    LibraryBookDTO bookDetails = restTemplate.getForObject("http://localhost:8080/api/books/auth/" + bookId, LibraryBookDTO.class);
                    return new LibraryFullDTO(library, bookDetails);
                })
                .collect(Collectors.toList());
    }

    public void addBook(LibraryRequest request) {
        Library library = new Library();
        library.setBookId(request.getBookId());
        libraryRepository.save(library);
    }

    public void updateBookStatus(Long id, LibraryUpdateRequest request) {
        Library library = libraryRepository.findById(id);
        library.setBorrowTime(request.getBorrowTime());
        library.setReturnTime(request.getReturnTime());
        libraryRepository.save(library);
    }
}
