Feature: Authentication and User Management

  Background:
    * url 'http://localhost:8080/api/v1'
    * def userEmail = 'empresario.core@mail.com'
    * def userPassword = 'Password123!'

  Scenario: Sign Up a new user successfully
    * def signUpRequest =
      """
      {
        "name": "Empresario Core",
        "email": "#(userEmail)",
        "password": "#(userPassword)",
        "country": "Peru",
        "city": "Lima",
        "address": "Av. Industrial 456",
        "phone": "987654321",
        "role": "BUSINESSMAN"
      }
      """
    Given path '/authentication', 'sign-up'
    And request signUpRequest
    When method post
    Then status 200
    And match response contains { id: '#number', email: '#(userEmail)' }

  Scenario: Sign In and get JWT Token
    * def signInRequest =
      """
      {
        "email": "#(userEmail)",
        "password": "#(userPassword)"
      }
      """
    Given path '/authentication', 'sign-in'
    And request signInRequest
    When method post
    Then status 200
    And match response contains { token: '#string', email: '#(userEmail)' }
    * def authToken = response.token
    * print 'Obtained Core Token:', authToken

  Scenario: Sign In with invalid password fails
    * def invalidSignInRequest =
      """
      {
        "email": "#(userEmail)",
        "password": "WrongPassword!"
      }
      """
    Given path '/authentication', 'sign-in'
    And request invalidSignInRequest
    When method post
    Then status 400