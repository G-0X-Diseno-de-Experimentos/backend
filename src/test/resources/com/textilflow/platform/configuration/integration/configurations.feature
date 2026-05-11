Feature: Configuration Management Integration Tests

  Background:
    * url 'http://localhost:8080/api/v1'
    * def userEmail = 'empresario.core@mail.com'
    * def userPassword = 'Password123!'

    # ==========================================================================
    # Autenticación Dinámica e Inyección de Cabeceras Globales
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
  # 1. ESCENARIOS DE CREACIÓN (POST /api/v1/configurations)
  # ============================================================================

  Scenario: Create a new user configuration fails safely due to DB constraints (Client Error - 400)
    # Al inyectar un User ID que no preexiste validado en la tabla de usuarios,
    # JPA arroja una excepción de integridad interceptada por el catch genérico del controlador.
    * def createConfigPayload =
      """
      {
        "userId": 999,
        "language": "es",
        "viewMode": "dark",
        "subscriptionPlan": "corporate"
      }
      """
    Given path '/configurations'
    And request createConfigPayload
    When method post
    Then status 400

  Scenario: Create a configuration with unreadable enum mapping fails safely (Client Error - 400)
    # Language.fromString lanza IllegalArgumentException ante valores fuera del enum.
    # El controlador lo captura y retorna badRequest().
    * def invalidEnumPayload =
      """
      {
        "userId": 998,
        "language": "frANCES_INVALIDO",
        "viewMode": "auto",
        "subscriptionPlan": "basic"
      }
      """
    Given path '/configurations'
    And request invalidEnumPayload
    When method post
    Then status 400

  Scenario: Create a duplicate configuration for an existing User ID triggers DB violation (Client Error - 400)
    * def duplicateUserPayload =
      """
      {
        "userId": 1,
        "language": "en",
        "viewMode": "light",
        "subscriptionPlan": "basic"
      }
      """
    Given path '/configurations'
    And request duplicateUserPayload
    When method post
    Then status 400

  # ============================================================================
  # 2. ESCENARIOS DE CONSULTA (GET /api/v1/configurations?userId=X)
  # ============================================================================

  Scenario: Get configuration settings for an unpopulated User ID returns Not Found (Client Error - 404)
    # Valida la regla programada: if (configuration.isEmpty()) return ResponseEntity.notFound().build();
    Given path '/configurations'
    And param userId = 1
    When method get
    Then status 404

  Scenario: Get configuration for a non-existent User ID returns clean Not Found response (Client Error - 404)
    Given path '/configurations'
    And param userId = 999999
    When method get
    Then status 404

  # ============================================================================
  # 3. ESCENARIOS DE ACTUALIZACIÓN (PUT /api/v1/configurations/{id})
  # ============================================================================

  Scenario: Update configuration details for non-existent entity throws service exception caught as 400
    # El servicio lanza IllegalArgumentException si el ID no existe en BD.
    # El controlador captura específicamente esta excepción y responde 400 Bad Request.
    * def updateConfigPayload =
      """
      {
        "language": "en",
        "viewMode": "light",
        "subscriptionPlan": "basic",
        "subscriptionStatus": "active"
      }
      """
    Given path '/configurations', '1'
    And request updateConfigPayload
    When method put
    Then status 400

  Scenario: Update configuration settings for a non-existent resource ID safely returns Bad Request (Client Error - 400)
    * def updateConfigPayload =
      """
      {
        "language": "es",
        "viewMode": "auto",
        "subscriptionPlan": "corporate",
        "subscriptionStatus": "pending"
      }
      """
    Given path '/configurations', '999999'
    And request updateConfigPayload
    When method put
    Then status 400

  Scenario: Update configuration settings with a malformed Subscription Status enum fails safely (Client Error - 400)
    * def malformedStatusPayload =
      """
      {
        "language": "es",
        "viewMode": "dark",
        "subscriptionPlan": "basic",
        "subscriptionStatus": "STATUS_INVENTADO"
      }
      """
    Given path '/configurations', '1'
    And request malformedStatusPayload
    When method put
    Then status 400