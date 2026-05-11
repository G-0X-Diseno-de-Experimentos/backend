Feature: Supplier Reviews Management Integration Tests

  Background:
    * url 'http://localhost:8080/api/v1'
    * def businessmanEmail = 'empresario.core@mail.com'
    * def basePassword = 'Password123!'

    # Autenticación global y configuración de cabeceras Bearer
    * def authReq = { "email": "#(businessmanEmail)", "password": "#(basePassword)" }
    Given path '/authentication', 'sign-in'
    And request authReq
    When method post
    Then status 200
    * def token = response.token
    * def authHeader = 'Bearer ' + token
    * configure headers = { Authorization: '#(authHeader)' }

  Scenario: Create a new supplier review successfully (Happy Path - 201 Created)
    # Conectamos al empresario 1 dejando una reseña inicial para el proveedor 2
    * def createReviewPayload =
      """
      {
        "supplierId": 2,
        "businessmanId": 1,
        "rating": 5,
        "reviewContent": "Excelente calidad de algodón, la entrega fue realizada antes de la fecha límite pactada."
      }
      """
    Given path '/supplier-reviews'
    And request createReviewPayload
    When method post
    Then status 201
    And match response.supplierId == 2
    And match response.businessmanId == 1
    And match response.rating == 5
    And match response.reviewContent contains 'Excelente calidad'

  Scenario: Create a duplicate review from the same businessman throws service exception (Server Error - 500)
    # Al intentar registrar una segunda reseña para la misma dupla (supplierId 2 / businessmanId 1),
    # el servicio lanza explícitamente IllegalArgumentException. Al no existir try-catch en el controlador, arroja HTTP 500.
    * def duplicateReviewPayload =
      """
      {
        "supplierId": 2,
        "businessmanId": 1,
        "rating": 3,
        "reviewContent": "Intento de reseña duplicada para forzar el fallo programado en el servicio."
      }
      """
    Given path '/supplier-reviews'
    And request duplicateReviewPayload
    When method post
    Then status 500

  Scenario: Create review with invalid rating value is rejected by DTO bindings (Client Error - 400)
    # El constructor compacto de CreateSupplierReviewResource valida: if (rating < 1 || rating > 5) throw ...
    # Interceptado tempranamente por el framework en la conversión del RequestBody como 400 Bad Request.
    * def invalidRatingPayload =
      """
      {
        "supplierId": 2,
        "businessmanId": 1,
        "rating": 6,
        "reviewContent": "Comentario irrelevante"
      }
      """
    Given path '/supplier-reviews'
    And request invalidRatingPayload
    When method post
    Then status 400

  Scenario: Check if businessman reviewed supplier yields correct boolean state (Happy Path - 200 OK)
    # Consultamos si la dupla (supplierId 2 / businessmanId 1) preexiste
    Given path '/supplier-reviews', 'check', '2', '1'
    When method get
    Then status 200
    # Al haber persistido el registro en el primer escenario, retorna true
    And match response == true

  Scenario: Get reviews for an unreviewed supplier returns safe Not Found response (Client Error - 404)
    # Valida la directriz programada: if (reviews.isEmpty()) return ResponseEntity.notFound().build();
    Given path '/supplier-reviews', 'supplier', '999999'
    When method get
    Then status 404

  Scenario: Get existing reviews for a valid supplier returns a populated JSON array (Happy Path - 200 OK)
    Given path '/supplier-reviews', 'supplier', '2'
    When method get
    Then status 200
    And match response == '#[]'
    And match response[0].rating == 5

  Scenario: Update a non-existent review throws defensive service exception (Server Error - 500)
    # Al buscar el ID y no encontrarlo, el servicio lanza IllegalArgumentException ("Review with ID X not found")
    # antes de retornar Optional.empty(), impidiendo que actúe el if del controlador y arrojando HTTP 500.
    * def updateReviewPayload = { "rating": 4, "reviewContent": "Comentario modificado" }
    Given path '/supplier-reviews', '999999'
    And request updateReviewPayload
    When method put
    Then status 500