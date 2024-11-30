package book_service.book.service;

import book_service.book.config.security.RestTemplateAuthInterceptor;
import book_service.book.dto.LibraryRequest;
import book_service.book.dto.DtoMapper;
import book_service.book.dto.books.BookFullDTO;
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
    public List<BookFullDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(book -> dtoMapper.convertToDto(book, BookFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookFullDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(book -> dtoMapper.convertToDto(book, BookFullDTO.class))
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    @Override
    public BookFullDTO getBookByIsbn(Long isbn) {
        return bookRepository.findByIsbn(isbn)
                .map(book -> dtoMapper.convertToDto(book, BookFullDTO.class))
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
    }

    @Override
    public BookFullDTO createBook(BookFullDTO bookDto) {
        Book book = dtoMapper.convertToEntity(bookDto, Book.class);
        bookRepository.save(book);

        HttpEntity<LibraryRequest> entity = authInterceptor.createAuthEntity(new LibraryRequest(book.getId()));
        restTemplate.postForEntity("http://localhost:8081/api/library", entity, Void.class);

        return dtoMapper.convertToDto(book, BookFullDTO.class);
    }

    @Override
    public BookFullDTO updateBook(Long id, BookFullDTO bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        book.setTitle(bookDto.getTitle());
        book.setGenre(bookDto.getGenre());
        book.setDescription(bookDto.getDescription());
        book.setAuthor(bookDto.getAuthor());
        bookRepository.save(book);
        return dtoMapper.convertToDto(book, BookFullDTO.class);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

