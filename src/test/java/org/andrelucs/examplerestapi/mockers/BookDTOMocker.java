package org.andrelucs.examplerestapi.mockers;

import org.andrelucs.examplerestapi.model.Book;
import org.andrelucs.examplerestapi.model.dto.BookDTO;

public class BookDTOMocker {
    public static BookDTO WithId() {
        return new BookDTO(
                1L,
                "Clean Code",
                "Robert C. Martin",
                new java.util.Date(),
                19.99
        );
    }

    public static BookDTO WithoutId() {
        return new BookDTO(
                null,
                "Clean Code",
                "Robert C. Martin",
                new java.util.Date(),
                19.99
        );
    }
}