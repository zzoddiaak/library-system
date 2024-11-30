package library_service.library.repository;

import library_service.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    @Query("SELECT l FROM Library l WHERE l.borrowTime IS NULL")
    List<Library> findFreeBooks();
}
