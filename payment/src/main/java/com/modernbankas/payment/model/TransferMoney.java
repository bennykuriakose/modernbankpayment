package com.modernbankas.payment.model;


public record TransferMoney( Long senderId, Long recieverId, Double amount) {
}
