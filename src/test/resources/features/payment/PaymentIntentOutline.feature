Feature: Payment Intent Validation
  As a system
  I want to validate payment intents
  So that only valid payments are processed

  Scenario Outline: Validate payment amounts
    Given a user with id 1 wants to create a payment intent
    And the payment amount is <amount> in currency "<currency>"
    When the user creates the payment intent
    Then the result should be <result>

    Examples:
      | amount | currency | result  |
      | 100.0  | usd      | success |
      | 50.0   | eur      | success |
      | -10.0  | usd      | error   |
      | 0.0    | usd      | error   |