package com.sapient.PSBank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "transaction_table")

public class Transaction {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Setter
    @Column
    private String dateAndTime;
    @Setter
    @Column
    private String type;
    @Setter
    @Column
    private double amt;
    @Setter
    @Column
    private int years;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    public Transaction(String dateAndTime, String type, double amt,int years,Customer customer) {
        this.dateAndTime = dateAndTime;
        this.type = type;
        this.amt = amt;
        this.years=years;
        this.customer=customer;
    }
}
