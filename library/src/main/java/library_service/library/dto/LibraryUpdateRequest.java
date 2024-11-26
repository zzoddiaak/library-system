package library_service.library.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LibraryUpdateRequest {
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
}
