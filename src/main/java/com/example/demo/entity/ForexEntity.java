package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "forex")
public class ForexEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="USD/NTD")
    private BigDecimal usdToNtd;

    private String date;

    public ForexEntity() {
    }

    public void setUsdToNtd(String usd){
        this.usdToNtd = new BigDecimal(usd);
    }

    public void setDate(String date){
        this.date = date;
    }

}



