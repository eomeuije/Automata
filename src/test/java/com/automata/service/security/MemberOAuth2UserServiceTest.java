package com.automata.service.security;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableScheduling
class MemberOAuth2UserServiceTest {

    private final Map<Date, HttpSession> sessionTimestamp = new LinkedHashMap<>();

    @Scheduled(fixedRate = 1000)
    public void remove() {
        System.out.println("MemberOAuth2UserServiceTest.remove");
        for (Date sessionTimestamp : sessionTimestamp.keySet()) {
            System.out.println("Date: " + sessionTimestamp);
        }
    }

    public void input() throws InterruptedException {
        for (int i=0; i < 12; i++) {
            sessionTimestamp.put(new Date(), null);
            Thread.sleep(100);
            System.out.println("i = " + i);
        }
    }

    @Test
    public void concurrentTest() throws InterruptedException {
        input();
    }
}