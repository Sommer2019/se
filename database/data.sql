-- Testdaten. Nur einfügen, wenn die Tabelle noch leer ist, damit das Skript
-- mehrfach laufen kann. IDs vergibt die Datenbank selbst (AUTO_INCREMENT).
INSERT INTO message (text)
SELECT * FROM (VALUES ('Hallo aus der Datenbank!'), ('Zweite Nachricht'), ('Dritte Nachricht'))
WHERE NOT EXISTS (SELECT 1 FROM message);

-- Testen:
-- SELECT * FROM message;
-- UPDATE message SET text = 'Geänderter Text' WHERE id = 2;
-- Datenbank komplett löschen:
-- DROP ALL OBJECTS DELETE FILES; SHUTDOWN;
