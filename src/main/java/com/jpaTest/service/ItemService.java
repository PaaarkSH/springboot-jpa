package com.jpaTest.service;

import com.jpaTest.domain.item.Book;
import com.jpaTest.domain.item.Item;
import com.jpaTest.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional  // 이부분은 readOnly 가 아님
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {  //  UpdateItemDto itemDto
        Item findItem = itemRepository.findOne(itemId);  // itemRepository 에서 id 를 기반으로 영속성 컨텍스트인 item 객체를 찾아옴
        //findItem.change(name, price, stockQuantity);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        // itemRepository 의 save 를 호출할 필요가 없음
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
}
