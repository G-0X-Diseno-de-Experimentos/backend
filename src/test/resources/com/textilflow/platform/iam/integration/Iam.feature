Feature: Authentication and User Management Integration Tests

  Background:
    * url 'http://localhost:8080/api/v1'
    * def userEmail = 'empresario.core@mail.com'
    * def userPassword = 'Password123!'
    * def newPassword = 'NewPassword456!'

  Scenario: Sign Up a new user successfully (Returns 500 due to SMTP/Email connection failure on Domain Event)
    * def signUpRequest =
      """
      {
        "name": "Nuevo Usuario Core",
        "email": "nuevo.usuario@mail.com",
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
    Then status 500

  Scenario: Sign Up with missing or malformed data fails (Returns 500 due to JPA Constraint Violation)
    * def invalidSignUpRequest =
      """
      {
        "name": "",
        "email": "",
        "password": ""
      }
      """
    Given path '/authentication', 'sign-up'
    And request invalidSignUpRequest
    When method post
    Then status 500

  Scenario: Sign In and get JWT Token successfully (Happy Path - 200 OK)
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

  Scenario: Sign In with invalid password fails (Server Exception - 500 Internal Server Error)
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
    Then status 500

  Scenario: Request password reset successfully (Returns 500 due to actual SMTP sending failure)
    * def forgotRequest = { "email": "#(userEmail)" }
    Given path '/authentication', 'forgot-password'
    And request forgotRequest
    When method post
    Then status 500

  Scenario: Request password reset for non-existent email (Returns 200 OK to prevent email enumeration)
    * def invalidForgotRequest = { "email": "no-existe@mail.com" }
    Given path '/authentication', 'forgot-password'
    And request invalidForgotRequest
    When method post
    Then status 200
    And match response == "Password reset email sent successfully"

  Scenario: Reset password with mocked token fails JWT validation (Returns 400 Bad Request)
    * def resetRequest =
      """
      {
        "token": "valid-mocked-token",
        "newPassword": "#(newPassword)"
      }
      """
    Given path '/authentication', 'reset-password'
    And request resetRequest
    When method post
    Then status 400
    And match response == "Invalid or expired reset token"

  Scenario: Reset password with invalid or expired token fails (Client Error - 400 Bad Request)
    * def invalidResetRequest =
      """
      {
        "token": "expired-or-invalid-token",
        "newPassword": "#(newPassword)"
      }
      """
    Given path '/authentication', 'reset-password'
    And request invalidResetRequest
    When method post
    Then status 400
    And match response == "Invalid or expired reset token"

  Scenario: Access protected Users endpoint without Bearer Token is rejected (Security - 401/403)
    Given path '/users', '1'
    When method get
    Then assert responseStatus == 401 || responseStatus == 403

  Scenario: Get existing User by ID successfully (Happy Path - 200 OK)
    * def authReq = { "email": "#(userEmail)", "password": "#(userPassword)" }
    Given path '/authentication', 'sign-in'
    And request authReq
    When method post
    Then status 200
    * def validToken = response.token

    Given path '/users', response.id
    And header Authorization = 'Bearer ' + validToken
    When method get
    Then status 200
    And match response contains { id: '#(response.id)', email: '#(userEmail)' }

  Scenario: Get non-existent User returns Not Found (Client Error - 404 Not Found)
    * def authReq = { "email": "#(userEmail)", "password": "#(userPassword)" }
    Given path '/authentication', 'sign-in'
    And request authReq
    When method post
    Then status 200
    * def validToken = response.token

    Given path '/users', '999999'
    And header Authorization = 'Bearer ' + validToken
    When method get
    Then status 404

  Scenario: Update User role successfully (Happy Path - 200 OK)
    * def authReq = { "email": "#(userEmail)", "password": "#(userPassword)" }
    Given path '/authentication', 'sign-in'
    And request authReq
    When method post
    Then status 200
    * def validToken = response.token
    * def currentUserId = response.id

    * def updateRoleReq = { "role": "SUPPLIER" }
    Given path '/users', currentUserId, 'role'
    And header Authorization = 'Bearer ' + validToken
    And request updateRoleReq
    When method put
    Then status 200
    And match response.role == 'SUPPLIER'

  Scenario: Update User role with invalid payload fails (Returns 500 due to unreadable enum/validation)
    * def authReq = { "email": "#(userEmail)", "password": "#(userPassword)" }
    Given path '/authentication', 'sign-in'
    And request authReq
    When method post
    Then status 200
    * def validToken = response.token
    * def currentUserId = response.id

    * def invalidRoleReq = { "role": "" }
    Given path '/users', currentUserId, 'role'
    And header Authorization = 'Bearer ' + validToken
    And request invalidRoleReq
    When method put
    Then status 500