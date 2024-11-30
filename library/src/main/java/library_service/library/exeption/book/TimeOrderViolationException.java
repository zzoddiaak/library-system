package library_service.library.exeption.book;

public class TimeOrderViolationException extends RuntimeException {
    private static final String message = "Borrow time cannot be after return time.";
    public TimeOrderViolationException() {
        super(message);
    }
}
