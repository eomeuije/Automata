package com.automata.service;

import com.automata.domain.Item;
import com.automata.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Test
    public void itemSave() {
        Member member = new Member();
        member.setId(23232L);
        Item item = new Item();
        item.setName("bee");
        itemService.save(member, item);

        Item item2 = new Item();
        item2.setName("bee");
        itemService.save(member, item2);

        List<Item> found = itemService.findByName("bee");
        Assertions.assertThat(found)
                .extracting(Item::getName)
                .containsOnly("bee");
    }
}