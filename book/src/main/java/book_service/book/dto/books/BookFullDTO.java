package book_service.book.dto.books;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFullDTO {
    private Long id;
    private Long isbn;

    private String title;

    private String genre;

    private String description;

    private String author;
}