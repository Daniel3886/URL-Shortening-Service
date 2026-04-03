# URL Shortening Service

A full-stack URL shortener built with Spring Boot and Next.js. Shorten links, track click statistics asynchronously via Kafka,
cache redirects with Redis, and enforce per-IP rate limiting with Bucket4j while all running in Docker.

---

## Prerequisites

- Docker and Docker Compose
- Node.js 18+

That's it for running the project. Everything else (Postgres, Redis, Kafka) runs inside containers.

---

## Running Locally

### 1. Clone the repo

```bash
git clone https://github.com/Daniel3886/URL-Shortening-Service.git
cd URL-Shortening-Service
```

### 2. Install frontend dependencies

```bash
cd frontend
npm install
cd ..
```

### 3. Build the backend

```bash
cd backend
./gradlew build -x test
cd ..
```
-x test skips tests for a faster build. Drop it if you want to run the test suite.

### 4. Set up environment files

**Root `.env`** (used by Docker Compose):
```dotenv
REDIS_PASSWORD=secret
```

**`frontend/.env`**:
```dotenv
NEXT_PUBLIC_API_BASE_URL="http://localhost:8080"
```

**`backend/src/main/resources/application-local.yaml`**:

Copy the example file and fill in your values:

```bash
cp backend/src/main/resources/application-example.yaml backend/src/main/resources/application.yaml
```

### 5. Build and run

```bash
docker compose up --build
```

---

## Features

- **URL shortening** — generate a short code for any valid URL
- **Redirects** — hitting a short URL redirects to the original destination
- **Redis caching** — short code lookups are cached so redirects skip the database entirely on repeat visits
- **Click tracking** — every redirect publishes an async Kafka event; a consumer writes click data to the database without slowing down the redirect
- **URL statistics** — see total click count and last visited time per short URL
- **Automatic expiration** — links expire after 24 hours (can be changed in application.yml)
- **Rate limiting** — per-IP request throttling via Bucket4j
- **Database migrations** — schema versioning with Flyway

---

## API Reference

Base path: `/shorten`

| Method | Endpoint                    | Description                  |
|--------|-----------------------------|------------------------------|
| POST   | `/shorten`                  | Create a shortened URL       |
| GET    | `/shorten/{shortCode}`      | Redirect to the original URL |
| PUT    | `/shorten/{shortCode}`      | Update the destination URL   |
| DELETE | `/shorten/{shortCode}`      | Delete a shortened URL       |
| GET    | `/shorten/{shortCode}/stats`| Retrieve URL statistics      |

---

## Tech Stack

**Backend**

| Technology              | Purpose                     |
|-------------------------|-----------------------------|
| Java 21 + Spring Boot 4 | REST API                    |
| PostgreSQL              | Persistent storage          |
| Flyway                  | Database migrations         |
| Redis                   | Redirect caching            |
| Apache Kafka (KRaft)    | Async click event streaming |
| Bucket4j                | Per-IP rate limiting        |
| Docker                  | Containerization            |

**Frontend**

| Technology              | Purpose |
|-------------------------|-------------------|
| Next.js 16 (App Router) | React framework   |
| React 19                | UI                |
| TypeScript              | Type safety       |  
| Tailwind CSS            | Styling           | 
| shadcn/ui + Radix UI    | Component library |

---

## Project Structure

```
url-shortening-service/
├── backend/
│   ├── src/main/java/org/springboot/urlshorteningservice/
│   │   ├── config/          # CORS, cache, and app configuration
│   │   ├── controller/      # REST controllers
│   │   ├── dto/             # Request/response DTOs and Kafka events
│   │   ├── exception/       # Global exception handler
│   │   ├── filter/          # Rate limiting filter
│   │   ├── model/           # JPA entities
│   │   ├── repository/      # Data access layer
│   │   └── service/         # Business logic, Kafka producer and consumer
│   ├── src/main/resources/
│   │   ├── db/migration/    # Flyway SQL migrations
│   │   └── application.yaml
│   └── Dockerfile
├── frontend/
│   ├── app/                 # Next.js App Router pages
│   ├── components/          # React components
│   ├── lib/                 # API client and utilities
│   └── Dockerfile
├── .env                     # Root env (Redis password for Docker Compose)
└── docker-compose.yaml
```

---

## License

[MIT](./LICENSE)
