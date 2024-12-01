package library_service.library.service;

import library_service.library.config.RestTemplateAuthInterceptor;
import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryBookDTO;
import library_service.library.dto.library.LibraryFullDTO;
import library_service.library.entity.Library;
import library_service.library.exeption.book.MissingBorrowTimeException;
import library_service.library.exeption.book.MissingReturnTimeException;
import library_service.library.exeption.book.TimeOrderViolationException;
import library_service.library.exeption.library.LibraryNotFoundException;
import library_service.library.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    private final RestTemplateAuthInterceptor authInterceptor;

    public List<LibraryFullDTO> getFreeBooks() {
        return libraryRepository.findFreeBooks()
                .stream()
                .map(library -> {
                    Long bookId = library.getBookId();
                    HttpEntity<Void> entity = authInterceptor.createAuthEntity(null);
                    ResponseEntity<LibraryBookDTO> response = restTemplate.exchange(
                            "http://localhost:8080/api/books/" + bookId,
                            HttpMethod.GET,
                            entity,
                            LibraryBookDTO.class
                    );
                    return new LibraryFullDTO(library, response.getBody());
                })
                .collect(Collectors.toList());
    }

    public void addBook(LibraryRequest request) {
        Library library = new Library();
        library.setBookId(request.getBookId());
        libraryRepository.save(library);
    }

    public void updateBookStatus(Long id, LibraryUpdateRequest request) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException(id));

        if (request.getBorrowTime() != null && request.getReturnTime() != null) {
            // Проверка: borrowTime не может быть позже returnTime
            if (request.getBorrowTime().isAfter(request.getReturnTime())) {
                throw new TimeOrderViolationException();
            }
        }

        if (request.getBorrowTime() != null && request.getReturnTime() == null) {
            throw new MissingReturnTimeException();
        }

        if (request.getReturnTime() != null && request.getBorrowTime() == null) {
            throw new MissingBorrowTimeException();
        }


        library.setBorrowTime(request.getBorrowTime());
        library.setReturnTime(request.getReturnTime());
        libraryRepository.save(library);
    }
    public void deleteByBookId(Long bookId) {
        Library library = libraryRepository.findByBookId(bookId)
                .orElseThrow(() -> new LibraryNotFoundException(bookId));
        libraryRepository.delete(library);
    }


}

