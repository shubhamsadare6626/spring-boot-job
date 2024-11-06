pipeline {
    agent any
    
    tools {
        maven 'maven-3.9.5'  // Reference the Maven tool by the name you configured
    }
    
    stages {
        stage('Clone') {
            steps {
                script {
                    // Clone the repository from GitHub using HTTPS
                    git branch: 'main', 
                        credentialsId: 'github-https-credentials', 
                        url: 'https://github.com/shubhamsadare6626/spring-boot-job.git'
                }
            }
        }
        
        stage('Maven build') {
            steps {
                script {
                    // Use Maven to build the project
                    sh 'mvn clean compile install -DskipTests'
                }
            }
        }
        
        stage('Docker build') {
            steps {
                script {
                    // Build Docker image using Dockerfile in the current directory
                    sh 'docker build -t personal/spring-boot-job-service .'
		    
		    // Run a container from the newly built image
	            sh 'docker run -d --name spring-boot-job-service \
	            -p 8081:8081 \
	            -e SPRING_DATASOURCE_USERNAME=postgres \
	            -e SPRING_DATASOURCE_PASSWORD=root \
	            -e SPRING_DATASOURCE_HOST=65.1.219.35 \
	            -e SPRING_DATASOURCE_PORT=5432 \
	            personal/spring-boot-job-service'
		 }
            }
        }
    }
}
