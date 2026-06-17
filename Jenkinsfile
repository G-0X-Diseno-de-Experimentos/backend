pipeline {
  agent any

  tools {
    maven 'MAVEN_3_9_11'
    jdk 'JDK_21'
  }

  environment {
    IMAGE_NAME = "textilflow-platform"
    TAG        = "${env.BUILD_NUMBER}"
  }

  stages {

    stage('Compile Project') {
      steps {
        withMaven(maven: 'MAVEN_3_9_11') {
          sh 'mvn clean compile'
        }
      }
    }

    stage('Validate Checkstyle') {
      steps {
        withMaven(maven: 'MAVEN_3_9_11') {
          sh 'mvn checkstyle:check'
        }
      }
    }

    stage('Validate Unit Tests') {
      steps {
        withMaven(maven: 'MAVEN_3_9_11') {
          sh 'mvn test'
        }
      }
    }

    stage('Validate Test Coverage') {
      steps {
        withMaven(maven: 'MAVEN_3_9_11') {
          sh 'mvn clean verify jacoco:report'
          sh 'mvn jacoco:check'
        }
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('MiSonarServer') {
          sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=textilflow-platform'
        }
        script {
          timeout(time: 10, unit: 'MINUTES') {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
              error "El pipeline se ha detenido porque el código no superó el Quality Gate de SonarQube. Estado: ${qg.status}"
            }
          }
        }
      }
    }

    stage('Construir Imagen Docker') {
      steps {
        script {
          echo "Iniciando la construcción de la imagen de Docker: ${IMAGE_NAME}:${TAG}"
          sh "docker buildx build --platform linux/amd64 -t ${IMAGE_NAME}:${TAG} --load ."
          sh "docker buildx build --platform linux/amd64 -t ${IMAGE_NAME}:latest --load ."
          echo "Imagen construida exitosamente."
        }
      }
    }

  }

  post {
    success {
      echo 'Build exitoso ✔'
    }
    failure {
      echo 'Build falló ❌'
    }
    always {
      echo 'Pipeline finalizado'
    }
  }
}
