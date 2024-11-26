package library_service.library.dto.library;

import library_service.library.entity.Library;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
public class LibraryFullDTO {
    private Long id;
    private Long bookId;
    private String title;
    private String author;
    private String genre;
    private String description;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;

    public LibraryFullDTO(Library library, LibraryBookDTO bookDetails) {
        this.id = library.getId();
        this.bookId = library.getBookId();
        this.title = bookDetails.getTitle();
        this.author = bookDetails.getAuthor();
        this.genre = bookDetails.getGenre();
        this.description = bookDetails.getDescription();
        this.borrowTime = library.getBorrowTime();
        this.returnTime = library.getReturnTime();
    }
}


