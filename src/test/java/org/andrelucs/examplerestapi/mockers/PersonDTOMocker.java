package org.andrelucs.examplerestapi.mockers;

import org.andrelucs.examplerestapi.model.dto.PersonDTO;

public class PersonDTOMocker {
    public static PersonDTO WithId() {
        return new PersonDTO(
                1L,
                "John",
                "Doe",
                "Male",
                "123 Main St",
                true

        );
    }

    public static PersonDTO WithoutId() {
        return new PersonDTO(
                null,
                "John",
                "Doe",
                "Male",
                "123 Main St",
                true
        );
    }
}