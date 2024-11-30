package book_service.book.config.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestTemplateAuthInterceptor {

    private final HttpServletRequest request;

    public <T> HttpEntity<T> createAuthEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            headers.set("Authorization", authHeader);
        }
        return new HttpEntity<>(body, headers);
    }
}
