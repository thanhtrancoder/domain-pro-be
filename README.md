# Domain Pro — Backend

A Spring Boot backend for Domain Pro, a personal project to showcase on CV. It provides APIs to
register, manage, and monitor domain names with authentication, notifications, and payment
integration.

## Tech Stack

- **Java 17**, **Spring Boot 3.5**
- **Spring Data JPA**, **Hibernate**, **MySQL**
- **Spring Security** with **JWT** and **OAuth2 Client** (e.g., Google)
- **ModelMapper**, **Lombok**
- **JavaMail (spring-boot-starter-mail)**
- Optional: **MoMo** payment integration

## Key Features

- **Authentication & Authorization**: JWT-based login/register, OAuth2 login.
- **Domain Management**: Register, renew, and manage domain records.
- **Notifications**: Email notifications for registration/renewal and alerts.
- **Payments**: MoMo wallet integration for checkout (if configured).

## Requirements

- JDK 17+
- Maven 3.9+
- MySQL 8+

## Configuration

Configure via environment variables or `src/main/resources/application.properties`.

Database:

- `SPRING_DATASOURCE_URL` (e.g.,
  `jdbc:mysql://localhost:3306/domain_pro?useSSL=false&serverTimezone=UTC`)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

JWT:

- `JWT_SECRET` (strong secret)
- `JWT_EXPIRATION` (ms, optional)

Mail:

- `SPRING_MAIL_HOST`, `SPRING_MAIL_PORT`
- `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD`
- `SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true`
- `SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true`

OAuth2 (example Google):

- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID`
- `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET`

MoMo (optional):

- `MOMO_PARTNER_CODE`, `MOMO_ACCESS_KEY`, `MOMO_SECRET_KEY`
- `MOMO_ENDPOINT` (sandbox/production)

Server:

- `SERVER_PORT` (default: 8080)

## Run Locally

```bash
mvn spring-boot:run
```

Or build and run the jar:

```bash
mvn clean package -DskipTests
java -jar target/domain-pro-be-0.0.1-SNAPSHOT.jar
```

## Project Structure

- `src/main/java/thanhtrancoder/domain_pro_be/`
    - `module/account`, `module/auth`, `module/notification`, `module/momo`, ...
    - `common/` utilities and shared components
- `src/main/resources/` properties, templates, static assets

