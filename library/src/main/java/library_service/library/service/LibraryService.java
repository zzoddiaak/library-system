package library_service.library.service;

import library_service.library.dto.LibraryRequest;
import library_service.library.dto.LibraryUpdateRequest;
import library_service.library.dto.library.LibraryFullDTO;
import library_service.library.entity.Library;
import library_service.library.repository.api.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public List<LibraryFullDTO> getFreeBooks() {
        return libraryRepository.findFreeBooks()
                .stream()
                .map(library -> new LibraryFullDTO(library.getBookId(), library.getBorrowTime(), library.getReturnTime()))
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
