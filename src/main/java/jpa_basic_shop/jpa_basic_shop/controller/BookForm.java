package jpa_basic_shop.jpa_basic_shop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
