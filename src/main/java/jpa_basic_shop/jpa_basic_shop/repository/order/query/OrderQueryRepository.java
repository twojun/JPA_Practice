package jpa_basic_shop.jpa_basic_shop.repository.order.query;

import jakarta.persistence.EntityManager;
import jpa_basic_shop.jpa_basic_shop.dto.OrderFlatDto;
import jpa_basic_shop.jpa_basic_shop.dto.OrderItemQueryDto;
import jpa_basic_shop.jpa_basic_shop.dto.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDto() {
        List<OrderQueryDto> result = findOrders();

        result.forEach(order -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(order.getOrderId());
            order.setOrderItems(orderItems);
        });

        return result;
    }

    public List<OrderQueryDto> isOptimizeFindAllByDto() {
        List<OrderQueryDto> result = findOrders();   /** Order -> Member, Order -> Delivery 조회 */
        List<Long> orderIds = toOrderIds(result);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);  /** OrderItem join Item on oi_item_id = i_item_id */

        result.forEach(order -> order.setOrderItems(orderItemMap.get(order.getOrderId())));    /** 각각의 OrderItem 루프를 돌며 orderId를 가져온다. */
        return result;
    }

    public List<OrderFlatDto> findAllByDtoWithFlat() {
        return em.createQuery(
                        "select new jpa_basic_shop.jpa_basic_shop.dto.OrderFlatDto(" +
                                "o.id, " +
                                "m.name, " +
                                "o.orderDate," +
                                " o.status, " +
                                "d.address, " +
                                "i.name, " +
                                "oi.orderPrice, " +
                                "oi.count)" +
                                "from Order o " +
                                "join o.member m " +
                                "join o.delivery d " +
                                "join o.orderItems oi " +
                                "join oi.item i", OrderFlatDto.class)
                .getResultList();
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpa_basic_shop.jpa_basic_shop.dto.OrderItemQueryDto(" +
                                "oi.order.id, " +
                                "i.name, " +
                                "oi.orderPrice, " +
                                "oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }


    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream()
                .map(order -> order.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }


    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpa_basic_shop.jpa_basic_shop.dto.OrderItemQueryDto(" +
                                "oi.order.id, " +
                                "i.name, " +
                                "oi.orderPrice, " +
                                "oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }


    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpa_basic_shop.jpa_basic_shop.dto.OrderQueryDto(" +
                                "o.id, " +
                                "m.name, " +
                                "o.orderDate, " +
                                "o.status, " +
                                "d.address) " +
                                "from Order o " +
                                "join o.member m " +
                                "join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }


}
