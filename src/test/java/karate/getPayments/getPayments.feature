Feature: Test get APIs

  Background:
    * url baseUrl
    * def transferPayment = '/transferPayment/'
    * def getLastTenPayments = '/getLastTenPayments/'

  Scenario: Create multiple Payments

    Given path transferPayment
    And request { fromAccountNo: 202202, toAccountNo: 202203, amount: 500.00 }
    And header Accept = 'application/json'
    When method post
    Then status 201

    And request { fromAccountNo: 202201, toAccountNo: 202202, amount: 500.00 }
    And header Accept = 'application/json'
    Then method post

    And request { fromAccountNo: 202204, toAccountNo: 202205, amount: 500.00 }
    And header Accept = 'application/json'
    Then method post

    And request { fromAccountNo: 202206, toAccountNo: 202205, amount: 500.00 }
    And header Accept = 'application/json'
    Then method post

    And request { fromAccountNo: 202202, toAccountNo: 202204, amount: 500.00 }
    And header Accept = 'application/json'
    Then method post

    And request { fromAccountNo: 202205, toAccountNo: 202202, amount: 500.00 }
    And header Accept = 'application/json'
    Then method post

    And request { fromAccountNo: 202201, toAccountNo: 202202, amount: 500.00 }
    And header Accept = 'application/json'
    Then method post

    And request { fromAccountNo: 202204, toAccountNo: 202203, amount: 500.00 }
    And header Accept = 'application/json'
    Then method post

    Given path getLastTenPayments + '202202'
    When method GET
    Then status 200
    And match response == 5
    Then delete account