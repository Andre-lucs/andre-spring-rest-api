package org.andrelucs.examplerestapi.service;

import org.andrelucs.examplerestapi.controller.PersonController;
import org.andrelucs.examplerestapi.exceptions.BadRequestException;
import org.andrelucs.examplerestapi.exceptions.NotFoundException;
import org.andrelucs.examplerestapi.model.Person;
import org.andrelucs.examplerestapi.model.dto.PersonDTO;
import org.andrelucs.examplerestapi.model.mapping.PersonMapper;
import org.andrelucs.examplerestapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper mapper;
    private final PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler;

    public PersonService(PersonRepository personRepository, PersonMapper mapper, PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler) {
        this.personRepository = personRepository;
        this.mapper = mapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }


    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable){
        var persons = personRepository.findAll(pageable);
        return getEntityModels(pageable, persons);
    }

    public PersonDTO findById(Long id){
        var person = personRepository.findById(id)
                        .orElseThrow(()->new NotFoundException("Person not found"));
        var personDto = mapper.personToPersonDTO(person);
        personDto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personDto;
    }

    public PersonDTO insert(PersonDTO person){
        //data validation
        if (person == null)
            throw new BadRequestException("Passed Person or id is null");
        System.out.println(person);
        System.out.println(mapper.personDTOToPerson(person));
        var savedPerson = mapper.personToPersonDTO(personRepository.save(mapper.personDTOToPerson(person)));
        savedPerson.add(linkTo(methodOn(PersonController.class).findById(savedPerson.getKey())).withSelfRel());
        return savedPerson;
    }

    public PersonDTO update(PersonDTO personDTO){
        if(personDTO == null || personDTO.getKey() == null) throw new BadRequestException("Passed person or person id is null");
        var oldPerson = personRepository.findById(personDTO.getKey());
        if(oldPerson.isEmpty()){
            throw new NotFoundException("Person not found");
        }
        //validate new data
        var updatedPerson = mapper.personToPersonDTO(personRepository.save(mapper.personDTOToPerson(personDTO)));
        updatedPerson.add(linkTo(methodOn(PersonController.class).findById(updatedPerson.getKey())).withSelfRel());
        return updatedPerson;
    }

    public void delete(Long id){
        personRepository.deleteById(id);
    }

    @Transactional
    public void disablePerson(Long id){
        personRepository.disablePerson(id);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String name, Pageable pageable){
        var persons = personRepository.findPersonByNames(name,pageable);
        return getEntityModels(pageable, persons);
    }

    private PagedModel<EntityModel<PersonDTO>> getEntityModels(Pageable pageable, Page<Person> persons) {
        var personDtos = persons.map(mapper::personToPersonDTO);
        personDtos.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(),pageable.getPageSize(), "asc")).withSelfRel();
        var model = pagedResourcesAssembler.toModel(personDtos,link);
        return model;
    }
}
