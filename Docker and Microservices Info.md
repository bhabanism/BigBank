Start
docker compose up --build

Stop - 
docker compose down

Frontend should keep calling gateway (http://localhost:8080/api) as already configured.
Gateway: http://localhost:8080/actuator/health
Identity: http://localhost:8082/actuator/health
Banking: http://localhost:8083/actuator/health
Interest: http://localhost:8084/actuator/health
ActiveMQ console: http://localhost:8161
