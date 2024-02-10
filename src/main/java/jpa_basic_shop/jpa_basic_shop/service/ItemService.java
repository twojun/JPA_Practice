package jpa_basic_shop.jpa_basic_shop.service;

import jpa_basic_shop.jpa_basic_shop.domain.item.Book;
import jpa_basic_shop.jpa_basic_shop.domain.item.Item;
import jpa_basic_shop.jpa_basic_shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, Book param) {
        Item findItem = itemRepository.findOne(itemId);  // 식별자를 기반으로 실제 영속 상태의 엔티티를 조회
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());

        // @Transactional 어노테이션에 의해 메서드 종료 시점 커밋이 발생
        // 필드 데이터의 변화를 확인하여 변경 감지 발생
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
}
