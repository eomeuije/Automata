package com.automata.service;

import com.automata.domain.Item;
import com.automata.domain.Member;
import com.automata.repository.item.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {


    private final ItemRepository itemRepository;

    public void save(Member member, Item item) {
        item.setOwner(member);
        itemRepository.save(item);
    }

    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found: " + id));
    }

    public List<Item> findByName(String itemName) {
        return itemRepository.findByName(itemName);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }
    public List<Item> findItemsByMember(Member member) {
        return itemRepository.findByOwner(member);
    }
}
