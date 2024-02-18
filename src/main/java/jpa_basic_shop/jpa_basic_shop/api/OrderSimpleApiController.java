package jpa_basic_shop.jpa_basic_shop.api;

import jpa_basic_shop.jpa_basic_shop.domain.Order;
import jpa_basic_shop.jpa_basic_shop.dto.OrderSimpleQueryDto;
import jpa_basic_shop.jpa_basic_shop.dto.SimpleOrderDto;
import jpa_basic_shop.jpa_basic_shop.repository.OrderRepository;
import jpa_basic_shop.jpa_basic_shop.repository.OrderSearch;

import jpa_basic_shop.jpa_basic_shop.repository.order.query.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @XToOne
 * order 조회
 * order -> member (ManyToOne)
 * order -> delivery (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        // ORDER 2개
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // N + 1 : (worst case) order(1) + member(2) + delivery(2), N = 4
        return orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());
    }

    /**
     * V3 : 엔티티 조회 후 엔티티를 DTO로 변환
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());
    }

    /**
     * V4 : DTO로 바로 조회
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDto();
    }
}
