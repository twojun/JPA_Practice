package jpa_basic_shop.jpa_basic_shop.service;

import jakarta.persistence.EntityManager;
import jpa_basic_shop.jpa_basic_shop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception {
        Book book = em.find(Book.class, 1L);

        // Transaction 내부에서
        book.setName("book2");

        // Transaction commit

    }
}
