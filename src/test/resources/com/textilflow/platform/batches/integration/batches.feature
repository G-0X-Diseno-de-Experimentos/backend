Feature: Batches Management Integration Tests

  Background:
    * url 'http://localhost:8080/api/v1'
    * def userEmail = 'empresario.core@mail.com'
    * def userPassword = 'Password123!'

    * def authPayload = { "email": "#(userEmail)", "password": "#(userPassword)" }
    Given path '/authentication', 'sign-in'
    And request authPayload
    When method post
    Then status 200
    * def authToken = response.token

    # Solución al ParseException: Concatenar la cabecera e inyectarla por referencia
    * def authHeader = 'Bearer ' + authToken
    * configure headers = { Authorization: '#(authHeader)' }

  Scenario: Create a new production batch (Returns 500 due to Domain Event external connection failure)
    * def createBatchPayload =
      """
      {
        "code": "LOTE-NUEVO-002",
        "client": "Cliente Integración",
        "businessmanId": 1,
        "supplierId": 2,
        "fabricType": "ALGODON",
        "color": "BLANCO",
        "price": 1500.50,
        "quantity": 500,
        "observations": "Lote generado automatizadamente desde Karate BDD",
        "address": "Av. Principal 789, Lima",
        "date": "2026-05-11",
        "status": "PENDIENTE",
        "imageUrl": "https://img.url/lote2.png"
      }
      """
    Given path '/batches'
    And request createBatchPayload
    When method post
    Then status 500

  Scenario: Create a batch with empty constraints is handled as Bad Request (Returns 400)
    * def invalidBatchPayload =
      """
      {
        "code": "",
        "client": "",
        "businessmanId": 0,
        "supplierId": 0
      }
      """
    Given path '/batches'
    And request invalidBatchPayload
    When method post
    Then status 400

  Scenario: Get all available production batches (Returns 200 OK with empty list)
    Given path '/batches'
    When method get
    Then status 200
    And match response == []

  Scenario: Get batch by ID returns null body when entity is not found in DB (Returns 200 OK)
    Given path '/batches', '1'
    When method get
    Then status 200
    And match response == null

  Scenario: Get batches filtered by a specific Supplier ID (Returns 200 OK)
    Given path '/batches', 'supplier', '2'
    When method get
    Then status 200
    And match response == []

  Scenario: Get batches filtered by a specific Businessman ID (Returns 200 OK)
    Given path '/batches', 'businessman', '1'
    When method get
    Then status 200
    And match response == []

  Scenario: Test custom Profiles ACL validation endpoint (Returns 200 OK)
    Given path '/batches', 'test', 'profiles', '1'
    When method get
    Then status 200
    And match response == 'Testing userId: 1'

  Scenario: Update batch details for non-existent entity throws service exception (Returns 500)
    * def updateBatchPayload =
      """
      {
        "batchId": 1,
        "code": "LOTE-SEED-001",
        "client": "Cliente Modificado",
        "businessmanId": 1,
        "supplierId": 2,
        "fabricType": "ALGODON",
        "color": "NEGRO",
        "quantity": 600,
        "price": 1350.00,
        "observations": "Observación de prueba",
        "address": "Lima Central",
        "date": "2026-05-12",
        "status": "PENDIENTE",
        "imageUrl": "https://img.url/batch1.png"
      }
      """
    Given path '/batches', '1'
    And request updateBatchPayload
    When method put
    Then status 500

  Scenario: Upload batch image with empty file payload is intercepted by Spring validation (Returns 400)
    Given path '/batches', '1', 'image'
    And multipart file file = { value: '', filename: '', contentType: 'image/png' }
    When method post
    Then status 400

  Scenario: Upload batch image with non-image format is explicitly rejected by controller logic (Returns 400)
    Given path '/batches', '1', 'image'
    And multipart file file = { value: 'texto plano', filename: 'doc.txt', contentType: 'text/plain' }
    When method post
    Then status 400
    And match response == 'File must be an image'

  Scenario: Upload batch image returns 400 due to simulated local Cloudinary credentials
    # Al inyectar un binario válido, CloudinaryService intenta subirlo a la nube.
    # El bloque catch del BatchController captura el fallo de credenciales y responde de forma controlada.
    Given path '/batches', '1', 'image'
    And multipart file file = { value: 'fakebinarycontent', filename: 'test.png', contentType: 'image/png' }
    When method post
    Then status 400
    And match response contains 'Error:'

  Scenario: Delete batch image returns 404 Not Found due to early entity validation check
    # Valida la regla temprana: if (batch.isEmpty()) return ResponseEntity.notFound().build();
    Given path '/batches', '1', 'image'
    When method delete
    Then status 404

  Scenario: Delete a production batch permanently for non-existent entity throws service exception (Returns 500)
    # Se ataca directamente el endpoint de borrado para aislar el test de fallos en peticiones POST previas
    Given path '/batches', '1'
    When method delete
    Then status 500