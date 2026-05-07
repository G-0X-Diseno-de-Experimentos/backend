-- =====================================================
-- TextilFlow Platform - Database Foreign Keys Migration
-- Version: V1_1__add_foreign_keys.sql
-- Description: Adds all foreign key constraints between tables
-- =====================================================

-- ✅ 1. CONFIGURATIONS TABLE
-- Add foreign key from configurations to users (1:1 relationship)
ALTER TABLE configurations
    ADD CONSTRAINT FK_configurations_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- ✅ 2. BUSINESSMEN TABLE
-- Add foreign key from businessmen to users (1:1 relationship)
ALTER TABLE businessmen
    ADD CONSTRAINT FK_businessmen_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- ✅ 3. SUPPLIERS TABLE
-- Add foreign key from suppliers to users (1:1 relationship)
ALTER TABLE suppliers
    ADD CONSTRAINT FK_suppliers_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- ✅ 4. BATCHES TABLE
-- Add foreign key from batches to businessmen (N:1 relationship)
ALTER TABLE batches
    ADD CONSTRAINT FK_batches_businessman_id
        FOREIGN KEY (businessman_id)
            REFERENCES businessmen (user_id)
            ON DELETE SET NULL;

-- Add foreign key from batches to suppliers (N:1 relationship)
ALTER TABLE batches
    ADD CONSTRAINT FK_batches_supplier_id
        FOREIGN KEY (supplier_id)
            REFERENCES suppliers (user_id)
            ON DELETE SET NULL;

-- ✅ 5. OBSERVATIONS TABLE
-- Add foreign key from observations to batches (N:1 relationship)
ALTER TABLE observations
    ADD CONSTRAINT FK_observations_batch_id
        FOREIGN KEY (batch_id)
            REFERENCES batches (id)
            ON DELETE CASCADE;

-- Add foreign key from observations to businessmen (N:1 relationship)
ALTER TABLE observations
    ADD CONSTRAINT FK_observations_businessman_id
        FOREIGN KEY (businessman_id)
            REFERENCES businessmen (user_id)
            ON DELETE CASCADE;

-- Add foreign key from observations to suppliers (N:1 relationship)
ALTER TABLE observations
    ADD CONSTRAINT FK_observations_supplier_id
        FOREIGN KEY (supplier_id)
            REFERENCES suppliers (user_id)
            ON DELETE CASCADE;

-- ✅ 6. BUSINESS_SUPPLIER_REQUESTS TABLE
-- Add foreign key from requests to businessmen (N:1 relationship)
ALTER TABLE business_supplier_requests
    ADD CONSTRAINT FK_requests_businessman_id
        FOREIGN KEY (businessman_id)
            REFERENCES businessmen (user_id)
            ON DELETE CASCADE;

-- Add foreign key from requests to suppliers (N:1 relationship)
ALTER TABLE business_supplier_requests
    ADD CONSTRAINT FK_requests_supplier_id
        FOREIGN KEY (supplier_id)
            REFERENCES suppliers (user_id)
            ON DELETE CASCADE;

-- ✅ 7. SUPPLIER_REVIEWS TABLE
-- Add foreign key from reviews to businessmen (N:1 relationship)
ALTER TABLE supplier_reviews
    ADD CONSTRAINT FK_reviews_businessman_id
        FOREIGN KEY (businessman_id)
            REFERENCES businessmen (user_id)
            ON DELETE CASCADE;

-- Add foreign key from reviews to suppliers (N:1 relationship)
ALTER TABLE supplier_reviews
    ADD CONSTRAINT FK_reviews_supplier_id
        FOREIGN KEY (supplier_id)
            REFERENCES suppliers (user_id)
            ON DELETE CASCADE;

-- =====================================================
-- UNIQUE CONSTRAINTS (Optional - for data integrity)
-- =====================================================

-- Ensure each user can have only one configuration
ALTER TABLE configurations
    ADD CONSTRAINT UQ_configurations_user_id
        UNIQUE (user_id);

-- Ensure each user can be either businessman OR supplier (not both)
-- Note: This is handled at application level, but you can add a check constraint if needed

-- =====================================================
-- INDEXES FOR PERFORMANCE (Optional but recommended)
-- =====================================================

-- Index on foreign key columns for better query performance
-- Note: MySQL doesn't support IF NOT EXISTS for indexes, so we create them directly
CREATE INDEX IDX_batches_businessman_id ON batches (businessman_id);
CREATE INDEX IDX_batches_supplier_id ON batches (supplier_id);
CREATE INDEX IDX_observations_batch_id ON observations (batch_id);
CREATE INDEX IDX_observations_businessman_id ON observations (businessman_id);
CREATE INDEX IDX_observations_supplier_id ON observations (supplier_id);
CREATE INDEX IDX_requests_businessman_id ON business_supplier_requests (businessman_id);
CREATE INDEX IDX_requests_supplier_id ON business_supplier_requests (supplier_id);
CREATE INDEX IDX_reviews_businessman_id ON supplier_reviews (businessman_id);
CREATE INDEX IDX_reviews_supplier_id ON supplier_reviews (supplier_id);