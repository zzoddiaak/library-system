package library_service.library.exeption;


import library_service.library.exeption.book.MissingBorrowTimeException;
import library_service.library.exeption.book.MissingReturnTimeException;
import library_service.library.exeption.book.TimeOrderViolationException;
import library_service.library.exeption.library.LibraryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LibraryNotFoundException.class)
    public ResponseEntity<String> handleLibraryRecordNotFound(LibraryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({
            MissingBorrowTimeException.class,
            MissingReturnTimeException.class,
            TimeOrderViolationException.class
    })
    public ResponseEntity<Object> handleException(Exception e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}

