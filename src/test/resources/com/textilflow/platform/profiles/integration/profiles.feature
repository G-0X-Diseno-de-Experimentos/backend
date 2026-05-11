Feature: Complete Profiles Management Integration Tests

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
  # 1. GESTIÓN DE EMPRESARIOS (/api/v1/businessmen)
  # ============================================================================

  Scenario: Create a new businessman profile successfully (Happy Path - 201 Created)
    # Creamos el perfil inicial para habilitar las consultas y actualizaciones posteriores
    * def createBusinessmanPayload =
      """
      {
        "companyName": "Textil Core S.A.C.",
        "ruc": "20123456789",
        "businessType": "MANUFACTURING",
        "description": "Confección de prendas premium",
        "website": "https://textilcore.com"
      }
      """
    Given path '/businessmen', '1'
    And request createBusinessmanPayload
    When method post
    Then status 201
    And match response.userId == 1
    And match response.companyName == 'Textil Core S.A.C.'

  Scenario: Get existing businessman profile by User ID successfully (Happy Path - 200 OK)
    Given path '/businessmen', '1'
    When method get
    Then status 200
    And match response.userId == 1
    And match response.companyName == 'Textil Core S.A.C.'

  Scenario: Get businessman profile for a non-existent User ID returns Not Found (Client Error - 404)
    Given path '/businessmen', '999999'
    When method get
    Then status 404

  Scenario: Get all businessmen profiles returns a valid JSON array (Happy Path - 200 OK)
    Given path '/businessmen'
    When method get
    Then status 200
    And match response == '#[]'
    And match response[*].userId contains 1

  Scenario: Update existing businessman profile successfully (Happy Path - 200 OK)
    * def updateBusinessmanPayload =
      """
      {
        "companyName": "Textil Core Modificada S.A.C.",
        "ruc": "20123456789",
        "businessType": "MANUFACTURING",
        "description": "Descripción actualizada desde prueba de integración Karate",
        "website": "https://textilcore-mod.com",
        "name": "Empresario Core Editado",
        "email": "empresario.core@mail.com",
        "country": "Peru",
        "city": "Lima",
        "address": "Av. Industrial 456 Modificada",
        "phone": "987654321"
      }
      """
    Given path '/businessmen', '1'
    And request updateBusinessmanPayload
    When method put
    Then status 200
    And match response.companyName == 'Textil Core Modificada S.A.C.'
    And match response.website == 'https://textilcore-mod.com'

  Scenario: Update non-existent businessman profile throws unhandled service exception (Server Error - 500)
    * def updatePayload = { "companyName": "Test Cia", "ruc": "20123456789" }
    Given path '/businessmen', '999999'
    And request updatePayload
    When method put
    Then status 500

  Scenario: Update businessman profile with invalid RUC throws unhandled VO exception (Server Error - 500)
    # El Value Object Ruc exige estrictamente 11 dígitos numéricos mediante regex
    * def invalidRucPayload = { "companyName": "Cia Válida", "ruc": "123" }
    Given path '/businessmen', '1'
    And request invalidRucPayload
    When method put
    Then status 500

  # ============================================================================
  # 2. GESTIÓN DE PROVEEDORES (/api/v1/suppliers)
  # ============================================================================

  Scenario: Create a new supplier profile successfully (Happy Path - 201 Created)
    # Registramos el perfil de proveedor bajo el mismo ID 1 (permitido por la lógica y alineado con IAM)
    * def createSupplierPayload =
      """
      {
        "companyName": "Fibras del Sur S.A.",
        "ruc": "20987654321",
        "specialization": "COTTON",
        "description": "Productores de algodón orgánico",
        "certifications": "ISO 9001, GOTS"
      }
      """
    Given path '/suppliers', '1'
    And request createSupplierPayload
    When method post
    Then status 201
    And match response.userId == 1
    And match response.companyName == 'Fibras del Sur S.A.'

  Scenario: Get existing supplier profile by User ID successfully (Happy Path - 200 OK)
    Given path '/suppliers', '1'
    When method get
    Then status 200
    And match response.userId == 1
    And match response.companyName == 'Fibras del Sur S.A.'

  Scenario: Get supplier profile for a non-existent User ID returns Not Found (Client Error - 404)
    Given path '/suppliers', '999999'
    When method get
    Then status 404

  Scenario: Get all suppliers profiles returns a valid JSON array (Happy Path - 200 OK)
    Given path '/suppliers'
    When method get
    Then status 200
    And match response == '#[]'
    And match response[*].userId contains 1

  Scenario: Update existing supplier profile successfully (Happy Path - 200 OK)
    * def updateSupplierPayload =
      """
      {
        "companyName": "Fibras del Sur Editada S.A.",
        "ruc": "20987654321",
        "specialization": "COTTON",
        "description": "Productores premium certificados",
        "certifications": "ISO 9001, GOTS, FAIR TRADE",
        "name": "Proveedor Core Editado",
        "email": "proveedor.core@mail.com",
        "country": "Peru",
        "city": "Arequipa",
        "address": "Calle Fibras 123",
        "phone": "912345678"
      }
      """
    Given path '/suppliers', '1'
    And request updateSupplierPayload
    When method put
    Then status 200
    And match response.companyName == 'Fibras del Sur Editada S.A.'
    And match response.certifications contains 'FAIR TRADE'

  # ============================================================================
  # 3. GESTIÓN DE PERFILES COMBINADOS (/api/v1/profiles)
  # ============================================================================

  Scenario: Get complete combined profile data for an existing user (Happy Path - 200 OK)
    # Al haber persistido exitosamente ambos perfiles para el ID 1, el ensamblador
    # de perfiles consolidados poblará los objetos businessman y supplier en el JSON.
    Given path '/profiles', '1'
    When method get
    Then status 200
    And match response.userId == 1
    And match response.userRole == 'SUPPLIER'
    And match response.businessman != null
    And match response.supplier != null

  Scenario: Get complete profile data for a non-existent IAM User ID returns Not Found (Client Error - 404)
    Given path '/profiles', '999999'
    When method get
    Then status 404

  # ============================================================================
  # 4. GESTIÓN DE LOGOS EXTERNOS (/api/v1/profiles/{userId}/images/logo)
  # ============================================================================

  Scenario: Upload profile logo returns 500 due to simulated local Cloudinary credentials
    # ProfileImagesController enruta por rol a la capa de servicio, la cual intenta subir la imagen a la nube.
    # Al carecer de bloque try-catch, el fallo de conexión externo se traduce limpiamente a HTTP 500.
    Given path '/profiles', '1', 'images', 'logo'
    And multipart file file = { value: 'fakebinarylogocontent', filename: 'logo.png', contentType: 'image/png' }
    When method post
    Then status 500

  Scenario: Delete profile logo successfully returns 200 OK (Happy Path - 200 OK)
    # Al estar el campo logoUrl como nulo (debido a que el upload previo falló por infraestructura externa),
    # el servicio omite la llamada de borrado a Cloudinary, actualiza la entidad y retorna un éxito atómico.
    Given path '/profiles', '1', 'images', 'logo'
    When method delete
    Then status 200
    And match response.message == 'Logo deleted successfully'

  Scenario: Delete profile logo for a non-existent profile throws service exception (Server Error - 500)
    Given path '/profiles', '999999', 'images', 'logo'
    When method delete
    Then status 500