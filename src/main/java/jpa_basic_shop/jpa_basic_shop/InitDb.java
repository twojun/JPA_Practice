package jpa_basic_shop.jpa_basic_shop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpa_basic_shop.jpa_basic_shop.domain.*;
import jpa_basic_shop.jpa_basic_shop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 테스트 데이터 생성
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;
    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member memberA = createMember("userA", "인천", "1", "111");
            em.persist(memberA);

            Book book1 = createBook("JPA", 10000, 100);
            em.persist(book1);
            Book book2 = createBook("JPA2", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(memberA);
            Order order = Order.createOrder(memberA, delivery, orderItem1, orderItem2);
            em.persist(order);
        }


        public void dbInit2() {
            Member memberB = createMember("userB", "서울", "2", "222");
            em.persist(memberB);

            Book book1 = createBook("Spring1 Book", 20000, 200);
            em.persist(book1);
            Book book2 = createBook("Spring2 Book", 40000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = createDelivery(memberB);
            Order order = Order.createOrder(memberB, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private static Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private static Delivery createDelivery(Member memberA) {
            Delivery delivery = new Delivery();
            delivery.setAddress(memberA.getAddress());
            return delivery;
        }
    }

}

