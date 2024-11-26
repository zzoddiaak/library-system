package library_service.library.dto.library;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class LibraryFullDTO {
    private Long bookId;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;

    public LibraryFullDTO(Long bookId, LocalDateTime borrowTime, LocalDateTime returnTime) {
        this.bookId = bookId;
        this.borrowTime = borrowTime;
        this.returnTime = returnTime;
    }
}


