# Yoga-app

## 📝 Description

Yoga-app is a full-stack application designed to manage yoga sessions, teachers, and users.
The application is built with **Angular** for the frontend and **Spring Boot** for the backend, using **MySQL** as the database.

---

## 🚀 Installation

### 1️⃣ Cloning the Project

Before installing the application, start by cloning the project from GitHub:

```bash
git clone https://github.com/maximedrouault/Yoga-app.git
cd Yoga-app
```

### 2️⃣ Prerequisites

Make sure you have installed the following tools:

- [Node.js](https://nodejs.org/) (version 16 or higher)
- [Angular CLI](https://angular.io/cli) (version 14 or higher)
- [Java JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) (version 8 or higher, 17 max)
- [Maven](https://maven.apache.org/) (version 3.8 or higher)
- [MySQL](https://dev.mysql.com/downloads/mysql/) (version 8.0 or higher)

### 3️⃣ Database Setup

1. Start MySQL and create the database:
   ```sql
   CREATE DATABASE yoga_app;
   ```
2. Update the connection details in **back/src/main/resources/application.properties**:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/yoga_app?allowPublicKeyRetrieval=true
   spring.datasource.username=YOUR_DB_USER  # Update according to your configuration
   spring.datasource.password=YOUR_DB_PASSWORD  # Update according to your configuration
   ```
3. Execute the SQL script:
   ```bash
   mysql -u root -p yoga_app < ressources/sql/script.sql
   ```

### 4️⃣ Backend Installation

1. Navigate to the backend directory:
   ```bash
   cd back
   ```
2. Build and run the project with Maven:
   ```bash
   mvn spring-boot:run
   ```
3. The backend will be accessible at **[http://localhost:8080](http://localhost:8080)**

### 5️⃣ Frontend Installation

1. Navigate to the frontend directory:
   ```bash
   cd front
   ```
2. Install npm dependencies:
   ```bash
   npm install
   ```
3. Start the Angular application:
   ```bash
   npm start
   ```
4. The frontend will be accessible at **[http://localhost:4200](http://localhost:4200)**

---

## ✅ Application Usage

### 📌 Postman Collection

A Postman collection is available to test the various API endpoints.

1. Navigate to the directory containing the Postman collection:
   ```bash
   cd ressources/postman
   ```
2. Import the **yoga.postman\_collection.json** file into Postman to test the backend endpoints.

### 📌 Authentication

The application uses a JWT authentication system.

- **Test Admin User**: `yoga@studio.com / test!1234`

### 📌 Key Features

Yoga-app provides the following key functionalities:

- **User Registration**: Allow new users to create an account.
- **User Authentication & Authorization**: Secure login with JWT.
- **Session Management**: Create, update, and delete yoga sessions.
- **Teacher Management**: Assign and manage yoga instructors.
- **User Participation**: Allow users to register for yoga sessions.

---

## 🔬 Testing

### 1️⃣ Backend Tests (JUnit + Jacoco)

Run only unit tests with the following command:

```bash
cd back
mvn clean test
```

Run both unit and integration tests with the following command:

```bash
mvn clean verify
```

Test results and the coverage report are generated at:

```bash
back/target/site/jacoco/index.html
```

### 2️⃣ Frontend Tests (Jest)

Run the tests with the following command:

```bash
cd front
npm run test:coverage
```

Test results and the coverage report are generated at:

```bash
front/coverage/jest/lcov-report/index.html
```

### 3️⃣ End-to-End Tests (Cypress)

Run E2E tests with:

```bash
npm run e2e:ci
```

Generate a Cypress coverage report with:

```bash
npm run e2e:coverage
```

Test results and the coverage report are generated at:

```bash
front/coverage/lcov-report/index.html
```