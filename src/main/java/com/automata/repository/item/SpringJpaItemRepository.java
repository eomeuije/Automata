package com.automata.repository.item;

import com.automata.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaItemRepository extends JpaRepository<Item, String>, ItemRepository {


}
