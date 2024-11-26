package library_service.library.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public abstract class AbstractRepository<ID, T> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> entityClass;

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T findById(ID id) {
        T entity = entityManager.find(entityClass, id);

        return entity;
    }

    public void save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
            if (getId(entity) != null) {
                entityManager.merge(entity);
            } else {
                entityManager.persist(entity);
            }

    }

    public List<T> findAll() {
        String queryString = String.format("SELECT u FROM %s u", entityClass.getSimpleName());
        TypedQuery<T> query = entityManager.createQuery(queryString, entityClass);
        return query.getResultList();
    }

    public void deleteById(ID id) {
            T entity = findById(id);
            entityManager.remove(entity);

    }

    public void update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        entityManager.merge(entity);
    }

    @SuppressWarnings("unchecked")
    private ID getId(T entity) {

        try {
            return (ID) entityClass.getMethod("getId").invoke(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get entity ID", e);
        }
    }
}