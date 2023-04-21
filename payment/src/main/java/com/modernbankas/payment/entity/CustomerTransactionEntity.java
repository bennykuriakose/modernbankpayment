package com.modernbankas.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_customer_transaction")
public class CustomerTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String transactionType;

    @Column
    private Double amount;

    @Column
    private String currency;

    @Column
    private LocalDateTime transactionTimeStamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_no", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AccountEntity account;

    public Long getid() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getTransactionTimeStamp() {
        return transactionTimeStamp;
    }

    public void setTransactionTimeStamp(LocalDateTime transactionTimeStamp) {
        this.transactionTimeStamp = transactionTimeStamp;
    }

    public AccountEntity getAccountEntity() {
        return account;
    }

    public void setAccountEntity(AccountEntity accountEntity) {
        this.account = accountEntity;
    }
}
