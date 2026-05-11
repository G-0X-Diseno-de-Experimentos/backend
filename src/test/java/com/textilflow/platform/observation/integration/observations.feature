Feature: Observations Management Integration Tests

  Background:
    * url 'http://localhost:8080/api/v1'
    * def signInRequest = { email: 'empresario.core@mail.com', password: 'Password123!' }
    Given path '/authentication', 'sign-in'
    And request signInRequest
    When method post
    Then status 200
    * def authToken = response.token

  Scenario: Create a New Observation for a Batch
    * def newObservation =
      """
      {
        "batchId": 1,
        "batchCode": "LOTE-SEED-001",
        "businessmanId": 1,
        "supplierId": 2,
        "reason": "Mancha detectada en el rollo 3",
        "imageUrl": "https://img.url/obs1.png",
        "status": "PENDIENTE"
      }
      """
    Given path '/observations'
    And header Authorization = 'Bearer ' + authToken
    And request newObservation
    When method post
    Then status 201
    And match response.reason == 'Mancha detectada en el rollo 3'
    * def createdObsId = response.id

  Scenario: Get Observations by Batch ID
    Given path '/observations/batch/1'
    And header Authorization = 'Bearer ' + authToken
    When method get
    Then status 200
    And match response == '#array'