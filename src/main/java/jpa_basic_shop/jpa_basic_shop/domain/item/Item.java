package jpa_basic_shop.jpa_basic_shop.domain.item;

import jakarta.persistence.*;
import jpa_basic_shop.jpa_basic_shop.domain.Category;
import jpa_basic_shop.jpa_basic_shop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /**
     * 도메인 주도 설계 : 도메인에 대한 직접적인 필드를 가지고 있는 곳(엔티티)에서
     * 비즈니스 로직을 설계하는 것이 응집도가 높은 설계 방법
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;  // 재고 수량 증가
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            throw new NotEnoughStockException("재고수량은 0 미만으로 감소할 수 없습니다.");
        }

        this.stockQuantity = restStock;
    }
}
