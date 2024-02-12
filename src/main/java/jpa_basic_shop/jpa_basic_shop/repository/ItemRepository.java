package jpa_basic_shop.jpa_basic_shop.repository;

import jakarta.persistence.EntityManager;
import jpa_basic_shop.jpa_basic_shop.domain.item.Book;
import jpa_basic_shop.jpa_basic_shop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
            .getResultList();
    }

    // isbn 조회
    public List<Book> findAllIsbn() {
        return em.createQuery("select b.isbn from Book b", Book.class)
                .getResultList();
    }
}
