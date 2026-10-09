-- Schicht 1: Datenbank (H2)
-- In der H2-Konsole ausführen (Editor-Bereich, grüner Pfeil).

CREATE TABLE IF NOT EXISTS message (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    text       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
