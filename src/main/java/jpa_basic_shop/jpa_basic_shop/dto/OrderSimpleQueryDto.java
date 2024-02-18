package jpa_basic_shop.jpa_basic_shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpa_basic_shop.jpa_basic_shop.domain.Address;
import jpa_basic_shop.jpa_basic_shop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * V4
 */
@Data
public class OrderSimpleQueryDto {

    @JsonIgnore
    private Long orderId;

    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address =  address;
    }
}