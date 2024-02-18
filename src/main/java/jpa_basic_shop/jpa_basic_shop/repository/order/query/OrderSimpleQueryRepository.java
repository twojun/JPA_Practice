package jpa_basic_shop.jpa_basic_shop.repository.order.query;

import jakarta.persistence.EntityManager;
import jpa_basic_shop.jpa_basic_shop.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDto() {
        return em.createQuery(
                "select new jpa_basic_shop.jpa_basic_shop.dto.OrderSimpleQueryDto(" +
                        "o.id, " +
                        "m.name, " +
                        "o.orderDate, " +
                        "o.status, " +
                        "d.address) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
