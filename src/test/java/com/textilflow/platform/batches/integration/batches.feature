Feature: Batch Management Core Flow

  Background:
    * url 'http://localhost:8080/api/v1'
    # Autenticación previa para obtener el token
    * def signInRequest = { email: "empresario.core@mail.com", password: "Password123!" }
    Given path '/authentication', 'sign-in'
    And request signInRequest
    When method post
    Then status 200
    * def authToken = response.token
    * def batchCode = 'LOTE-CORE-001'

  Scenario: Create a New Batch
    * def batchRequest =
      """
      {
        "code": "#(batchCode)",
        "client": "Cliente Core Integration",
        "businessmanId": 1,
        "supplierId": 2,
        "fabricType": "ALGODON",
        "color": "AZUL",
        "price": 1500.50,
        "quantity": 500,
        "observations": "Lote de prueba de integracion",
        "address": "Almacen Central",
        "date": "2026-05-20",
        "status": "PENDIENTE",
        "imageUrl": "http://imagen.url/lote.png"
      }
      """
    Given path '/batches'
    And header Authorization = 'Bearer ' + authToken
    And request batchRequest
    When method post
    Then status 201
    And match response contains { code: '#(batchCode)', client: 'Cliente Core Integration' }
    * def createdBatchId = response.id
    * print 'Created Batch ID:', createdBatchId

  Scenario: Get Batch by ID
    # Asumiendo que el ID 1 existe o pasamos una variable guardada
    Given path '/batches', 1
    And header Authorization = 'Bearer ' + authToken
    When method get
    Then status 200
    And match response.id == 1

  Scenario: Get Batch with invalid ID returns error
    Given path '/batches', 999999
    And header Authorization = 'Bearer ' + authToken
    When method get
    Then status 400