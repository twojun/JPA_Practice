package jpa_basic_shop.jpa_basic_shop.api;

import jpa_basic_shop.jpa_basic_shop.domain.Order;
import jpa_basic_shop.jpa_basic_shop.domain.OrderItem;
import jpa_basic_shop.jpa_basic_shop.dto.OrderDto;
import jpa_basic_shop.jpa_basic_shop.dto.OrderFlatDto;
import jpa_basic_shop.jpa_basic_shop.dto.OrderItemQueryDto;
import jpa_basic_shop.jpa_basic_shop.dto.OrderQueryDto;
import jpa_basic_shop.jpa_basic_shop.repository.OrderRepository;
import jpa_basic_shop.jpa_basic_shop.repository.OrderSearch;
import jpa_basic_shop.jpa_basic_shop.repository.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /**
     * order -> order_item(OneToMany)
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
                    .forEach(orderElement -> orderElement.getItem().getName());
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
                .collect(toList());
    }

    /**
     * V3 : Entity -> DTO : LAZY 최적화를 위한 FETCH JOIN
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findWithItem();

        return orders.stream()
                .map(order -> new OrderDto(order))
                .collect(toList());
    }

    /**
     * V3.1 : Entity -> DTO : fetch join, paging(Batch size 사용)
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3Page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                      @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        return orders.stream()
                .map(order -> new OrderDto(order))
                .collect(Collectors.toList());
    }

    /**
     * V4 : JPA에서 DTO로 바로 조회(Collection)
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDto();
    }

    /**
     * V5 : JPA에서 DTO로 바로 조회(Collection) - N+1 문제 최적화
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.isOptimizeFindAllByDto();
    }


    /**
     * V6 : JPA에서 DTO로 바로 조회(Collection) - 플랫 데이터 최적화
     */
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDtoWithFlat();

        return flats.stream()
                .collect(Collectors.groupingBy(o -> new OrderQueryDto(
                                o.getOrderId(),
                                o.getName(),
                                o.getOrderDate(),
                                o.getOrderStatus(),
                                o.getAddress()),
                        Collectors.mapping(o -> new OrderItemQueryDto(
                                        o.getOrderId(),
                                        o.getItemName(),
                                        o.getOrderPrice(),
                                        o.getCount()),
                                Collectors.toList())))
                .entrySet().stream()
                .map(e -> new OrderQueryDto(
                        e.getKey().getOrderId(),
                        e.getKey().getName(),
                        e.getKey().getOrderDate(),
                        e.getKey().getOrderStatus(),
                        e.getKey().getAddress(),
                        e.getValue()))
                .collect(toList());
    }
}


