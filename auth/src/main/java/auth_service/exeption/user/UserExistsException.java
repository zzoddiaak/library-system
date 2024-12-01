package auth_service.exeption.user;

public class UserExistsException extends RuntimeException {
    private static final String message = "User already exists";
    public UserExistsException() {
        super(message);
    }
}
