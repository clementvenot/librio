***

# 📚 Librio

Application web simple pour gérer des livres :

*   ✅ Création / suppression de livres
*   🔍 Recherche par (titre, auteur, éditeur, catégories, note minimale)
*   ⭐ Ajout / suppression de favoris par utilisateur
*   📖 Détails complets d’un livre

***

## ✅ Prérequis

*   **Java** 17+
*   **Maven** 3.13+
*   **MySQL** 8

***

## 🚀 Installation rapide

```bash
# 1. Cloner le projet
git clone https://github.com/<ton-user>/librio.git
cd librio

# 2. Créer la base MySQL
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS librio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 3. Importer le script SQL
mysql -u root -p librio < librio.sql

# 4. Build Maven (parent + modules)
mvn -q clean install
```

***

## ⚙️ Configuration

Modifier le fichier :  
`librio-backend/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/librio?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

***

## ▶️ Démarrage

### Backend

```bash
cd librio-backend
mvn spring-boot:run
# http://localhost:8080
```

### Frontend

```bash
cd librio-frontend
mvn spring-boot:run
# http://localhost:8081
```

***

## 🖥 Utilisation (Frontend)

*   **/login** : définir un cookie `userEmail` après vérification login
*   **/register** : création de compte

### Pages principales :

*   **/ui/books**
    *   Liste des livres (ou résultats si filtres)
    *   Formulaire : Créer un livre
    *   Actions : Supprimer / Favori / Retirer des favoris / Détails

*   **/ui/books/{externalId}** : Détail complet du livre

*   **/account**
    *   Changer mot de passe
    *   Mes favoris (liens vers détails)
    *   Lien vers la page livres
    *   Logout

ℹ️ L’app frontend utilise un cookie `userEmail` pour simuler l’utilisateur courant. Sans ce cookie, plusieurs pages redirigent vers `/login`.

***

## 🔌 API principales

Base API (backend) : `http://localhost:8080`

### Favorites

*   `GET /api/favorites` → Lister les favoris d’un utilisateur
*   `POST /api/favorites` → Ajouter un favori
*   `DELETE /api/favorites` → Retirer un favori

### Users

*   `PUT /api/users/password` → Mettre à jour le mot de passe (par email)
*   `POST /api/users` → Créer un utilisateur
*   `POST /api/users/exists` → Vérifier l’existence d’un utilisateur par email
*   `POST /api/auth/login` → Connexion utilisateur

### Books

*   `GET /api/books` → Lister les livres
*   `POST /api/books` → Créer un livre
*   `GET /api/books/{externalId}/summary` → Infos résumées
*   `GET /api/books/{externalId}/full` → Infos détaillées
*   `GET /api/books/{externalId}/exists` → Vérifier existence
*   `GET /api/books/search` → Rechercher des livres
*   `DELETE /api/books/{externalId}` → Supprimer un livre

***

## 🧪 Données

Le fichier `librio.sql` :

*   Crée les tables nécessaires
*   Insère quelques livres de démonstration

***
