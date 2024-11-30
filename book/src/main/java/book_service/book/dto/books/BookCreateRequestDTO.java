package book_service.book.dto.books;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateRequestDTO {
    private Long isbn;
    private String title;
    private String genre;
    private String description;
    private String author;
}
