# SE – System-Architektur: drei Schichten

Minimal Working Example zu Kapitel 05 „System-Architektur entwerfen“:

| Schicht | Ordner | Technik |
|---|---|---|
| 1 Datenbank | `database/` | H2 (SQL-Skripte) |
| 2 Backend (BE) | `backend/` | Java 25, Spring Boot 4.1.1, Spring Data JPA |
| 3 Präsentation (FE) | `frontend/` | React + Vite |

## Voraussetzungen

- JDK 25
- Node.js 20.19+ (bzw. 22.12+)

## Schicht 1: Datenbank

`database/schema.sql` legt die Tabelle `message` an, `database/data.sql` füllt sie mit Testdaten.
Beide Skripte lassen sich in der H2-Konsole ausführen (Editor-Bereich, grüner Pfeil).
Das Backend führt dieselben Skripte (`backend/src/main/resources/`) beim Start automatisch aus.

H2-Konsole des laufenden Backends: <http://localhost:8080/h2-console>

- JDBC URL: `jdbc:h2:file:./data/sedb;AUTO_SERVER=TRUE`
- Benutzer: `sa`, Passwort: leer

Die Datenbank liegt als Datei unter `backend/data/` und überlebt einen Neustart (Persistenz).
Komplett löschen: `DROP ALL OBJECTS DELETE FILES; SHUTDOWN;`

## Schicht 2: Backend

```bash
cd backend
./mvnw spring-boot:run      # Windows: mvnw.cmd spring-boot:run
./mvnw test
```

Läuft auf <http://localhost:8080>.

| Methode | Pfad | Beschreibung |
|---|---|---|
| GET | `/api/hello` | liefert einen einfachen String |
| GET | `/api/messages` | alle Nachrichten aus der DB |
| POST | `/api/messages` | neue Nachricht speichern, Body `{"text": "..."}` |

Die Klasse `WebConfig` erlaubt CORS für `http://localhost:5173`. Ohne sie blockiert der Browser
die Antworten des Backends (in den DevTools, F12, als CORS-Fehler sichtbar).

## Schicht 3: Frontend

```bash
cd frontend
npm install
npm run dev
```

Läuft auf <http://localhost:5173>.

- Test 1: Seite öffnen, der Inhalt aus `src/App.jsx` wird angezeigt.
- Test 2: Text in `src/App.jsx` ändern (z. B. „my first message line“), die Seite aktualisiert sich sofort.
- Test 3: Button „Backend fragen“ zeigt den String aus dem BE an.
- Test 4: Änderungen im FE sind dank Vite sofort sichtbar (Hot Module Replacement). Im BE sorgen die
  Spring Boot DevTools für einen automatischen Neustart, sobald neu kompiliert wird.
