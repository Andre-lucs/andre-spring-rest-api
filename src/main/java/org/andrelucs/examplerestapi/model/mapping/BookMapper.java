package org.andrelucs.examplerestapi.model.mapping;

import org.andrelucs.examplerestapi.model.dto.BookDTO;
import org.andrelucs.examplerestapi.model.Book;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {
    @Mapping(source = "id", target = "key")
    BookDTO bookToBookDTO(Book book);
    @Mapping(source = "key", target = "id")
    Book bookDTOToBook(BookDTO book);
    @Mapping(source = "id", target = "key")
    List<BookDTO> bookListToBookDTOList(List<Book> list);
    @Mapping(source = "key", target = "id")
    List<Book> bookDTOListToBookList(List<BookDTO> list);
}
