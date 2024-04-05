package com.automata.controller.item;

import com.automata.domain.Item;
import com.automata.domain.Member;
import com.automata.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("new")
    public String newItemGet() {
        return "/item/new";
    }

    @PostMapping("new")
    public String newItemPost(Authentication authentication, Item item) {
        itemService.save((Member) authentication.getPrincipal(), item);
        return "redirect:/item/my-items";
    }

    @GetMapping("my-items")
    public String getMyItems() {
        return "/item/myItems";
    }

    @GetMapping("s")
    public String getItems(@RequestParam(required = false) boolean isMine, Model model, Authentication authentication) {
        List<Item> items;
        if (isMine) {
            items = itemService.findItemsByMember((Member) authentication.getPrincipal());
        } else {
            items = itemService.findItems();
        }
        model.addAttribute("items", items);
        return "/item/s";
    }

    @GetMapping("detail/{id}")
    public String getItemDetail(@PathVariable Long id, Model model) {
        Item item = itemService.findById(id);
        model.addAttribute("item", item);
        return "/item/detail";
    }
}
