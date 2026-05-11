DELETE FROM observations;
DELETE FROM supplier_reviews;
DELETE FROM business_supplier_requests;
DELETE FROM batches;
DELETE FROM configurations;
DELETE FROM businessmen;
DELETE FROM suppliers;
DELETE FROM users;

INSERT INTO users (id, name, email, password, country, city, address, phone, role_name, created_at)
VALUES
    (1, 'Empresario Core', 'empresario.core@mail.com', '$2a$10$D.LUSlB7Bha/wE8.G2.L.O4MGAXqL5hWMyo4D3m9nU1e8KGBE.5mO', 'Peru', 'Lima', 'Av. Industrial 456', '987654321', 'BUSINESSMAN', CURRENT_TIMESTAMP),
    (2, 'Proveedor Core', 'proveedor.core@mail.com', '$2a$10$D.LUSlB7Bha/wE8.G2.L.O4MGAXqL5hWMyo4D3m9nU1e8KGBE.5mO', 'Peru', 'Arequipa', 'Calle Fibras 123', '912345678', 'SUPPLIER', CURRENT_TIMESTAMP);

INSERT INTO businessmen (id, user_id, company_name, ruc, business_type, description, website, logo_url)
VALUES
    (1, 1, 'Textil Core S.A.C.', '20123456789', 'MANUFACTURING', 'Confección de prendas premium', 'https://textilcore.com', 'https://img.url/logo1.png');

INSERT INTO suppliers (id, user_id, company_name, ruc, specialization, description, certifications, logo_url)
VALUES
    (2, 2, 'Fibras del Sur S.A.', '20987654321', 'COTTON', 'Productores de algodón orgánico', 'ISO 9001, GOTS', 'https://img.url/logo2.png');

INSERT INTO configurations (id, user_id, language, view_mode, subscription_plan, subscription_status)
VALUES
    (1, 1, 'ES', 'AUTO', 'PREMIUM', 'ACTIVE'),
    (2, 2, 'ES', 'LIGHT', 'BASIC', 'ACTIVE');

INSERT INTO batches (id, code, client, businessman_id, supplier_id, fabric_type, color, price, quantity, observations, address, date, status, image_url)
VALUES
    (1, 'LOTE-SEED-001', 'Cliente Semilla', 1, 2, 'ALGODON', 'AZUL', 1200.00, 400, 'Lote inicial listo para revisión', 'Almacén Lima', CURRENT_DATE, 'PENDIENTE', 'https://img.url/batch1.png');

INSERT INTO business_supplier_requests (id, businessman_id, supplier_id, message, batch_type, color, quantity, address, status, created_at)
VALUES
    (1, 1, 2, 'Solicitud de cotización inicial', 'COTTON', 'BLANCO', 500, 'Av. Industrial 456', 'PENDING', CURRENT_TIMESTAMP);

INSERT INTO supplier_reviews (id, supplier_id, businessman_id, rating, review_content)
VALUES
    (1, 2, 1, 5, 'Excelente calidad y entrega a tiempo.');