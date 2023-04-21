package com.modernbankas.payment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_account")
public class AccountEntity {
    @Id
    @SequenceGenerator(name = "mySeqGen", sequenceName = "mySeq", initialValue = 98723450, allocationSize = 100)
    @GeneratedValue(generator = "mySeqGen")
    private Long accountNumber;
    @Column
    private String accountType;
    @Column
    private Double balance;

    @Column
    private String currency;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private CustomerEntity customer;

//    @OneToMany(mappedBy = "post",
//            cascade = CascadeType.ALL)
//    private Set<CustomerTransactionEntity> customerTransactionEntities;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

//    public Set<CustomerTransactionEntity> getCustomerTransactionEntities() {
//        return customerTransactionEntities;
//    }
//
//    public void setCustomerTransactionEntities(Set<CustomerTransactionEntity> customerTransactionEntities) {
//        this.customerTransactionEntities = customerTransactionEntities;
//    }
}
