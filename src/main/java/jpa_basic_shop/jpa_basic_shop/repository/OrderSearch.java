package jpa_basic_shop.jpa_basic_shop.repository;

import jpa_basic_shop.jpa_basic_shop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
