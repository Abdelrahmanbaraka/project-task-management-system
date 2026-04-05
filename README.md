
Dieses Projekt ist eine webbasierte Anwendung zur Verwaltung von Projekten, Aufgaben und Benutzern in einem IT-Dienstleistungsunternehmen. Ziel war es, eine bestehende Excel-basierte Lösung durch ein strukturiertes, sicheres und erweiterbares Softwaresystem zu ersetzen.

Die Anwendung ermöglicht eine zentrale Verwaltung von Projekten und Aufgaben sowie eine klare Trennung von Benutzerrollen und Zugriffsrechten.

---

## 🚀 Funktionen

Die Anwendung bietet folgende zentrale Funktionen:

- Erstellung und Verwaltung von Benutzerkonten
- Rollenbasierte Zugriffskontrolle (ADMIN, PROJECT_LEADER, EMPLOYEE)
- Erstellung, Bearbeitung und Archivierung von Projekten
- Zuweisung von Mitarbeitenden zu Projekten
- Erstellung und Bearbeitung von Aufgaben innerhalb von Projekten
- Änderung des Aufgabenstatus (OPEN, IN_PROGRESS, DONE)
- Anzeige des Projektfortschritts basierend auf erledigten Aufgaben
- Zugriff nur auf berechtigte Projekte
- Authentifizierung über HTTP Basic Authentication

---

## 🏗️ Technologien

### Frontend
- React
- React Router
- Fetch API
- CSS

### Backend
- Spring Boot
- Spring MVC
- Spring Security
- JPA / Hibernate

### Datenbank
- PostgreSQL

### Weitere Tools
- Maven
- Git & GitHub
- Thunder Client (API-Tests)
- draw.io (Diagramme)
- JUnit & Mockito (Tests)

---



---

## ⚙️ Installation und Start

### Voraussetzungen

- Java 17 oder höher
- Node.js (empfohlen v18+)
- PostgreSQL
- Mav

- --
### 1. Datenbank erstellen

```sql


CREATE DATABASE task_management_db;


2. Backend starten
cd backend
./mvnw spring-boot:run

Backend läuft auf:
http://localhost:8080
3. Frontend starten
cd frontend
npm install
npm run dev

Frontend läuft auf:
http://localhost:5173

🔐 Demo-Benutzer
Die Anwendung enthält initiale Benutzer:

Rolle	                   Username	          Passwort
Administrator            	admin            Admin123!
Projektleiter	            leader           Leader123!
Mitarbeiter               employee	       Employee123!


🔌 API-Endpunkte (Auswahl)
Authentifizierung prüfen
GET /api/auth/me
Benutzer erstellen (nur ADMIN)
POST /api/users
Projekte erstellen
POST /api/projects
Aufgaben erstellen
POST /api/tasks
Projektfortschritt anzeigen
GET /api/projects/{id}/progress



🔒 Sicherheit
Die Anwendung verwendet:
HTTP Basic Authentication
Rollenbasierte Autorisierung
Zugriffsbeschränkungen auf Endpunkte

Beispiele:
ADMIN darf Benutzer erstellen
PROJECT_LEADER darf Projekte verwalten
EMPLOYEE darf Aufgaben bearbeiten




Tests
Die Anwendung enthält Unit-Tests im Backend.
Tests ausführen:
cd backend
./mvnw test

Geprüft werden u.a.:
Benutzererstellung
Projektfortschritt-Berechnung




⚠️ Einschränkungen
Keine vollständige Multi-Tenancy implementiert
Verwendung von Basic Auth statt JWT (bewusst vereinfacht)
UI bewusst einfach gehalten (Fokus auf Funktionalität)



👤 Autor
Abdelrahman Baraka
