Feature: Profiles Management Integration Tests

  Background:
    * url 'http://localhost:8080/api/v1'
    # Autenticación como Empresario (User ID: 1)
    * def signInRequest = { email: 'empresario.core@mail.com', password: 'Password123!' }
    Given path '/authentication', 'sign-in'
    And request signInRequest
    When method post
    Then status 200
    * def authToken = response.token

  Scenario: Get Businessman Profile by User ID
    Given path '/profiles/businessmen/user/1'
    And header Authorization = 'Bearer ' + authToken
    When method get
    Then status 200
    And match response.companyName == 'Textil Core S.A.C.'
    And match response.ruc == '20123456789'

  Scenario: Get Supplier Profile by User ID
    Given path '/profiles/suppliers/user/2'
    And header Authorization = 'Bearer ' + authToken
    When method get
    Then status 200
    And match response.companyName == 'Fibras del Sur S.A.'
    And match response.specialization == 'COTTON'

  Scenario: Update Businessman Profile
    * def updatePayload =
      """
      {
        "companyName": "Textil Core Editado S.A.C.",
        "ruc": "20123456789",
        "businessType": "RETAIL",
        "description": "Nueva descripción de integración",
        "website": "https://textilcore-editado.com"
      }
      """
    Given path '/profiles/businessmen/1'
    And header Authorization = 'Bearer ' + authToken
    And request updatePayload
    When method put
    Then status 200
    And match response.companyName == 'Textil Core Editado S.A.C.'