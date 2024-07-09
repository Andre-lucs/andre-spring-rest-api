package org.andrelucs.examplerestapi.unittests.service;

import org.andrelucs.examplerestapi.model.Book;
import org.andrelucs.examplerestapi.model.dto.BookDTO;
import org.andrelucs.examplerestapi.model.mapping.BookMapper;
import org.andrelucs.examplerestapi.repository.BookRepository;
import org.andrelucs.examplerestapi.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookServiceTest {

    BookDTO bookDTO;
    Book book;

    @InjectMocks
    private BookService service;

    @Mock
    private BookRepository repository;

    @Mock
    private BookMapper mapperMock;

    private BookMapper mapper = Mappers.getMapper(BookMapper.class);

    @BeforeEach
    void setUpTestBook() {
        bookDTO = new BookDTO();
        bookDTO.setKey(1L);
        bookDTO.setTitle("title");
        bookDTO.setAuthor("author");
        bookDTO.setLaunchDate(new java.util.Date());
        bookDTO.setPrice(10.0);
        MockitoAnnotations.openMocks(this);
        book = mapper.bookDTOToBook(bookDTO);
    }

    @Test
    void findByIdTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(mapperMock.bookToBookDTO(any())).thenReturn(bookDTO);

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/books/1>;rel=\"self\"]"));
        assertEquals(1L, result.getKey());
        assertEquals("title", result.getTitle());
        assertEquals("author", result.getAuthor());
        assertEquals(10.0, result.getPrice());
        assertEquals(bookDTO.getLaunchDate(), result.getLaunchDate());
    }

    @Test
    void findAllTest(){
        var list = List.of(book, book);
        when(repository.findAll()).thenReturn(list);
        when(mapperMock.bookListToBookDTOList(any())).thenReturn(mapper.bookListToBookDTOList(list));

        var result = service.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertNotNull(result.getFirst().getLinks());
        assertEquals(1L, result.getFirst().getKey());
        assertEquals("title", result.getFirst().getTitle());
        assertEquals("author", result.getFirst().getAuthor());
        assertEquals(10.0, result.getFirst().getPrice());
        assertEquals(bookDTO.getLaunchDate(), result.getFirst().getLaunchDate());
        assertTrue(result.getFirst().toString().contains("links: [</api/books/1>;rel=\"self\"]"));
    }

    @Test
    void insertTest(){
        when(repository.save(any())).thenReturn(book);
        when(mapperMock.bookToBookDTO(any())).thenReturn(bookDTO);
        when(mapperMock.bookDTOToBook(any())).thenReturn(book);

        var result = service.insert(bookDTO);
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/books/1>;rel=\"self\"]"));
        assertEquals(1L, result.getKey());
        assertEquals("title", result.getTitle());
        assertEquals("author", result.getAuthor());
        assertEquals(10.0, result.getPrice());
        assertEquals(bookDTO.getLaunchDate(), result.getLaunchDate());
    }

    @Test
    void updateTest(){
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(any())).thenReturn(book);
        when(mapperMock.bookToBookDTO(any())).thenReturn(bookDTO);
        when(mapperMock.bookDTOToBook(any())).thenReturn(book);

        var result = service.update(bookDTO);
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/books/1>;rel=\"self\"]"));
        assertEquals(1L, result.getKey());
        assertEquals("title", result.getTitle());
        assertEquals("author", result.getAuthor());
        assertEquals(10.0, result.getPrice());
        assertEquals(bookDTO.getLaunchDate(), result.getLaunchDate());
    }

    @Test
    void deleteTest(){
        //when(repository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
