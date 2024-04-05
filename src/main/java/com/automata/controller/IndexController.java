package com.automata.controller;

import com.automata.domain.Member;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    public IndexController() {
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/hello")
    public String hello(Model model, @AuthenticationPrincipal Member member) {
        model.addAttribute("name", member.getName());
        model.addAttribute("nickName", member.getNickName());
        return "hello";
    }
}
