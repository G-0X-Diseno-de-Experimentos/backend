Feature: Business-Supplier Requests Management Integration Tests

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

  # ============================================================================
  # 1. CREACIÓN DE SOLICITUDES (POST /api/v1/business-supplier-requests)
  # ============================================================================

  Scenario: Create a new business-supplier request successfully (Happy Path - 201 Created)
    * def createRequestPayload =
      """
      {
        "businessmanId": 1,
        "supplierId": 2,
        "message": "Solicitud inicial de cotización de lote de prueba",
        "batchType": "ALGODON_PIMA",
        "color": "AZUL_NAVY",
        "quantity": 1500,
        "address": "Av. Principal 123, Zona Industrial"
      }
      """
    Given path '/business-supplier-requests'
    And request createRequestPayload
    When method post
    Then status 201
    And match response.businessmanId == 1
    And match response.supplierId == 2
    And match response.status == 'PENDING'
    And match response.quantity == 1500

  Scenario: Create request with blank constraints throws VO validation exception (Server Error - 500)
    # Value Objects como Address y Color lanzan IllegalArgumentException ante cadenas vacías.
    # Al no existir try-catch en el controlador, el framework arroja HTTP 500.
    * def invalidPayload =
      """
      {
        "businessmanId": 1,
        "supplierId": 2,
        "message": "Cotización",
        "batchType": "ALGODON",
        "color": "",
        "quantity": 0,
        "address": ""
      }
      """
    Given path '/business-supplier-requests'
    And request invalidPayload
    When method post
    Then status 500

  # ============================================================================
  # 2. CONSULTA DE SOLICITUDES (GET /api/v1/business-supplier-requests/*)
  # ============================================================================

  Scenario: Get specific request by non-existent ID safely returns Not Found (Client Error - 404)
    # Valida la condición programada: if (request.isEmpty()) return ResponseEntity.notFound().build();
    Given path '/business-supplier-requests', '999999'
    When method get
    Then status 404

  Scenario: Get all existing requests returns a valid JSON collection (Happy Path - 200 OK)
    Given path '/business-supplier-requests'
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Get requests filtered by a specific Businessman ID (Happy Path - 200 OK)
    Given path '/business-supplier-requests', 'businessman', '1'
    When method get
    Then status 200
    And match response == '#[]'

  Scenario: Get requests filtered by a specific Supplier ID (Happy Path - 200 OK)
    Given path '/business-supplier-requests', 'supplier', '2'
    When method get
    Then status 200
    And match response == '#[]'

  # ============================================================================
  # 3. ACTUALIZACIÓN DE SOLICITUDES (PUT /status y /details)
  # ============================================================================

  Scenario: Update request status on a non-existent resource safely returns Not Found (Client Error - 404)
    # El servicio mapea la consulta devolviendo Optional vacío sin arrojar excepciones.
    * def statusPayload = { "status": "ACCEPTED", "message": "Aprobado formalmente" }
    Given path '/business-supplier-requests', '999999', 'status'
    And request statusPayload
    When method put
    Then status 404

  Scenario: Update request details on a non-existent resource safely returns Not Found (Client Error - 404)
    * def detailsPayload =
      """
      {
        "message": "Detalles modificados",
        "batchType": "ALGODON",
        "color": "BLANCO",
        "quantity": 2000,
        "address": "Nueva Dirección"
      }
      """
    Given path '/business-supplier-requests', '999999', 'details'
    And request detailsPayload
    When method put
    Then status 404

  # ============================================================================
  # 4. BORRADO DE SOLICITUDES (DELETE /api/v1/business-supplier-requests/{id})
  # ============================================================================

  Scenario: Delete a request successfully yields No Content (Happy Path - 204)
    # Creamos un registro temporal para garantizar un test atómico sin destruir datos base
    * def tempRequestPayload =
      """
      {
        "businessmanId": 1,
        "supplierId": 2,
        "message": "Registro temporal para borrado",
        "batchType": "TEST",
        "color": "ROJO",
        "quantity": 100,
        "address": "Lima"
      }
      """
    Given path '/business-supplier-requests'
    And request tempRequestPayload
    When method post
    Then status 201
    * def requestToDelete = response.id

    # Disparamos el borrado atómico
    Given path '/business-supplier-requests', requestToDelete
    When method delete
    Then status 204