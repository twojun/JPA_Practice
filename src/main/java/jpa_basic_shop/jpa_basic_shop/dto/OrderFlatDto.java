package jpa_basic_shop.jpa_basic_shop.dto;

import jpa_basic_shop.jpa_basic_shop.domain.Address;
import jpa_basic_shop.jpa_basic_shop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFlatDto {

    /**
     * Order -> OrderItem Join
     * Order -> Item Join
     * 해당 join을 통해 특정 API에서 필요한 데이터를 모두 담는 FlatDto
     */
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId,
                        String name,
                        LocalDateTime orderDate,
                        OrderStatus orderStatus,
                        Address address,
                        String itemName,
                        int orderPrice,
                        int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
