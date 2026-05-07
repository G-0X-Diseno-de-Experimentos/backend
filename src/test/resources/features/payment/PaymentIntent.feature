Feature: Payment Intent Management
  As a user
  I want to create payment intents
  So that I can process payments through the platform

  Scenario: Create a valid payment intent
    Given a user with id 1 wants to create a payment intent
    And the payment amount is 100.0 in currency "usd"
    When the user creates the payment intent
    Then the payment intent should be created successfully
    And the client secret should be returned

  Scenario: Create payment intent with invalid amount
    Given a user with id 1 wants to create a payment intent
    And the payment amount is -100.0 in currency "usd"
    When the user creates the payment intent
    Then an error should be thrown