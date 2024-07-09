package org.andrelucs.examplerestapi.unittests.mapping;

import org.andrelucs.examplerestapi.model.dto.BookDTO;
import org.andrelucs.examplerestapi.model.Book;
import org.andrelucs.examplerestapi.model.mapping.BookMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookMapperTests {
    //private static BookMapper mapper = Mappers.getMapper(BookMapper.class);
    @Autowired
    private BookMapper mapper;

    @Test
    public void bookToBookDTOTest() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setLaunchDate(new java.util.Date());
        book.setPrice(10.0);
        BookDTO bookDTO = mapper.bookToBookDTO(book);
        System.out.println(bookDTO);
        assertNotNull(bookDTO.getTitle());
        assertNotNull(bookDTO.getAuthor());
        assertNotNull(bookDTO.getLaunchDate());
        assertNotNull(bookDTO.getPrice());
        assertNotNull(bookDTO.getKey());
    }

    @Test
    public void bookDTOToBookTest() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setKey(1L);
        bookDTO.setTitle("title");
        bookDTO.setAuthor("author");
        bookDTO.setLaunchDate(new java.util.Date());
        bookDTO.setPrice(10.0);
        Book book = mapper.bookDTOToBook(bookDTO);
        System.out.println(book);
        assertNotNull(book.getTitle());
        assertNotNull(book.getAuthor());
        assertNotNull(book.getLaunchDate());
        assertNotNull(book.getPrice());
        assertNotNull(book.getId());
    }

    @Test
    public void bookListToBookDTOListTest(){
        Book book = new Book();
        book.setId(1L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setLaunchDate(new java.util.Date());
        book.setPrice(10.0);
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("title2");
        book2.setAuthor("author2");
        book2.setLaunchDate(new java.util.Date());
        book2.setPrice(10.0);
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        bookList.add(book2);
        List<BookDTO> bookDTOList = mapper.bookListToBookDTOList(bookList);
        System.out.println(bookDTOList);
        assertNotNull(bookDTOList.get(0));
        assertNotNull(bookDTOList.get(1));
    }

    @Test
    public void bookDTOListToBookListTest(){
        BookDTO bookDTO = new BookDTO();
        bookDTO.setKey(1L);
        bookDTO.setTitle("title");
        bookDTO.setAuthor("author");
        bookDTO.setLaunchDate(new java.util.Date());
        bookDTO.setPrice(10.0);
        BookDTO bookDTO2 = new BookDTO();
        bookDTO2.setKey(2L);
        bookDTO2.setTitle("title2");
        bookDTO2.setAuthor("author2");
        bookDTO2.setLaunchDate(new java.util.Date());
        bookDTO2.setPrice(10.0);
        List<BookDTO> bookDTOList = new ArrayList<>();
        bookDTOList.add(bookDTO);
        bookDTOList.add(bookDTO2);
        List<Book> bookList = mapper.bookDTOListToBookList(bookDTOList);
        System.out.println(bookList);
        assertNotNull(bookList.get(0));
        assertNotNull(bookList.get(1));
    }
}
