Feature: Observations Management Integration Tests

  Background:
    * url 'http://localhost:8080/api/v1'
    * def userEmail = 'empresario.core@mail.com'
    * def userPassword = 'Password123!'

    # ==========================================================================
    # Autenticación e Inyección de Cabeceras Globales
    # ==========================================================================
    * def authPayload = { "email": "#(userEmail)", "password": "#(userPassword)" }
    Given path '/authentication', 'sign-in'
    And request authPayload
    When method post
    Then status 200
    * def authToken = response.token
    * def authHeader = 'Bearer ' + authToken
    * configure headers = { Authorization: '#(authHeader)' }

  # ============================================================================
  # 1. ESCENARIOS DE CREACIÓN (POST /api/v1/observations)
  # ============================================================================

  Scenario: Create a new observation successfully (Happy Path - 201 Created)
    * def createObservationPayload =
      """
      {
        "batchId": 1,
        "batchCode": "LOTE-SEED-001",
        "businessmanId": 1,
        "supplierId": 2,
        "reason": "Fibras con leve decoloración en el muestreo inicial",
        "imageUrl": "https://img.url/obs1.png",
        "status": "pendiente"
      }
      """
    Given path '/observations'
    And request createObservationPayload
    When method post
    # El controlador responde con HTTP 201 Created y el recurso serializado
    Then status 201
    And match response.batchId == 1
    And match response.batchCode == 'LOTE-SEED-001'
    And match response.status == 'PENDIENTE'
    * def createdObservationId = response.id

  Scenario: Create an observation with empty batchCode throws VO validation exception (Server Error - 500)
    # Al pasar un batchCode vacío, el Value Object BatchCode lanza IllegalArgumentException.
    # Al no existir un bloque try-catch en ObservationsController, retorna HTTP 500.
    * def invalidCodePayload =
      """
      {
        "batchId": 1,
        "batchCode": "",
        "businessmanId": 1,
        "supplierId": 2,
        "reason": "Razón válida",
        "imageUrl": "https://img.url/obs.png",
        "status": "pendiente"
      }
      """
    Given path '/observations'
    And request invalidCodePayload
    When method post
    Then status 500

  Scenario: Create an observation with invalid status enum mapping throws exception (Server Error - 500)
    # ObservationStatus.valueOf(...) falla al intentar mapear un estado inexistente.
    * def invalidStatusPayload =
      """
      {
        "batchId": 1,
        "batchCode": "LOTE-SEED-001",
        "businessmanId": 1,
        "supplierId": 2,
        "reason": "Razón de prueba",
        "imageUrl": "",
        "status": "ESTADO_INVENTADO"
      }
      """
    Given path '/observations'
    And request invalidStatusPayload
    When method post
    Then status 500

  # ============================================================================
  # 2. ESCENARIOS DE CONSULTA (GET /api/v1/observations/*)
  # ============================================================================

  Scenario: Get an observation by a non-existent ID returns clean Not Found (Client Error - 404)
    # Evalúa: if (observation.isPresent()) [...] return ResponseEntity.notFound().build();
    Given path '/observations', '999999'
    When method get
    Then status 404

  Scenario: Get observations associated with a specific Batch ID (Happy Path - 200 OK)
    Given path '/observations', 'batch', '1'
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Get observations associated with a specific Businessman ID (Happy Path - 200 OK)
    Given path '/observations', 'businessman', '1'
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Get observations visible to a specific Supplier ID (Happy Path - 200 OK)
    Given path '/observations', 'supplier', '2'
    When method get
    Then status 200
    And match response == '#[]'

  # ============================================================================
  # 3. ESCENARIOS DE ACTUALIZACIÓN (PUT /api/v1/observations/{id})
  # ============================================================================

  Scenario: Update an observation for a non-existent ID safely returns Not Found (Client Error - 404)
    # El servicio retorna Optional.empty() al no hallar la entidad, y el controlador responde 404.
    * def updateObservationPayload =
      """
      {
        "reason": "Razón corregida",
        "imageUrl": "https://img.url/obs_new.png",
        "status": "en_revision"
      }
      """
    Given path '/observations', '999999'
    And request updateObservationPayload
    When method put
    Then status 404

  # ============================================================================
  # 4. GESTIÓN DE IMÁGENES EXTERNAS (ObservationImagesController)
  # ============================================================================

  Scenario: Upload an image to a non-existent observation ID returns Not Found (Client Error - 404)
    # Valida: if (observation.isEmpty()) return ResponseEntity.notFound().build();
    Given path '/observations', '999999', 'images'
    And multipart file file = { value: 'fakebinarycontent', filename: 'obs.png', contentType: 'image/png' }
    When method post
    Then status 404

  Scenario: Delete an image from a non-existent observation ID returns Not Found (Client Error - 404)
    Given path '/observations', '999999', 'images'
    When method delete
    Then status 404

  # ============================================================================
  # 5. ESCENARIOS DE BORRADO (DELETE /api/v1/observations/{id})
  # ============================================================================

  Scenario: Delete an observation successfully returns No Content (Happy Path - 204)
    # Paso 1: Creamos una observación temporal con imageUrl vacío ("") para evitar
    # que intente contactar a Cloudinary durante su eliminación en BD.
    * def tempObsPayload =
      """
      {
        "batchId": 1,
        "batchCode": "LOTE-SEED-001",
        "businessmanId": 1,
        "supplierId": 2,
        "reason": "Observación temporal destinada al borrado",
        "imageUrl": "",
        "status": "pendiente"
      }
      """
    Given path '/observations'
    And request tempObsPayload
    When method post
    Then status 201
    * def obsToDelete = response.id

    # Paso 2: Consumimos el borrado y validamos el código HTTP 204 No Content
    Given path '/observations', obsToDelete
    When method delete
    Then status 204