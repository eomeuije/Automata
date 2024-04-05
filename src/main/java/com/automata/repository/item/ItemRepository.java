package com.automata.repository.item;

import com.automata.domain.Item;
import com.automata.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    List<Item> findAll();

    List<Item> findByOwner(Member member);

    List<Item> findByName(String itemName);

    Optional<Item> findById(Long id);
}
