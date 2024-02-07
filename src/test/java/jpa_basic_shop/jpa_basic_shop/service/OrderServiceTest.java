package jpa_basic_shop.jpa_basic_shop.service;

import jakarta.persistence.EntityManager;
import jpa_basic_shop.jpa_basic_shop.domain.Address;
import jpa_basic_shop.jpa_basic_shop.domain.Member;
import jpa_basic_shop.jpa_basic_shop.domain.Order;
import jpa_basic_shop.jpa_basic_shop.domain.OrderStatus;
import jpa_basic_shop.jpa_basic_shop.domain.item.Book;
import jpa_basic_shop.jpa_basic_shop.exception.NotEnoughStockException;
import jpa_basic_shop.jpa_basic_shop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품_주문() throws Exception {
        // Given
        Member member = createMember();
        Book book = createBook("자바의 정석", 10000, 10);

        int orderCount = 2;

        // When
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // Then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문 시 주문 상태는 ORDER");
        Assertions.assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 수가 정확해야 한다.");
        Assertions.assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문금액은 상품 가격 * 주문 수량이다");
        Assertions.assertEquals(8, book.getStockQuantity(), "주문 개수만큼 수량은 감소해야 한다.");
    }

    @Test
    public void 주문_취소() throws Exception {
        // Given
        Member member = createMember();
        Book book = createBook("자바의 정석", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // When
        orderService.cancelOrder(orderId);

        // Then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소 시 주문 상태는 CANCEL");
        Assertions.assertEquals(10, book.getStockQuantity(), "주문 취소 후 수량은 증가한다.");

    }

    @Test
    public void 상품_주문_시_재고수량_초과() throws Exception {
        // Given
        Member member = createMember();
        Book book = createBook("자바의 정석", 10000, 10);

        int orderCount = 20;

        // When

        // Then
        Assertions.assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount));
        Assertions.assertEquals(10, book.getStockQuantity(), "재고수량은 변하지 않는다.");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("인천", "부평", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

}