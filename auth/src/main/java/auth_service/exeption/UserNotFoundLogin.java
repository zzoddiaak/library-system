package auth_service.exeption;

public class UserNotFoundLogin extends RuntimeException {
    private static final String message = "User not found login";
    public UserNotFoundLogin(String message) {
        super(message);
    }
}
