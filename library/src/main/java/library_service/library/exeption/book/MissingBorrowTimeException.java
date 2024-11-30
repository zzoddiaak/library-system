package library_service.library.exeption.book;

public class MissingBorrowTimeException extends RuntimeException {
    private static final String message = "Borrow time must be specified when return time is provided.";
    public MissingBorrowTimeException() {
        super(message);
    }
}
