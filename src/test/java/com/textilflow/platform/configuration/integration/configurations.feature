Feature: User Configuration Integration Tests

  Background:
    * url 'http://localhost:8080/api/v1'
    * def signInRequest = { email: 'empresario.core@mail.com', password: 'Password123!' }
    Given path '/authentication', 'sign-in'
    And request signInRequest
    When method post
    Then status 200
    * def authToken = response.token

  Scenario: Get Configuration by User ID
    Given path '/configurations/user/1'
    And header Authorization = 'Bearer ' + authToken
    When method get
    Then status 200
    And match response.language == 'ES'
    And match response.subscriptionPlan == 'PREMIUM'

  Scenario: Update Preferences
    * def updateConfig =
      """
      {
        "language": "EN",
        "viewMode": "DARK",
        "subscriptionPlan": "PREMIUM",
        "subscriptionStatus": "ACTIVE"
      }
      """
    Given path '/configurations/1'
    And header Authorization = 'Bearer ' + authToken
    And request updateConfig
    When method put
    Then status 200
    And match response.viewMode == 'DARK'
    And match response.language == 'EN'