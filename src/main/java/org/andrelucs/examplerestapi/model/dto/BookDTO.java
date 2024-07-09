package org.andrelucs.examplerestapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;

@JsonPropertyOrder({"key", "title", "author", "price", "launch_date" })
public class BookDTO extends RepresentationModel<BookDTO> {

    private Long key;
    private String title;
    private String author;
    @JsonProperty("launch_date")
    private Date launchDate;
    private Double price;

    public BookDTO(){}

    public BookDTO(Long key, String title, String author, Date launchDate, Double price) {
        this.key = key;
        this.title = title;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
    }

    public BookDTO(Long id) {
        setKey(id);
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Objects.equals(key, bookDTO.key) && Objects.equals(title, bookDTO.title) && Objects.equals(author, bookDTO.author) && Objects.equals(launchDate, bookDTO.launchDate) && Objects.equals(price, bookDTO.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, title, author, launchDate, price);
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "key=" + key +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", launchDate=" + launchDate +
                ", price=" + price +
                super.toString()+"} " ;
    }
}
