# / BooksAPI

> A high-performance Spring Boot 3 microservice for book discovery. Features a minimalistic developer playground with live `curl` execution, syntax-highlighted JSON responses, and production-ready Docker deployment.

---

### Core Features

- **Minimalist API Showcase**: Integrated live-demo UI designed for developers.
- **Real-time Discovery**: High-speed wrapper for the Open Library API.
- **Deep Filtering**: Search by Title, Author, Subject, or ISBN.
- **Zero-Config**: Completely public endpoints with no API key required.
- **Production Ready**: Optimized multi-stage Docker builds and Render/Koyeb blueprints.

---

### Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.0
- **Documentation**: OpenAPI 3 / Swagger UI
- **DevOps**: Docker (Alpine JRE), GitHub Actions, Render
- **Frontend**: Vanilla JS (ES6+), CSS Mesh Gradients, JetBrains Mono Typography

---

### API Usage

The service exposes a primary search endpoint with pagination support.

> **Note**: The URLs below are examples. Replace `localhost:8080` with your actual deployment URL.

#### Search Query Example

```bash
curl -X GET "http://localhost:8080/api/books/search?query=dune"
```

#### Filtered Search Example

```bash
curl -X GET "http://localhost:8080/api/books/search?filterType=author&filterValue=Frank+Herbert"
```

**Parameters:**

- `query`: General search string.
- `filterType`: One of `author`, `subject`, `isbn`.
- `filterValue`: Specific value for the filter.
- `page` (default `0`): Page index.
- `size` (default `10`): Page size.

---

### Caching

Book search responses are cached in-process using Caffeine. Identical searches reuse the cached response while entries remain within the configured TTL.

Default configuration:

- **TTL**: 300 seconds.
- **Maximum entries**: 500.
- **Cache key**: query, filter type, filter value, page, and size.

Configuration can be overridden with environment variables:

```text
BOOKS_CACHE_TTL_SECONDS=300
BOOKS_CACHE_MAX_SIZE=500
```

The cache is intentionally local to the application instance. A distributed cache such as Redis can be introduced later if multi-instance caching becomes necessary.

---

### Deployment

#### Local (Docker)

```bash
docker build -t books-api .
docker run -p 8080:8080 books-api
```

#### Render / Koyeb

Simply connect your GitHub repository to **Render** and apply the `render.yaml` blueprint. The service will automatically build via the `Dockerfile` and go live on your custom subdomain.

---

Built for high-performance discovery.
