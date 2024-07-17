package com.jpaTest.repository;

import com.jpaTest.domain.item.Item;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository  {
    private final EntityManager em;
    public void save(Item item) {
        if (item.getId() == null){  // 새로 생성하는 단계에서는 id 값이 없음
            em.persist(item);
        } else {
            em.merge(item);  // update 와 비슷하지만 다름
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
