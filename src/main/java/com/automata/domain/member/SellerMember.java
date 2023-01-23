package com.automata.domain.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

//@Table(name = "seller_member")
public class SellerMember extends Member {

//    @Column(name = "irs_number")
    public String getIrsNumber() {
        return irsNumber;
    }

    public void setIrsNumber(String irsNumber) {
        this.irsNumber = irsNumber;
    }

    private String irsNumber;
}
