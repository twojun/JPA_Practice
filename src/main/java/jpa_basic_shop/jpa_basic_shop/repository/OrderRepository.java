package jpa_basic_shop.jpa_basic_shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpa_basic_shop.jpa_basic_shop.domain.Order;
import jpa_basic_shop.jpa_basic_shop.domain.OrderStatus;
import jpa_basic_shop.jpa_basic_shop.domain.QMember;
import jpa_basic_shop.jpa_basic_shop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static jpa_basic_shop.jpa_basic_shop.domain.QMember.member;
import static jpa_basic_shop.jpa_basic_shop.domain.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * 주문 내역 검색("회원명", "주문 상태" 두 가지로 필터링)
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (orderSearch.getMemberName() != null) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    /** QueryDSL, 동적 검색 조건 */
    public List<Order> findAll(OrderSearch orderSearch) {
        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(
                        isEqualStatus(orderSearch.getOrderStatus()),
                        isLikeName(orderSearch.getMemberName()))
                .limit(100)
                .fetch();
    }

    private BooleanExpression isEqualStatus(OrderStatus statusCondition) {
        if (statusCondition == null) {
            return null;
        }
        return order.status.eq(statusCondition);
    }

    private BooleanExpression isLikeName(String memberName) {
        if (!StringUtils.hasText(memberName)) {
            return null;
        }
        return order.member.name.like(memberName);  // like : %abc, abc%  // contains : %abc%
    }


    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                        "select o " +
                                "from Order o " +
                                "join fetch o.member m " +
                                "join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o " +
                        "from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Order> findWithItem() {
        return em.createQuery(
                "select o " +
                        "from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d " +
                        "join fetch o.orderItems oi " +
                        "join fetch oi.item i", Order.class)
                .setFirstResult(0)
                .setMaxResults(200)
                .getResultList();
    }
}
