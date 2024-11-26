package book_service.book.repository.impl;


import book_service.book.entity.Books;
import book_service.book.repository.AbstractRepository;
import book_service.book.repository.api.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepositoryImpl extends AbstractRepository<Long, Books> implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public BookRepositoryImpl() {
        super(Books.class);
    }



    @Override
    public Books findByIsbn(Long isbn) {
        TypedQuery<Books> query = entityManager.createQuery(
                "SELECT b FROM Books b WHERE b.isbn = :isbn", Books.class);
        query.setParameter("isbn", isbn);
        List<Books> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }



}
