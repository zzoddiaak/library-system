package library_service.library.exeption.library;

public class LibraryNotFoundException extends RuntimeException {
    public LibraryNotFoundException(Long id) {
        super("Library record not found for ID: " + id);
    }
}

