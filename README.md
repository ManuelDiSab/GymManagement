 Gym Management System API REST
 
Applicazione sviluppata con Spring, Jpa, Spring security, 

## 🚀 Caratteristiche Principali

Il sistema gestisce tre tipologie di utenti con permessi granulari grazie a **Spring Security**:

* **Admin**: Gestione completa degli utenti, assegnazione ruoli, creazione/eliminazione abbonamenti e monitoraggio globale.
* **Instructor (istruttore)**: Gestione delle classi, visualizzazione dei partecipanti e pianificazione degli allenamenti.
* **Cliente (cliente)**: Visualizzazione del proprio abbonamento, prenotazione delle classi e gestione del profilo.

---

## 🛠 Tech Stack

* **Java 17+**
* **Spring Boot 3.x**
* **Spring Data JPA**: Per la persistenza dei dati.
* **Spring Security**: Autenticazione e autorizzazione basata su **JWT** (JSON Web Token).
* **MySQL**: Database relazionale.
* **Maven**: Gestione delle dipendenze.

---

## 🔑 Sicurezza e Ruoli

L'accesso alle risorse è protetto tramite annotazioni `@PreAuthorize`.

| Ruolo | Permessi |
| :--- | :--- |
| `ROLE_ADMIN` | Accesso totale (CRUD Utenti, Abbonamenti, Ruoli). |
| `ROLE_TEACHER` | Gestione Classi e visualizzazione iscritti. |
| `ROLE_USER` | Visualizzazione profilo e prenotazioni. |

---

## 📂 Struttura degli Endpoint (Esempi)

### Autenticazione
* `POST /api/auth/register` - Registrazione nuovo utente.
* `POST /api/auth/login` - Login e ricezione del token JWT.

### Gestione Utenti (Admin)
* `GET /api/users` - Lista completa degli utenti.
* `PATCH /api/users/{id}/makeadmin` - Promuove un utente ad Admin.

---

## ⚙️ Configurazione Locale

1. **Clona il repository**:
   ```bash
   git clone [https://github.com/tuo-username/gym-management.git](https://github.com/tuo-username/gym-management.git)
2. Configura il Database:
Modifica src/main/resources/application.properties:
Properties
spring.datasource.url=jdbc:mysql://localhost:3306/gym_db
spring.datasource.username=tuo_user
spring.datasource.password=tua_password

