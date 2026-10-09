-- Testdaten. Die Tabelle legt Hibernate an (Entity Message).
-- Nur einfügen, wenn die Tabelle noch leer ist, damit das Skript mehrfach laufen kann.
INSERT INTO message (text, created_at)
SELECT t, CURRENT_TIMESTAMP
FROM (VALUES ('Hallo aus der Datenbank!'), ('Zweite Nachricht'), ('Dritte Nachricht')) AS v(t)
WHERE NOT EXISTS (SELECT 1 FROM message);
