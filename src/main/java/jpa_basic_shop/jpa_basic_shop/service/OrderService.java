package jpa_basic_shop.jpa_basic_shop.service;

import jpa_basic_shop.jpa_basic_shop.domain.Delivery;
import jpa_basic_shop.jpa_basic_shop.domain.Member;
import jpa_basic_shop.jpa_basic_shop.domain.Order;
import jpa_basic_shop.jpa_basic_shop.domain.OrderItem;
import jpa_basic_shop.jpa_basic_shop.domain.item.Item;
import jpa_basic_shop.jpa_basic_shop.repository.ItemRepository;
import jpa_basic_shop.jpa_basic_shop.repository.MemberRepository;
import jpa_basic_shop.jpa_basic_shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문 진행
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 주문자, 관련 상품 (엔티티) 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문상품 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();
    }

    /**
     * 주문 검색
     */
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
