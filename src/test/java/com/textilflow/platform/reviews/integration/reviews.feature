Feature: Supplier Reviews Integration Tests

  Background:
    * url 'http://localhost:8080/api/v1'
    * def signInRequest = { email: 'empresario.core@mail.com', password: 'Password123!' }
    Given path '/authentication', 'sign-in'
    And request signInRequest
    When method post
    Then status 200
    * def authToken = response.token

  Scenario: Get All Reviews for a Supplier
    Given path '/supplier-reviews/supplier/2'
    And header Authorization = 'Bearer ' + authToken
    When method get
    Then status 200
    And match response == '#array'
    And match response[0].rating == 5
    And match response[0].reviewContent contains 'Excelente calidad'

  Scenario: Create a New Review fails if already reviewed
    # El usuario 1 ya tiene una reseña para el proveedor 2 en los datos semilla
    * def reviewPayload = { supplierId: 2, businessmanId: 1, rating: 4, reviewContent: 'Intento duplicado' }
    Given path '/supplier-reviews'
    And header Authorization = 'Bearer ' + authToken
    And request reviewPayload
    When method post
    Then status 400