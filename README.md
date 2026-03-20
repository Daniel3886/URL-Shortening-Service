# URL Shortening Service

A full-stack URL shortening service with per-IP rate limiting, and scheduled link expiration.
The backend is built with Spring Boot, PostgreSQL, and Flyway for schema migrations
the frontend with Next.js, TypeScript, and Tailwind.
Fully containerized with Docker, Docker Compose and a deployed frontend demo on Vercel.

---

## Getting Started

### Prerequisites
- Docker and Docker Compose
- Java 21 (for local backend development)
- Node.js 18+ (for local frontend development)

### Run with Docker Compose

```bash
git clone https://github.com/Daniel3886/URL-Shortening-Service.git
cd Url-Shortening-Service
cp backend/.env.example backend/.env
cp frontend/.env.example frontend/.env
docker compose up --build
```

The app will be available at:
- Frontend: http://localhost:3000
- Backend: http://localhost:8080

### Environment Variables

**Backend** (`backend/.env`):
```env
# NOTE: if using docker-compose use db:5432 instead of localhost:5432
DATABASE_URL=jdbc:postgresql://localhost:5432/urlshortener
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
BASE_URL=http://localhost:8080
```

**Frontend** (`frontend/.env`):
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

## Features

- **URL shortening** — generate a short code for any URL
- **Automatic expiration** — links expire after 24 hours of url creation
- **Rate limiting** — per-IP request throttling powered by Bucket4j to prevent spamming requests
- **Database migrations** — schema versioning with Flyway and PostgreSQL as the database
- **Global exception handling** — consistent error responses across the API

---

## Tech Stack

### Backend
| Technology            | Purpose             |
|-----------------------|---------------------|
| Java 21 + Spring Boot | REST API            |
| PostgreSQL            | Persistent storage  |
| Flyway                | Database migrations |
| Bucket4j              | Rate limiting       |
| Docker                | Containerization    |

### Frontend
| Technology              | Purpose           |
|-------------------------|-------------------|
| Next.js 14 (App Router) | React framework   |
| TypeScript              | Type safety       |
| Tailwind CSS            | Styling           |
| shadcn/ui               | Component library |

## API Reference

| Method   | Endpoint                     | Description                  |
| -------- | ---------------------------- | ---------------------------- |
| `POST`   | `/shorten`                   | Create a shortened URL       |
| `GET`    | `/shorten/{shortCode}`       | Redirect to the original URL |
| `PUT`    | `/shorten/{shortCode}`       | Update the original URL      |
| `DELETE` | `/shorten/{shortCode}`       | Delete a shortened URL       |
| `GET`    | `/shorten/{shortCode}/stats` | Retrieve URL statistics      |

## Project Structure

```
url-shortening-service/
├── backend/
│   ├── src/main/java/org/springboot/urlshorteningservice/
│   │   ├── config/          # CORS and app configuration
│   │   ├── controller/      # REST controllers
│   │   ├── dto/             # Request/response DTOs
│   │   ├── exception/       # Global exception handler
│   │   ├── filter/          # Rate limiting filter
│   │   ├── model/           # JPA entities
│   │   ├── repository/      # Data access layer
│   │   └── service/         # Business logic
│   ├── src/main/resources/
│   │   ├── db/migration/    # Flyway SQL migrations
│   │   └── application.yaml
│   └── Dockerfile
├── frontend/
│   ├── app/                 # Next.js App Router pages
│   ├── components/          # React components
│   ├── lib/                 # API client and utilities
│   └── Dockerfile
└── docker-compose.yaml
```

---


## License

[MIT](./LICENSE)
