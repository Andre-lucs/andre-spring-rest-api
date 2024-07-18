package org.andrelucs.examplerestapi.unittests.service;

import org.andrelucs.examplerestapi.exceptions.BadRequestException;
import org.andrelucs.examplerestapi.exceptions.NotFoundException;
import org.andrelucs.examplerestapi.mockers.PersonDTOMocker;
import org.andrelucs.examplerestapi.mockers.PersonMocker;
import org.andrelucs.examplerestapi.model.dto.PersonDTO;
import org.andrelucs.examplerestapi.model.mapping.PersonMapper;
import org.andrelucs.examplerestapi.repository.PersonRepository;
import org.andrelucs.examplerestapi.service.PersonService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@RunWith(MockitoJUnitRunner.class)
class PersonServiceTest {
    @Mock
    PersonRepository repository;

    @Mock
    PersonMapper mapper;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        System.out.println("before all "+repository.hashCode());
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
        var person = PersonMocker.WithId();
        Mockito.when(mapper.personToPersonDTO(any())).thenReturn(PersonDTOMocker.WithId());
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));

        var result = personService.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertEquals(person.getId(), result.getKey());
        assertTrue(result.toString().contains("links: [</api/person/1>;rel=\"self\"]"));
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getEnabled(), result.getEnabled());
        assertEquals(person.getGender(), result.getGender());
        assertEquals(person.getAddress(), result.getAddress());

    }

    @Test
    void insert() {
        var person = PersonMocker.WithId();
        var persondto = PersonDTOMocker.WithId();
        Mockito.when(repository.findById(any())).thenReturn(Optional.of(person));
        Mockito.when(repository.save(any())).thenReturn(person);
        Mockito.when(mapper.personToPersonDTO(notNull())).thenReturn(persondto);
        Mockito.when(mapper.personDTOToPerson(notNull())).thenReturn(person);

        PersonDTO result = personService.insert(persondto);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertEquals(person.getId(), result.getKey());
        assertTrue(result.toString().contains("links: [</api/person/1>;rel=\"self\"]"));
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getEnabled(), result.getEnabled());
        assertEquals(person.getGender(), result.getGender());
        assertEquals(person.getAddress(), result.getAddress());

    }

    @Test
    void insertWithNullBody(){
        assertThrows(BadRequestException.class,()->{
            personService.insert(null);
        });
    }

    @Test
    void update() {
        var person = PersonMocker.WithId();
        var persondto = PersonDTOMocker.WithId();


        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(person));
        var modifiedPerson = PersonMocker.WithId();
        modifiedPerson.setLastName("Robert");
        var modifiedPersonDto = PersonDTOMocker.WithId();
        modifiedPersonDto.setLastName("Robert");
        Mockito.when(repository.save(any())).thenReturn(modifiedPerson);
        Mockito.when(mapper.personToPersonDTO(any())).thenReturn(modifiedPersonDto);
        Mockito.when(mapper.personDTOToPerson(any())).thenReturn(modifiedPerson);

        var result = personService.update(modifiedPersonDto);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertEquals(modifiedPerson.getId(), result.getKey());
        assertTrue(result.toString().contains("links: [</api/person/1>;rel=\"self\"]"));
        assertEquals(modifiedPerson.getFirstName(), result.getFirstName());
        assertEquals(modifiedPerson.getLastName(), result.getLastName());
        assertEquals(modifiedPerson.getEnabled(), result.getEnabled());
        assertEquals(modifiedPerson.getGender(), result.getGender());
        assertEquals(modifiedPerson.getAddress(), result.getAddress());
    }

    @Test
    void updateWithNullBody(){
        assertThrows(BadRequestException.class,()->{
            personService.update(null);
        });
    }

    @Test
    void updateWithNullId(){
        assertThrows(BadRequestException.class,
                ()-> personService.update(PersonDTOMocker.WithoutId()));
    }

    @Test
    void updateWithNonexistentId(){
        Mockito.when(repository.findById(-1L)).thenReturn(Optional.empty());

        var nonexistentPerson = PersonDTOMocker.WithId();
        nonexistentPerson.setKey(-1L);
        assertThrows(NotFoundException.class, ()-> personService.update(nonexistentPerson));
    }

    @Test
    void delete() {
    }

    @Test
    void disablePerson() {
    }

    @Test
    void findByName() {
    }
}