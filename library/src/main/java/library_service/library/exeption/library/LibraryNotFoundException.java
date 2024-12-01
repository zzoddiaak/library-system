package library_service.library.exeption.library;

public class LibraryNotFoundException extends RuntimeException {
    private static final String message = "Library record not found for ID: ";

    public LibraryNotFoundException(Long id) {
        super(message + id);
    }
}

