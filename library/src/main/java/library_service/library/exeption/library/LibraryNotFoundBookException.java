package library_service.library.exeption.library;

public class LibraryNotFoundBookException extends RuntimeException {
    private static final String message = "Library entry not found for book ID: ";

    public LibraryNotFoundBookException(Long id) {
        super(message + id);
    }
}

