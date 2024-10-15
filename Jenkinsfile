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
                    sh 'docker build -t personal/springboot-job .'
		    
		    // Run a container from the newly built image
	            sh 'docker run -d --name springboot-job \
	            -p 8081:8081 \
	            -e SPRING_DATASOURCE_USERNAME=postgres \
	            -e SPRING_DATASOURCE_PASSWORD=root \
	            -e SPRING_DATASOURCE_HOST=3.109.123.151 \
	            -e SPRING_DATASOURCE_PORT=5432 \
	            personal/springboot-job'
		 }
            }
        }
    }
}
