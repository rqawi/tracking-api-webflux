# Tracking API WebFlux

A scalable and reactive Tracking Number Generator API built with:
- Java 17
- Spring Boot 3.1 (WebFlux)
- Reactive MongoDB
- Kafka (Reactive Producer)
- Prometheus metrics
- Docker Compose for local MongoDB & Kafka
- GCP Kubernetes deployment & Cloud Build CI/CD

---

## Features

- Stateless, unique tracking number generation conforming to `^[A-Z0-9]{1,16}$`
- Reactive MongoDB persistence
- Event publishing via Kafka
- Prometheus metrics exposed via Spring Boot Actuator
- Ready for containerized deployment and GCP Kubernetes

---

## Prerequisites

- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- (Optional) GCP SDK (`gcloud`) for deployment to Google Cloud

---

## Local Setup

### 1. Start MongoDB and Kafka with Docker Compose

```bash
docker-compose up -d


2. Verify services are running
bash
docker ps
You should see mongo, zookeeper, and kafka containers running.

3. Build and run the application
bash

mvn clean package
java -jar target/tracking-api-webflux.jar
Or use Spring Boot plugin:

bash

mvn spring-boot:run

API Usage
Endpoint

GET /next-tracking-number
Query Parameters
origin_country_id (ISO Alpha-2)

destination_country_id (ISO Alpha-2)

weight (double)

customer_id (UUID string)

customer_name (string)

customer_slug (slug-case string)

Sample Request
bash

curl 'http://localhost:8080/next-tracking-number?origin_country_id=US&destination_country_id=MY&weight=2.5&customer_id=uuid-123&customer_name=Acme&customer_slug=acme'
Sample Response
json

{
  "trackingNumber": "USMYA1B2C3D4",
  "generatedAt": "2025-05-15T12:00:00Z"
}

Metrics and Monitoring
Prometheus metrics available at:

bash

http://localhost:8080/actuator/prometheus

Health check endpoint:

bash

http://localhost:8080/actuator/health
Docker & Kubernetes Deployment
Build Docker Image
bash

docker build -t tracking-api-webflux .
Push to Docker Registry

bash

docker tag tracking-api-webflux your-dockerhub-username/tracking-api-webflux:latest
docker push your-dockerhub-username/tracking-api-webflux:latest
Deploy to Kubernetes (GKE or any Kubernetes)
bash

kubectl apply -f deployment.yaml
GCP Cloud Build
Trigger build and deploy pipeline using:

bash

gcloud builds submit --config cloudbuild.yaml
Make sure you have configured:

Artifact Registry or Container Registry

Kubernetes Engine cluster

kubectl context pointing to your cluster

Testing
Run unit and integration tests with:

bash

mvn test
Troubleshooting

If MongoDB connection fails, ensure MongoDB is running locally or update the spring.data.mongodb.uri in application.yml.

If Kafka connection fails, ensure Kafka and Zookeeper containers are running.

Enable debug logs by adding --debug to Maven run command.