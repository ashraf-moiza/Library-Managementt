# Library Management System

A beginner-friendly Spring Boot and MySQL library app. It manages books and students, records loans and returns, tracks available copies, and serves a responsive browser interface from the same Spring Boot application.

## Requirements

- Java 17 or newer (the project compiles for Java 17; check with `java -version`)
- Maven 3.9 or newer, or use the included Maven Wrapper (`mvnw.cmd`)
- MySQL 8 (or a compatible MySQL server)
- VS Code with Extension Pack for Java (optional)

## Project structure

`src/main/java/com/library/managementt` contains the application, `entity`, `repository`, `service`, `controller`, and `exception` packages. `src/main/resources/static` contains the frontend. `src/test/java` contains automated tests. The existing Maven wrapper is `mvnw.cmd`.

## Create the database

In MySQL Workbench, connect to your local server, open a query tab, and run:

```sql
CREATE DATABASE library_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

This creates the schema only; it does not remove or reset existing data. On startup, Hibernate's `update` setting creates or updates the app tables. It is convenient for local learning, but production applications should use managed migrations such as Flyway or Liquibase.

## Configure credentials on Windows

The defaults target `localhost:3306/library_management`, username `root`, and an empty password for a local MySQL setup only. Set environment variables in PowerShell before launching VS Code from that terminal. Use the account and password you created during MySQL setup:

```powershell
$env:DB_USERNAME = "your_mysql_user"
$env:DB_PASSWORD = "your_mysql_password"
code .
```

To set them persistently for your Windows account, use Windows Environment Variables settings, then restart VS Code. Do not put a real password in Java source or commit it. `src/main/resources/application.properties` uses Spring placeholders and supports `DB_URL` if the server address differs.

## Run from VS Code

Open this folder in VS Code, ensure Java 17 is selected, set the environment variables above, then run `ManagementtApplication.java` with **Run Java**. Alternatively, in the integrated terminal:

```powershell
.\mvnw.cmd spring-boot:run
```

Open <http://localhost:8080/>. The static frontend and REST API share the same origin; no CORS configuration is needed.

To confirm which Java installation VS Code and the terminal use, run `java -version`. If Java is missing or older than 17, install a Java 17 JDK, set `JAVA_HOME` to its installation folder, add `%JAVA_HOME%\bin` to `PATH`, restart VS Code, then check again. A newer JDK can run this Java 17 project, but Java 17 is the documented baseline.

## Features

- Dashboard totals for books, students, available copies, and issued copies.
- Searchable book and student directories with create, edit, and guarded delete operations.
- Issue a book to a registered student and return it once. The same student cannot have two active issue records for the same book.
- Search and filter issue history by student/book text and status.
- Bean validation, duplicate key checks, safe copy count updates, transactions, and clear API error responses.

## API quick reference

All endpoints are rooted at `/api`. Book and student list endpoints accept optional `q` search text. Issue history accepts optional `q` and `status` (`ISSUED` or `RETURNED`).

| Method | Endpoint | Purpose |
|---|---|---|
| GET, POST | `/api/books` | List/search or create books |
| GET, PUT, DELETE | `/api/books/{id}` | Read, update, or safely delete a book |
| GET, POST | `/api/students` | List/search or register students |
| GET, PUT, DELETE | `/api/students/{id}` | Read, update, or safely delete a student |
| GET, POST | `/api/issues` | Search issue history or create an issue |
| GET | `/api/issues/active` | List active issues |
| PUT | `/api/issues/{id}/return` | Record a return |
| GET | `/api/dashboard/stats` | Dashboard totals |

Example requests (PowerShell):

```powershell
curl.exe -X POST http://localhost:8080/api/books -H "Content-Type: application/json" -d '{"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","category":"Software","publisher":"Prentice Hall","publicationYear":2008,"quantity":3}'
curl.exe -X POST http://localhost:8080/api/students -H "Content-Type: application/json" -d '{"studentId":"S1001","name":"Asha Rao","email":"asha@example.com","phone":"555-0100","department":"Computer Science"}'
curl.exe -X POST http://localhost:8080/api/issues -H "Content-Type: application/json" -d '{"bookId":1,"studentId":1,"issueDate":"2026-10-09","dueDate":"2026-10-23"}'
curl.exe -X PUT http://localhost:8080/api/issues/1/return
curl.exe http://localhost:8080/api/issues?status=ISSUED
```

## Test and compile

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```

The service tests cover issue quantity decrement, unavailable copies, duplicate active issues, invalid due dates, and one-time return quantity restoration. The application context test requires a reachable MySQL database because the configured datasource is MySQL. Create the database, start MySQL, and set credentials before running `.\mvnw.cmd test` if the context test reports a connection error.

## Troubleshooting

- **Communications link failure:** Start MySQL and verify the host/port in `DB_URL`.
- **Maven dependency download or cache lock error:** Check your network connection and close other Maven builds using the same local Maven repository, then retry `.\mvnw.cmd test`.
- **Access denied:** Check `DB_USERNAME` and `DB_PASSWORD` in the terminal that launched VS Code.
- **Unknown database:** Create `library_management` in Workbench using the SQL above.
- **Port 8080 in use:** Stop the other server or launch with `--server.port=8081`.
- **Duplicate ISBN/student ID/email:** Choose a unique value; these fields are constrained by the database.
- **Delete rejected:** Return active issues before deleting the related book or student.
