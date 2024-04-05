package com.automata.domain;

import jakarta.persistence.*;

import java.time.format.DateTimeFormatter;

@Entity
public class Item {

    private static final DateTimeFormatter ID_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");

    @Id
    @SequenceGenerator(name = "itemSeqGen", sequenceName = "itemSeq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemSeqGen")
    private Long id;
    private String name;

    @ManyToOne
    private Member owner;
    private String detail;
    private int price;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
