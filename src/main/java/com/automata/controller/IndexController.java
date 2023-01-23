package com.automata.controller;

import com.automata.domain.member.Member;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;

@Controller
public class IndexController {

    @GetMapping({"/", "/home"})
    public String index(Model model) {
        return "home";
    }

    @GetMapping("/index")
    public String i(Model model) {
        return "index";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = (Member) principal;
        model.addAttribute("name", member.getName());
        return "hello";
    }
}
