package com.modernbankas.payment.model;

public record TransactionHistoryResponse(Long accountNo,Double amount,String currency,String type,String transactiondate) {
}
