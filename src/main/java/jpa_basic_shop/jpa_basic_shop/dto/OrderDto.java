package jpa_basic_shop.jpa_basic_shop.dto;

import jpa_basic_shop.jpa_basic_shop.domain.Address;
import jpa_basic_shop.jpa_basic_shop.domain.Order;
import jpa_basic_shop.jpa_basic_shop.domain.OrderItem;
import jpa_basic_shop.jpa_basic_shop.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress();

        this.orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem))
                .collect(Collectors.toList());
    }
}
