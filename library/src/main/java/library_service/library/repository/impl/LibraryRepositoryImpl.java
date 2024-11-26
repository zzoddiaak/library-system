package library_service.library.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import library_service.library.entity.Library;
import library_service.library.repository.AbstractRepository;
import library_service.library.repository.api.LibraryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public class LibraryRepositoryImpl extends AbstractRepository<Long, Library> implements LibraryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public LibraryRepositoryImpl() {
        super(Library.class);
    }

    @Override
    public List<Library> findFreeBooks() {
        TypedQuery<Library> query = entityManager.createQuery(
                "SELECT l FROM Library l WHERE l.returnTime IS NULL", Library.class);
        return query.getResultList();
    }

}
