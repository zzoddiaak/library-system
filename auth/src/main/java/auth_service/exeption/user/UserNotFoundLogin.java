package auth_service.exeption.user;

public class UserNotFoundLogin extends RuntimeException {
    private static final String message = "User not found login";
    public UserNotFoundLogin(String login) {
        super(message);
    }
}
