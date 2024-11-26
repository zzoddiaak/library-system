package library_service.library.dto.library;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryFullDTO {
    private Long bookId;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;

}


