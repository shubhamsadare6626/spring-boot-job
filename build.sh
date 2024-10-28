git pull origin main && \
mvn clean compile package -DskipTests && \
docker-compose down && \
docker build -t personal/spring-boot-job-service . && \
docker image prune -f && docker-compose up -d && \
docker logs -f spring-boot-job-service
