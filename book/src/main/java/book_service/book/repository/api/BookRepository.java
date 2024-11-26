package book_service.book.repository.api;



import book_service.book.entity.Books;

import java.util.List;

public interface BookRepository {
    Books findById(Long id);

    List<Books> findAll();

    void save(Books book);

    void deleteById(Long id);

    void update(Books book);

    Books findByIsbn(Long isbn);
}

