**modernbankpayment**

Internal payment system for organization

**Project Description**

This is an internal payment system that can be used for handling internal payments between users.

**Included API**
As of now, there are 4 APIs.

1) Create an account (not specified in the requirement ) added to create the account and customer into the system 
url:/api/accounts/create
2) get balance: Api can be used to fetch the balance of a particular account number. Have to pass the account number as part of the request 
url:/api/accounts/{accountNumber}/balance
3) get mini statement: Api to return account transaction history for a provided account number. Have to pass the account number as part of the request
url:/api/accounts/{accountnumber}/statements/mini
4) Transfer money: Api to transfer money between two accounts. Have to provide the request body in the below format 
url:/api/accounts/transfer
 body:
 {
  "senderId":"",
  "recieverId":"",
  "amount":""
}

API will return a payment exception if an invalid account number is provided or failed validations,More exceptions need to be added for more scenarios


Note: As of now only support one currency **NOK**.


**Requirements**
For building and running the application you need:


JDK 17
Maven
**Running the application locally**

There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the Application class from your IDE.

Alternatively, you can use the Spring Boot Maven plugin:

mvn spring-boot:run

Swagger API documentation can be added.
