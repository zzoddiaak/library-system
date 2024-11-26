package library_service.library.repository.api;



import library_service.library.entity.Library;

import java.util.List;

public interface LibraryRepository {
    Library findById(Long id);
    List<Library> findAll();
    void save(Library library);
    void deleteById(Long id);
    void update(Library library);

    List<Library> findFreeBooks();
}
