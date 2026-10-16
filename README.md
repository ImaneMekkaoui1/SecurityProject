# 🔐 TP JWT – Spring Boot Security

API REST sécurisée avec **Spring Security** et **JWT (JSON Web Token)** : inscription, authentification, génération et validation de token, protection des endpoints.

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen)
![JWT](https://img.shields.io/badge/JJWT-0.13.0-blue)
![H2](https://img.shields.io/badge/DB-H2%20in--memory-lightgrey)

---

## 📑 Sommaire

1. [Objectif du TP](#-objectif-du-tp)
2. [Technologies utilisées](#-technologies-utilisées)
3. [Architecture du projet](#-architecture-du-projet)
4. [Fonctionnement de l'authentification JWT](#-fonctionnement-de-lauthentification-jwt)
5. [Configuration](#-configuration)
6. [Installation et lancement](#-installation-et-lancement)
7. [Endpoints de l'API](#-endpoints-de-lapi)
8. [Tests et captures d'écran](#-tests-et-captures-décran)
9. [Résultats des tests](#-résultats-des-tests)
10. [Auteur](#-auteur)

---

## 🎯 Objectif du TP

Mettre en place une authentification **stateless** basée sur JWT avec Spring Boot :

- Enregistrer un utilisateur avec un mot de passe **haché (BCrypt)**.
- Authentifier l'utilisateur et lui délivrer un **token JWT signé** (HMAC-SHA).
- Protéger les routes `/api/**` : accessibles uniquement avec un token valide.
- Vérifier qu'un **token modifié est refusé**.

---

## 🛠 Technologies utilisées

| Technologie | Version | Rôle |
|---|---|---|
| Java | 25 | Langage |
| Spring Boot | 4.1.1 | Framework |
| Spring Security | (starter) | Sécurité / filtres |
| Spring Data JPA | (starter) | Persistance |
| JJWT | 0.13.0 | Génération / validation JWT |
| H2 Database | in-memory | Base de données + console web |
| Lombok | - | Réduction du code répétitif |
| Maven (wrapper) | - | Build |
| Postman | - | Tests des endpoints |

---

## 🗂 Architecture du projet

```
src/main/java/org/mekkaoui/tp_jwt
├── TpJwtApplication.java
├── config
│   └── SecurityConfig.java            # SecurityFilterChain, BCrypt, AuthenticationManager
├── controllers
│   ├── AuthController.java            # /auth/register, /auth/login
│   └── UserController.java            # /api/hello, /api/profile (protégés)
├── dtos
│   ├── RegisterRequest.java           # email, password
│   ├── LoginRequest.java              # email, password
│   └── AuthResponse.java              # token
├── entities
│   └── AppUser.java                   # table USERS (id, email, password, role)
├── repository
│   └── UserRepository.java
├── security
│   └── JwtFilter.java                 # lit le header Authorization: Bearer <token>
└── service
    ├── JwtService.java                # génération, extraction, validation du token
    └── CustomerUserDetailsService.java
```

---

## ⚙️ Fonctionnement de l'authentification JWT

```
 Client                                   API Spring Boot
   │  POST /auth/register                      │
   │ ────────────────────────────────────────► │  mot de passe haché (BCrypt) → H2
   │  POST /auth/login (email, password)       │
   │ ────────────────────────────────────────► │  AuthenticationManager vérifie
   │ ◄──────────────── { "token": "eyJ..." }   │  JwtService génère le token
   │                                           │
   │  GET /api/profile                         │
   │  Authorization: Bearer eyJ...             │
   │ ────────────────────────────────────────► │  JwtFilter valide signature + expiration
   │ ◄──────────────── 200 OK                  │  → utilisateur placé dans le SecurityContext
```

Le token contient le **subject** (email), la date d'émission (`iat`) et la date d'expiration (`exp`), signé avec la clé secrète définie dans `application.properties`.

---

## 🔧 Configuration

Fichier `src/main/resources/application.properties` :

```properties
spring.application.name=Tp_jwt

# Base H2 en mémoire
spring.datasource.url=jdbc:h2:mem:securitydb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Console H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JWT
jwt.secret=<clé en Base64>
jwt.expiration=3600000      # 1 heure (en ms)
```

> ⚠️ **Sécurité** : la clé `jwt.secret` est ici une clé de démonstration. En production, ne la versionnez pas : utilisez une variable d'environnement (`JWT_SECRET`) ou un gestionnaire de secrets.

---

## 🚀 Installation et lancement

**Prérequis** : JDK 25, Git (Maven n'est pas nécessaire grâce au wrapper).

```bash
# 1. Cloner le projet
git clone https://github.com/ImaneMekkaoui1/SecurityProject.git
cd SecurityProject

# 2. Lancer l'application
./mvnw spring-boot:run          # Linux / macOS
mvnw.cmd spring-boot:run        # Windows
```

L'application démarre sur **http://localhost:8080**.
La console H2 est disponible sur **http://localhost:8080/h2-console** (JDBC URL : `jdbc:h2:mem:securitydb`, user : `sa`, mot de passe vide).

---

## 📡 Endpoints de l'API

| Méthode | URL | Accès | Description |
|---|---|---|---|
| `POST` | `/auth/register` | Public | Crée un utilisateur (rôle `USER`) |
| `POST` | `/auth/login` | Public | Retourne un token JWT |
| `GET` | `/api/hello` | 🔒 JWT requis | Message de bienvenue |
| `GET` | `/api/profile` | 🔒 JWT requis | Retourne l'utilisateur connecté |
| `GET` | `/h2-console/**` | Public | Console de la base H2 |

**Corps JSON** pour `/auth/register` et `/auth/login` :

```json
{
  "email": "student@gmail.com",
  "password": "123456"
}
```

---

## 🧪 Tests et captures d'écran

Tous les tests ont été réalisés avec **Postman**.

### Test 1 – Inscription d'un utilisateur

`POST /auth/register` avec l'email et le mot de passe en JSON.
✅ **Résultat attendu** : `200 OK` – `Utilisateur cree`

<img width="468" height="434" alt="image" src="https://github.com/user-attachments/assets/7fc2e82f-cb7a-4b4a-a803-e96a4fd7be63" />


### Test 2 – Connexion et génération du token JWT

`POST /auth/login` avec les mêmes identifiants.
✅ **Résultat attendu** : `200 OK` – réponse JSON contenant le champ `token` (header `eyJhbGciOiJIUzM4NCJ9` = HS384, puis payload, puis signature).

<img width="468" height="429" alt="image" src="https://github.com/user-attachments/assets/1b580886-5572-4836-a8da-5f90cb01225b" />


### Test 3 – Vérification en base de données (console H2)

Requête `SELECT * FROM USERS` : l'utilisateur est bien enregistré avec le rôle `USER` et son **mot de passe haché en BCrypt** (`$2a$10$...`), jamais en clair.

<img width="468" height="159" alt="image" src="https://github.com/user-attachments/assets/c261814c-0b15-470e-8aa3-6ad2e11be7e4" />


### Test 4 – Utilisation du token en *Bearer Token*

Le token reçu au login est copié dans l'onglet **Authorization → Bearer Token** de Postman. Postman ajoute automatiquement le header `Authorization: Bearer <token>` aux requêtes suivantes.

<img width="468" height="457" alt="image" src="https://github.com/user-attachments/assets/ac2ef7a6-210c-4826-9d28-c0e5a7c3f9a5" />


### Test 5 – Accès à un endpoint protégé : `/api/hello`

`GET /api/hello` avec un token valide.
✅ **Résultat attendu** : `200 OK` – `Bienvenue dans l api securisee`

<img width="468" height="222" alt="image" src="https://github.com/user-attachments/assets/5a4f2b3d-f728-448a-a262-f6d335dc7260" />


### Test 6 – Accès au profil : `/api/profile`

`GET /api/profile` avec un token valide : le filtre `JwtFilter` extrait l'email du token et authentifie l'utilisateur.
✅ **Résultat attendu** : `200 OK` – `Utilisateur connecte : student@gmail.com`

<img width="468" height="216" alt="image" src="https://github.com/user-attachments/assets/a5578529-89ea-4929-9f23-7ec107ceba8e" />


### Test 7 – Token modifié (test négatif)

On altère quelques caractères du token puis on rappelle `GET /api/profile`. La signature ne correspond plus : le token est rejeté et l'accès est refusé.
❌ **Résultat attendu** : `403 Forbidden`

<img width="468" height="416" alt="image" src="https://github.com/user-attachments/assets/3033a4ca-3596-45af-bb06-7ebdfc0662d7" />


---

## ✅ Résultats des tests

| # | Scénario | Requête | Résultat obtenu | Statut |
|---|---|---|---|---|
| 1 | Inscription | `POST /auth/register` | `200` – Utilisateur cree | ✅ |
| 2 | Connexion | `POST /auth/login` | `200` – token JWT retourné | ✅ |
| 3 | Persistance + BCrypt | `SELECT * FROM USERS` (H2) | 1 ligne, mot de passe haché | ✅ |
| 4 | Envoi du token | Authorization Bearer | Header généré par Postman | ✅ |
| 5 | Endpoint protégé | `GET /api/hello` + token valide | `200` | ✅ |
| 6 | Endpoint protégé | `GET /api/profile` + token valide | `200` – student@gmail.com | ✅ |
| 7 | Token modifié | `GET /api/profile` + token altéré | `403 Forbidden` | ✅ (refus attendu) |

---

## 👩‍💻 Auteur

**Imane Mekkaoui** – Master SDIA (Systèmes Distribués et Intelligence Artificielle), ENSET Mohammedia – Université Hassan II de Casablanca.

Dépôt : [github.com/ImaneMekkaoui1/SecurityProject](https://github.com/ImaneMekkaoui1/SecurityProject)
