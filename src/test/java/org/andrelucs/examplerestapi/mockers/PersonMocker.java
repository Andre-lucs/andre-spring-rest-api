package org.andrelucs.examplerestapi.mockers;

import org.andrelucs.examplerestapi.model.Person;

public class PersonMocker {
    public static Person WithId() {
        return new Person(
                1L,
                "John",
                "Doe",
                "Male",
                "123 Main St",
                true
        );
    }

    public static Person WithoutId() {
        return new Person(
                null,
                "John",
                "Doe",
                "Male",
                "123 Main St",
                true
        );
    }
}