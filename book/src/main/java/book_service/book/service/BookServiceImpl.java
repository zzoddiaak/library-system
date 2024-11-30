package book_service.book.service;

import book_service.book.config.security.RestTemplateAuthInterceptor;
import book_service.book.dto.LibraryRequest;
import book_service.book.dto.DtoMapper;
import book_service.book.dto.books.BookCreateRequestDTO;
import book_service.book.dto.books.BookFullResponseDTO;
import book_service.book.entity.Book;
import book_service.book.exeption.book.BookNotFoundException;
import book_service.book.repository.BookRepository;
import book_service.book.service.api.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final DtoMapper dtoMapper;
    private final RestTemplate restTemplate;
    private final RestTemplateAuthInterceptor authInterceptor;


    @Override
    public BookFullResponseDTO getBookByIsbn(Long isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(book -> dtoMapper.convertToDto(book, BookFullResponseDTO.class))
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
    }

    @Override
    public List<BookFullResponseDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(book -> dtoMapper.convertToDto(book, BookFullResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookFullResponseDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(book -> dtoMapper.convertToDto(book, BookFullResponseDTO.class))
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    @Override
    public BookFullResponseDTO createBook(BookCreateRequestDTO bookDto) {
        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new IllegalArgumentException("A book with ISBN " + bookDto.getIsbn() + " already exists.");
        }

        Book book = dtoMapper.convertToEntity(bookDto, Book.class);  // Mapping Request DTO to Entity
        bookRepository.save(book);

        HttpEntity<LibraryRequest> entity = authInterceptor.createAuthEntity(new LibraryRequest(book.getId()));
        restTemplate.postForEntity("http://localhost:8081/api/library", entity, Void.class);

        return dtoMapper.convertToDto(book, BookFullResponseDTO.class);  // Mapping Entity to Response DTO
    }

    @Override
    public BookFullResponseDTO updateBook(Long id, BookCreateRequestDTO bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));

        if (!book.getIsbn().equals(bookDto.getIsbn()) &&
                bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new IllegalArgumentException("A book with ISBN " + bookDto.getIsbn() + " already exists.");
        }

        book.setTitle(bookDto.getTitle());
        book.setGenre(bookDto.getGenre());
        book.setDescription(bookDto.getDescription());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());

        bookRepository.save(book);
        return dtoMapper.convertToDto(book, BookFullResponseDTO.class);
    }


    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}


