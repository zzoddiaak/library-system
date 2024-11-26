package library_service.library.dto.library;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBookDTO {

    private Long id;
    private Long isbn;
    private String title;
    private String genre;
    private String description;
    private String author;
}

