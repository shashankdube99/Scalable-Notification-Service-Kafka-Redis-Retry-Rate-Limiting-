# Scalable-Notification-Service-Kafka-Redis-Retry-Rate-Limiting-
A robust and scalable notification microservice built with Spring Boot, designed to handle asynchronous message delivery using Apache Kafka, support retry mechanisms, and enforce rate limiting per user using Redis.

🧠 **Features**
✅ Send notifications asynchronously using Kafka
🔁 Automatic retry mechanism with exponential backoff
🚫 Redis-based per-user rate limiting (e.g., 5 reqs per 10 sec)
🧪 Easy-to-test endpoints with Postman or curl
🐳 Ready for Dockerized environments with Kafka + Zookeeper
📈 Scalable & production-friendly architecture

⚙️ **Technologies Used**
Java 17
Spring Boot 3
Apache Kafka
Redis
Docker + Docker Compose
Jackson (JSON Processing)
Lombok (optional)
Concurrent Queues / Executors (for retry logic)
Maven

🚀 **Setup & Installation**
Prerequisites
Java 17+
Maven
Docker + Docker Compose
Redis (runs inside Docker)
Kafka + Zookeeper (runs inside Docker)

1. Clone the repository
    **(https://github.com/shashankdube99/Scalable-Notification-Service-Kafka-Redis-Retry-Rate-Limiting-)**

2. Start Kafka and Redis using Docker
   **docker-compose up**

This will start:
Kafka on localhost:9092
Zookeeper on localhost:2181
Redis on localhost:6379
📁 Make sure docker-compose.yml is present in src/main/resources/.

3. Run the Spring Boot App
   mvn spring-boot:run

📬 **API Usage**

1. Send Notification
   Endpoint:
   POST /api/notifications

   Request Body:
   **{
  "to": "shashank@example.com",
  "message": "Hello again!"
}**

Response:
✅ 200 OK — if sent successfully
❌ 429 Too Many Requests — if rate limit exceeded
🔁 Retry happens automatically on transient failures

🔁 **Retry Mechanism**
If Kafka delivery fails or a simulated exception occurs, the message is added to an in-memory retry queue with exponential backoff:
| Attempt | Delay (ms) |
| ------- | ---------- |
| 1       | 1000       |
| 2       | 2000       |
| 3       | 4000       |
After 3 attempts, the system gives up and logs the failure.

📊 **Rate Limiting**
We use Redis to allow only 5 requests per user within 10 seconds. If the limit is crossed, the user receives:
❌ Rate limit exceeded for: user@example.com
Implemented using:
redisTemplate.opsForValue().increment(email);
redisTemplate.expire(email, Duration.ofSeconds(10));

🛠️ **Tools Used**
| Purpose          | Tool           |
| ---------------- | -------------- |
| Build            | Maven          |
| Messaging        | Kafka          |
| Rate Limiting    | Redis          |
| Dockerized Stack | Docker Compose |
| REST Client      | Postman / curl |
| Scheduler        | `@Scheduled`   |
| JSON Handling    | Jackson        |

📌 **Example Curl Command**
POST http://localhost:8080/api/notifications \
"Content-Type: application/json" \
{
  "to": "shashank@example.com",
  "message": "Hello again!"
}


🧪 **Testing**
Test via:
Postman
curl
Kafka logs (see successful/failed retries in console)
