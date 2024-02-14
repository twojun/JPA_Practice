package jpa_basic_shop.jpa_basic_shop.api;

import jpa_basic_shop.jpa_basic_shop.domain.Order;
import jpa_basic_shop.jpa_basic_shop.domain.OrderItem;
import jpa_basic_shop.jpa_basic_shop.dto.OrderDto;
import jpa_basic_shop.jpa_basic_shop.repository.OrderRepository;
import jpa_basic_shop.jpa_basic_shop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    /**
     * V1 : Entity 직접 노출, 사용 X
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream()
                    .forEach(orderElement-> orderElement.getItem().getName());
        }

        return all;
    }

    /**
     * V2 : Entity -> DTO
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        return orders.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());
    }
}

