package com.automata.controller;

import com.automata.domain.member.Member;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "home";
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/hello")
    public String hello(Model model, @AuthenticationPrincipal Member member) {
        model.addAttribute("name", member.getName());
        model.addAttribute("nickName", member.getNickName());
        return "hello";
    }
}
