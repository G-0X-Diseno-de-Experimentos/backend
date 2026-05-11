Feature: Business Supplier Request Management

  Background:
    * url 'http://localhost:8080/api/v1'
    * def signInRequest = { email: "empresario.core@mail.com", password: "Password123!" }
    Given path '/authentication', 'sign-in'
    And request signInRequest
    When method post
    Then status 200
    * def authToken = response.token

  Scenario: Create a New Request between Businessman and Supplier
    * def createRequestPayload =
      """
      {
        "businessmanId": 1,
        "supplierId": 2,
        "message": "Solicitud de integracion core",
        "batchType": "COTTON",
        "color": "RED",
        "quantity": 300,
        "address": "Av. Las Palmeras 123"
      }
      """
    Given path '/business-supplier-requests'
    And header Authorization = 'Bearer ' + authToken
    And request createRequestPayload
    When method post
    Then status 201
      # Retorna el ID de la solicitud creada
    And match response == '#number'
    * def requestId = response
    * print 'Created Request ID:', requestId

  Scenario: Get All Requests
    Given path '/business-supplier-requests'
    And header Authorization = 'Bearer ' + authToken
    When method get
    Then status 200
    And match response == '#array'