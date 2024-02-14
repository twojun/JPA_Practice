package jpa_basic_shop.jpa_basic_shop.dto;

import jpa_basic_shop.jpa_basic_shop.domain.Address;
import jpa_basic_shop.jpa_basic_shop.domain.Order;
import jpa_basic_shop.jpa_basic_shop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * V3
 */
@Data
public class SimpleOrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address =  order.getDelivery().getAddress();
    }
}
