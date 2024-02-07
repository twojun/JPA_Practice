package jpa_basic_shop.jpa_basic_shop.domain;

import jakarta.persistence.*;
import jpa_basic_shop.jpa_basic_shop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    /**
     * 핵심 비즈니스 : 주문 시, 주문 아이템에 대한 정보 생성 (생성 메서드)
     */
    public static OrderItem createOrderItem(Item item, int price, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(price);
        orderItem.setCount(count);

        item.removeStock(count);  // 주문 수만큼 재고 감소
        return orderItem;
    }

    /**
     * 핵심 비즈니스 : 주문 취소 시, 재고 수량 원복
     */
    public void cancel() {
        getItem().addStock(count);
    }


    /**
     * 핵심 비즈니스 : 주문 상품 전체 가격 계산
     */
    public int getTotalPrice() {
        return getCount() * getOrderPrice();
    }
}
