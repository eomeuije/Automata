package com.automata.controller;

import com.automata.domain.member.Member;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

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
    public String hello(Model model, @AuthenticationPrincipal Member member, HttpServletRequest request, HttpServletRequest sr) {
        model.addAttribute("name", member.getName());
        model.addAttribute("nickName", member.getNickName());
        return "hello";
    }
}
