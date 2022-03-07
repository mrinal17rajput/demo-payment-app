Feature: Create Accounts and Payments

  Background:
    * url baseUrl
    * def transferPayment = '/transferPayment/'

  Scenario: Create a Payment
    Given path transferPayment
    And request { fromAccountNo: 202202, toAccountNo: 202203, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 201
    And match response.split(":")[0] == 'new Payment created with the reference number'

  Scenario: Create a Payment with wrong from account number

    Given path transferPayment
    And request { fromAccountNo: 202205, toAccountNo: 202203, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 400
    And match response.message == 'invalid from account'

  Scenario: Create a Payment with invalid to account number

    Given path transferPayment
    And request { fromAccountNo: 202202, toAccountNo: 202206, amount: 500.58 }
    And header Accept = 'application/json'
    When method post
    Then status 400
    And match response.message == 'invalid to account'

  Scenario: Create a Payment with insufficient balance

    Given path transferPayment
    And request { fromAccountNo: 202202, toAccountNo: 202203, amount: 50000 }
    And header Accept = 'application/json'
    When method post
    Then status 409
    And match response.message == 'insufficient funds'

