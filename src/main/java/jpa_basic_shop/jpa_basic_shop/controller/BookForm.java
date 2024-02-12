package jpa_basic_shop.jpa_basic_shop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private Long id;

    @NotEmpty(message = "상품 이름은 공백일 수 없습니다." )
    private String name;

    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
