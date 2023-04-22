**modernbankpayment

Internal payment system for organisation

**Project Description

This is an internal payment system that can be used for handeling internal payments between users .
**Included API

1) Create account (not specified in the requirement ) added to create account and customer in to the system 
url:/api/accounts/create
2) get balance : Api can be used to fetch balance of a particular account number .Have to pass account number as part of the request 
url:/api/accounts/{accountNumber}/balance
3) get mini statement  : Api to return account transaction history for a provided account number .Have to pass account number as part of the request
url:/api/accounts/{accountnumber}/statements/mini
6) Transer money : Api to transfer money between two accounts .Have to provide request body in below format 
url :/api/accounts/transfer
 body:
 {
  "senderId":"",
  "recieverId":"",
  "amount":""
}
Api will return payment exception if invalid account number provided 
Note: As of now only support one currency **NOK** .


**Requirements**
For building and running the application you need:

JDK 17
Maven
**Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the de.codecentric.springbootsample.Application class from your IDE.

Alternatively you can use the Spring Boot Maven plugin:

mvn spring-boot:run
