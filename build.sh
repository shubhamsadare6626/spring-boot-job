git pull origin main && \
mvn clean compile package -DskipTests && \
docker-compose down && \
docker build -t personal/springboot-job . && \
docker image prune -f && docker-compose up -d && \
docker logs -f springboot-job
