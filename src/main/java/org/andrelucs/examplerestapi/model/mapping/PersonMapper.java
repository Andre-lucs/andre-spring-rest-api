package org.andrelucs.examplerestapi.model.mapping;

import org.andrelucs.examplerestapi.model.Person;
import org.andrelucs.examplerestapi.model.dto.PersonDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {
    @Mapping(source = "id", target = "key")
    PersonDTO personToPersonDTO(Person person);
    @Mapping(source = "key", target = "id")
    Person personDTOToPerson(PersonDTO person);
    @Mapping(source = "id", target = "key")
    List<PersonDTO> personListToPersonDTOList(List<Person> list);
    @Mapping(source = "key", target = "id")
    List<Person> personDTOListToPersonList(List<PersonDTO> list);
}
