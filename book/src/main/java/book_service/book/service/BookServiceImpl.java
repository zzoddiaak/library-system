package book_service.book.service;

import book_service.book.dto.LibraryRequest;
import book_service.book.dto.DtoMapper;
import book_service.book.dto.books.BookFullDTO;
import book_service.book.entity.Books;
import book_service.book.exeption.book.BookNotFoundException;
import book_service.book.repository.api.BookRepository;
import book_service.book.service.api.BookService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public List<BookFullDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(book -> dtoMapper.convertToDto(book, BookFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookFullDTO getBookById(Long id) {
        return dtoMapper.convertToDto(
                bookRepository.findById(id),
                BookFullDTO.class);
    }

    @Override
    public BookFullDTO getBookByIsbn(Long isbn) {
        Books book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Книга с ISBN " + isbn + " не найдена");
        }
        return dtoMapper.convertToDto(book, BookFullDTO.class);
    }


    @Override
    public BookFullDTO createBook(BookFullDTO bookDto) {
        Books book = dtoMapper.convertToEntity(bookDto, Books.class);
        bookRepository.save(book);
        restTemplate.postForEntity("http://localhost:8081/api/library", new LibraryRequest(book.getId()), Void.class);
        return dtoMapper.convertToDto(book, BookFullDTO.class);
    }

    @Override
    public BookFullDTO updateBook(Long id, BookFullDTO bookDto) {
        Books book = bookRepository.findById(id);
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
