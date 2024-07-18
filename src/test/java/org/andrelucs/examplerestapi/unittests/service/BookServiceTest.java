package org.andrelucs.examplerestapi.unittests.service;

import org.andrelucs.examplerestapi.exceptions.BadRequestException;
import org.andrelucs.examplerestapi.mockers.BookDTOMocker;
import org.andrelucs.examplerestapi.mockers.BookMocker;
import org.andrelucs.examplerestapi.model.dto.BookDTO;
import org.andrelucs.examplerestapi.model.mapping.BookMapper;
import org.andrelucs.examplerestapi.repository.BookRepository;
import org.andrelucs.examplerestapi.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@RunWith(MockitoJUnitRunner.class)

class BookServiceTest {

    @Mock
    private BookRepository repository;
    
    @Mock
    private BookMapper mapper;
    
    @InjectMocks
    private BookService bookService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
        var book = BookMocker.WithId();
        Mockito.when(mapper.bookToBookDTO(any())).thenReturn(BookDTOMocker.WithId());
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(book));

        var result = bookService.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertEquals(book.getId(), result.getKey());
        assertTrue(result.toString().contains("links: [</api/books/1>;rel=\"self\"]"));
        assertEquals(book.getId(), result.getKey());
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    void insert() {
        var book = BookMocker.WithId();
        Mockito.when(mapper.bookToBookDTO(any())).thenReturn(BookDTOMocker.WithId());
        Mockito.when(repository.save(any())).thenReturn(book);

        var result = bookService.insert(BookDTOMocker.WithId());

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertEquals(book.getId(), result.getKey());
        assertTrue(result.toString().contains("links: [</api/books/1>;rel=\"self\"]"));
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    void update() {
        var book = BookMocker.WithId();
        Mockito.when(mapper.bookToBookDTO(any())).thenReturn(BookDTOMocker.WithId());
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(repository.save(any())).thenReturn(book);

        var result = bookService.update(BookDTOMocker.WithId());

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertEquals(book.getId(), result.getKey());
        assertTrue(result.toString().contains("links: [</api/books/1>;rel=\"self\"]"));
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPrice(), result.getPrice());
    }

    @Test
    void updateWithNull(){
        assertThrows(BadRequestException.class, ()->{
            bookService.update(null);
        });
        assertThrows(BadRequestException.class, ()->{
            bookService.update(new BookDTO());
        });
    }
}