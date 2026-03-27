# Sprint Backlog - Settimana 1
**Periodo:** 23-27 Marzo 2026

---

## Obiettivo della settimana

L'obiettivo che ci siamo dati per questa prima settimana era quello di costruire le fondamenta dell'applicazione: un backend funzionante con tutte le API necessarie e un frontend collegato al backend reale, navigabile e che coprisse le funzionalità principali per i diversi tipi di utente.

---

## Cosa abbiamo fatto - Backend

### Setup del progetto

Come prima cosa abbiamo configurato il progetto Spring Boot con tutte le dipendenze necessarie: MySQL come database, JPA/Hibernate per la persistenza, Lombok per ridurre il codice boilerplate e MapStruct per la conversione tra entity e DTO. Abbiamo anche configurato il file application.properties con le impostazioni di connessione al database e di Jackson.

### Modellazione del dominio

Abbiamo creato tutte le entity JPA che rappresentano il dominio di Bookly. Nello specifico abbiamo modellato l'utente (con i ruoli GUEST, OWNER e ADMIN), l'attività (con gli stati PENDING, APPROVED e REJECTED per la moderazione), il servizio offerto dall'attività, la prenotazione (con i suoi stati PENDING, CONFIRMED, CANCELLED e COMPLETED), la recensione (con moderazione e campo createdAt impostato lato server), il diary per la gestione degli slot di disponibilità e infine la segnalazione (reporting). Ogni entity ha le sue relazioni JPA configurate correttamente, facendo attenzione a non usare @Data su classi con relazioni lazy per evitare problemi con Hibernate.

### API REST

Per ogni risorsa abbiamo creato controller, service, repository e mapper. Il backend espone endpoint per tutte le operazioni principali: CRUD completo su utenti, attività, servizi, prenotazioni, recensioni e slot del diario. Abbiamo aggiunto anche endpoint specifici come le prenotazioni filtrate per utente o per attività, gli slot disponibili per data, le recensioni approvate per attività, e operazioni di aggiornamento parziale (PATCH) per campi come email, password, bio, foto profilo e stato delle risorse.

Abbiamo anche aggiunto il campo activityId al BookingDTO, derivato automaticamente da MapStruct navigando la relazione service -> activity, perché il frontend ne aveva bisogno per caricare gli slot disponibili durante la riprogrammazione di una prenotazione.

### Sicurezza JWT

Abbiamo implementato il sistema di autenticazione con JWT: l'AuthController espone gli endpoint di login e registrazione, il JwtService si occupa di generare e validare i token, e il JwtAuthenticationFilter intercetta ogni richiesta per controllare il token. La SecurityConfig per il momento lascia ancora tutti gli endpoint pubblici perché stiamo ancora sviluppando e non volevamo bloccarci con problemi di autorizzazione durante il lavoro. L'attivazione completa della sicurezza con anyRequest().authenticated() e il redirect post-login basato sul ruolo sono stati spostati alla settimana 2.

---

## Cosa abbiamo fatto - Frontend

### Setup e struttura base

Abbiamo inizializzato il progetto Angular 21 con architettura standalone (niente NgModules), configurato il routing, aggiunto W3.CSS come framework di stile e Font Awesome per le icone. Abbiamo creato i componenti condivisi Navbar e Footer, e Alessio ha implementato l'interceptor HTTP che aggiunge automaticamente il token JWT a ogni richiesta verso il backend.

### Homepage

Gabriela ha costruito la homepage pubblica con una sezione hero, tre card che spiegano i diversi ruoli della piattaforma (cliente, titolare, amministratore) e una sezione per la registrazione dell'attività. Tutti i bottoni della homepage controllano il ruolo dell'utente loggato prima di navigare: per esempio un titolare che clicca su "Prenota ora" vede un messaggio di errore invece di andare alla pagina dei servizi, e un guest loggato che clicca su "Accedi al pannello" va direttamente alla sua dashboard.

### Login e sessione utente

Il form di login è collegato al backend reale. Dopo il login il token JWT viene salvato in localStorage e il UserService lo decodifica per ricavare i dati dell'utente (id, nome, ruolo, ecc.) che vengono tenuti in un signal reattivo disponibile in tutta l'applicazione. Per ora dopo il login tutti vengono mandati alla homepage; il redirect differenziato per ruolo (guest alla guest-dashboard, owner alla owner-dashboard) è stato lasciato alla prossima settimana insieme all'implementazione dei route guard.

### Ricerca attività e prenotazione

Abbiamo creato la pagina di ricerca attività con filtri, il componente card per visualizzare ogni attività, la pagina di dettaglio con la lista dei servizi disponibili e la pagina globale dei servizi con filtri per categoria. Andrea ha implementato il form di prenotazione collegato al backend.

### Dashboard titolare

La dashboard del titolare (owner-dashboard) mostra le informazioni sulla sua attività e le card di navigazione verso le altre sezioni. Abbiamo creato la pagina di registrazione di una nuova attività, la pagina di gestione dei servizi con CRUD completo e modifica inline, e il calendario della disponibilità dove il titolare può aggiungere ed eliminare slot giornalieri.

### Dashboard guest

E' stata la parte piu' lunga della settimana. Abbiamo costruito da zero una dashboard completa per il cliente loggato con quattro sezioni principali.

La sezione prenotazioni mostra la lista delle prenotazioni dell'utente con badge colorati per lo stato. Da li' il cliente puo' cancellare una prenotazione (se e' ancora in attesa o confermata), riprogrammarla scegliendo un nuovo slot disponibile dell'attivita' (tramite modal con date picker e selezione slot), oppure scrivere una recensione se la prenotazione e' gia' stata completata e non ha ancora una recensione associata.

La sezione recensioni mostra lo storico di tutte le recensioni scritte dall'utente, con le stelle, il testo, la data di pubblicazione e il badge di moderazione (in attesa, approvata, rifiutata).

La pagina profilo permette all'utente di vedere i propri dati personali, impostare una foto profilo tramite URL e modificare la propria bio direttamente in pagina senza form separati.

Il componente della dashboard e' diventato molto grande durante lo sviluppo, quindi abbiamo deciso di scomporlo in sotto-componenti piu' piccoli: GuestBookingsSection per la tabella delle prenotazioni, GuestReviewsSection per la lista delle recensioni, RescheduleModal per il modal di riprogrammazione e ReviewModal per quello della recensione. I modal gestiscono il loro stato interno (testo, valutazione, data selezionata) e comunicano con il componente padre solo quando c'e' qualcosa da salvare, usando il pattern input/output con signals di Angular 21.

---

## Stato finale e cosa manca

La settimana e' andata abbastanza bene. Il backend e' completo su tutte le risorse e il frontend copre tutti i flussi principali. Cio' che e' rimasto aperto e che dobbiamo assolutamente chiudere la prossima settimana e':

- attivare la sicurezza JWT sul backend (anyRequest().authenticated()) e smettere di lasciare tutto pubblico
- implementare i route guard su Angular per proteggere le rotte private
- gestire il redirect post-login differenziato per ruolo
- costruire la dashboard admin per l'approvazione delle attivita', la moderazione delle recensioni e la gestione delle segnalazioni

Queste ultime funzionalita', in particolare la dashboard admin e i guard, sono le piu' critiche per poter fare i test finali del martedi'.
