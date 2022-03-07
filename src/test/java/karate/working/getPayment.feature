Feature: Test get APIs

  Background:
    * url baseUrl
    * def transferPayment = '/payment/transferPayment/'
    * def getLastTenPayments = '/payment/getLastTenPayments/'
    * def createAccount = '/createAccount/'
    * def findAllAccounts = '/findAll'

  Scenario: Create multiple Payments

#    * path findAllAccounts
#    When method GET
#    Then status 200
#    And match response == { firstName: 'John', lastName: 'Doe', age: 30 }
#
#    Given path createAccount
#    And request { accountNo: 202201, name: "cust1", balance: 10000.00 }
#    And header Accept = 'application/json'
#    When method post
#    Then status 200
#    And match response == { id: 1, name:, accountNo: 202201, name: "cust1", balance: 10000.00 }
#
#    Given path createAccount
#    And request { accountNo: 202202, name: "cust2", balance: 10000.00 }
#    And header Accept = 'application/json'
#    When method post
#    Then status 200
#
#    Given path createAccount
#    And request { accountNo: 202203, name: "cust3", balance: 10000.00 }
#    And header Accept = 'application/json'
#    When method post
#
#    Given path createAccount
#    And request { accountNo: 202204, name: "cust4", balance: 10000.00 }
#    And header Accept = 'application/json'
#    When method post

    Given path transferPayment
    And request { fromAccountNo: 202202, toAccountNo: 202203, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 201

    Given path transferPayment
    And request { fromAccountNo: 202201, toAccountNo: 202202, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 201

    Given path transferPayment
    And request { fromAccountNo: 202204, toAccountNo: 202205, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 400

    Given path transferPayment
    And request { fromAccountNo: 202206, toAccountNo: 202205, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 400

    Given path transferPayment
    And request { fromAccountNo: 202202, toAccountNo: 202204, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 201

    Given path transferPayment
    And request { fromAccountNo: 202205, toAccountNo: 202202, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 400

    Given path transferPayment
    And request { fromAccountNo: 202201, toAccountNo: 202202, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 201

    Given path transferPayment
    And request { fromAccountNo: 202204, toAccountNo: 202203, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 201

    Given path getLastTenPayments + '202202'
    When method GET
    Then status 200
    And match response.size() == 4
#
#    Given path '/deleteAllPayments'
#    When method DELETE
#    Then status 200

