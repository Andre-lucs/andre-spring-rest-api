package org.andrelucs.examplerestapi.mockers;

import org.andrelucs.examplerestapi.model.Book;
import org.andrelucs.examplerestapi.model.Person;

public class BookMocker {
    public static Book WithId() {
        return new Book(
                1L,
                "Clean Code",
                "Robert C. Martin",
                new java.util.Date(),
                19.99
        );
    }

    public static Book WithoutId() {
        return new Book(
                null,
                "Clean Code",
                "Robert C. Martin",
                new java.util.Date(),
                19.99
        );
    }
}