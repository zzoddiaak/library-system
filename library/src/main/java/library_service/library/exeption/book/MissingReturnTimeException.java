package library_service.library.exeption.book;

public class MissingReturnTimeException extends RuntimeException {
    private static final String message = "Return time cannot be null when borrow time is specified.";
    public MissingReturnTimeException() {
        super(message);
    }
}
