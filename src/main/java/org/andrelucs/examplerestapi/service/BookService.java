package org.andrelucs.examplerestapi.service;

import org.andrelucs.examplerestapi.controller.BookController;
import org.andrelucs.examplerestapi.exceptions.BadRequestException;
import org.andrelucs.examplerestapi.model.Book;
import org.andrelucs.examplerestapi.model.dto.BookDTO;
import org.andrelucs.examplerestapi.exceptions.NotFoundException;
import org.andrelucs.examplerestapi.model.dto.PersonDTO;
import org.andrelucs.examplerestapi.model.mapping.BookMapper;
import org.andrelucs.examplerestapi.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper mapper;
    private final PagedResourcesAssembler<BookDTO> pagedResourcesAssembler;


    public BookService(BookRepository bookRepository, BookMapper mapper, PagedResourcesAssembler<BookDTO> pagedResourcesAssembler) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable){
        Page<Book> books = bookRepository.findAll(pageable);

        return getEntityModels(pageable, books);
    }

    private PagedModel<EntityModel<BookDTO>> getEntityModels(Pageable pageable, Page<Book> books) {
        Page<BookDTO> booksDTOs = books.map(mapper::bookToBookDTO);
        booksDTOs.map(bookDTO -> linkTo(methodOn(BookController.class).findById(bookDTO.getKey())).withSelfRel());
        Link allLink = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return pagedResourcesAssembler.toModel(booksDTOs, allLink);
    }

    public BookDTO findById(Long id){
        var book = mapper.bookToBookDTO(bookRepository.findById(id).orElseThrow(()->new NotFoundException("Book not found")));
        book.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return book;
    }

    public BookDTO insert(BookDTO book){
        //data validation
        var savedBook = mapper.bookToBookDTO(bookRepository.save(mapper.bookDTOToBook(book)));
        savedBook.add(linkTo(methodOn(BookController.class).findById(savedBook.getKey())).withSelfRel());
        return savedBook;
    }

    public BookDTO update(BookDTO bookDTO){
        if(bookDTO == null || bookDTO.getKey() == null)
            throw new BadRequestException("Invalid book data");
        var oldBook = bookRepository.findById(bookDTO.getKey());
        if(oldBook.isEmpty()){
            throw new NotFoundException("Book not found");
        }
        //validate new data
        var updatedBook = mapper.bookToBookDTO(bookRepository.save(mapper.bookDTOToBook(bookDTO)));
        updatedBook.add(linkTo(methodOn(BookController.class).findById(updatedBook.getKey())).withSelfRel());
        return updatedBook;
    }

    public void delete(Long id){
        bookRepository.deleteById(id);
    }
}
