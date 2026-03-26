# Backlog Bookly – 26/03/2026

---

## Backend

### `98c40bb` – creati e provati ultimi metodi Post e Delete per Activity
Completati e testati i metodi `POST` e `DELETE` per la risorsa Activity. CRUD Activity ora completo.

### `6623557` – CRUD su Reporting
Prima implementazione del CRUD per la risorsa Reporting (entity, DTO, repository, service, API).

### `d5dad70` – fix: corretto Booking entity, DTO, service, mapper, repository e API
Risolto un bug che coinvolgeva l'intera catena di Booking: allineati entity, DTO, mapper MapStruct, service e controller.

### `f0927e2` – CRUD per reporting
Iterazione successiva sul CRUD Reporting; aggiornamenti post-fix e consolidamento dei metodi.

### `0e1ef71` – fix: aggiunto package mancante in ReportingService, ripristinato naming repository originale
Corretto errore di compilazione causato da import mancante in `ReportingService`.
Ripristinato il nome originale del repository per coerenza con la convenzione del progetto.

### `f429196` – aggiunto a ReviewAPI: delete, put e getByUser
Estesa `ReviewAPI` con i metodi `DELETE`, `PUT` e `GET /byUser/{id}`.

### `e12b236` – UserApi, Service Api, test service, activity, test script
Implementate `UserAPI` e `ServiceAPI`.
Aggiunti i test JS per le API di Activity e Service:
- `OkActivityTestsApi.js` — test scenari 200 per ActivityAPI
- `ErrorActivityTestsApi.js` — test scenari di errore (400, 404, 500) per ActivityAPI
- `OkServiceTestsApi.js` — test scenari 200 per ServiceAPI
- `ErrorServiceTestsApi.js` — test scenari di errore per ServiceAPI
- `AllTests.js` — runner Node.js che esegue tutti i test in sequenza

### `5aaad31` – aggiunto CrossOrigin
Aggiunta l'annotazione `@CrossOrigin` sui controller per abilitare le chiamate dal frontend Angular su porta 4200.

### `f6a6685` – docs: aggiornata analisi funzionale API con CRUD completo UserAPI
Aggiornata la documentazione funzionale delle API con la specifica completa del CRUD per `UserAPI`.

### `2b9be2d` – create e testate le API CRUD per Diary
Implementate e testate tutte le API CRUD per la risorsa Diary.

### `f88d1b4` – documentazione funzionale api
Aggiornata la documentazione generale delle API (endpoint, payload, response codes).

### `480772d` – refactor(dto): sostituisce int con Integer per id e chiavi esterne nei DTO
Refactor trasversale a tutti i DTO: i campi `id` e le chiavi esterne passano da `int` a `Integer` per supportare valori `null` e migliorare la compatibilità con Jackson/JPA.

---

## Frontend

### `48f16d5` – refactor: rimozione spec.ts e riorganizzazione cartella service
Rimossi i file `*.spec.ts` generati automaticamente da Angular CLI per componenti e servizi.
Tutti i service spostati in un'unica cartella centralizzata.

### `3bda2a2` – aggiornato user.ts
Aggiornato il modello TypeScript `User` per allinearlo alle ultime modifiche del DTO backend.

### `2c0f020` – feat: aggiunge Navbar, Footer, ActivitySearchPage, ActivityCard e fix vari
Commit principale del branch `gabriela`. Include:
- Componente `Navbar` e `Footer` condivisi
- Pagina `ActivitySearchPage` per la ricerca delle attività
- Componente `ActivityCard` per visualizzare una singola attività
- Vari fix su routing e stili

### `871438e` – aggiunta guest_dashboard
Aggiunta la dashboard per gli utenti non autenticati (guest), con le funzionalità base di navigazione pubblica.

### `bbec81a` – aggiunto component form per aggiungere nuova attività
Aggiunto il form per la creazione di una nuova attività (branch `Alessio`), con binding `[(ngModel)]` e invio al backend.

### `63ed585` – fix: reindirizzamento registerActivity
Corretto il reindirizzamento post-registrazione attività: dopo il submit il router naviga alla pagina corretta.

### PR #3, #5, #6, #7, #8 – merge branch dev, gabriela, Alessio (×2), andrea
Integrazione progressiva dei branch di sviluppo nel `master`:
- `dev` → base condivisa aggiornata
- `gabriela` → Navbar, Footer, ActivitySearch, ActivityCard
- `Alessio` (×2) → form nuova attività, fix e aggiustamenti vari
- `andrea` → contributi del branch andrea
