package org.andrelucs.examplerestapi.service;

import org.andrelucs.examplerestapi.controller.BookController;
import org.andrelucs.examplerestapi.model.dto.BookDTO;
import org.andrelucs.examplerestapi.exceptions.NotFoundException;
import org.andrelucs.examplerestapi.model.mapping.BookMapper;
import org.andrelucs.examplerestapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper mapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper){
        this.bookRepository = bookRepository;
        this.mapper = bookMapper;
    }

    public List<BookDTO> findAll(){
        var books = bookRepository.findAll();
        return mapper.bookListToBookDTOList(books).stream()
                .map(book -> book.add(
                        linkTo(methodOn(BookController.class).findById(book.getKey())).withSelfRel())
                ).toList();
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
