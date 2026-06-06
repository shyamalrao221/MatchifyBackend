pipeline {
    agent any

    environment {
        PROJECT_ID = 'matchify-backend-shyamal-2026'
        REGION = 'asia-south1'
        ZONE = 'asia-south1-a'
        CLUSTER_NAME = 'matchify-gke-cluster'
        REPO_NAME = 'matchify-backend-repo'
        IMAGE_NAME = 'matchify-backend'
        IMAGE_TAG = "${BUILD_NUMBER}"
        IMAGE_URI = "${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/${IMAGE_NAME}:${IMAGE_TAG}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Debug Workspace') {
            steps {
                sh 'echo "Current directory:"'
                sh 'pwd'
                sh 'echo "Top-level files:"'
                sh 'ls -la'
                sh 'echo "Searching for deployment manifests:"'
                sh 'find . -name "deployment.yaml" -o -name "service.yaml"'
                sh 'echo "Searching for k8s directory:"'
                sh 'find . -type d -name "k8s"'
                sh '''
                    if [ -f k8s/deployment.yaml ]; then
                      echo "----- k8s/deployment.yaml -----"
                      sed -n '1,240p' k8s/deployment.yaml
                    else
                      echo "k8s/deployment.yaml not found"
                    fi
                '''
            }
        }

        stage('Build JAR') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${IMAGE_URI} .'
            }
        }

        stage('Authenticate to GCP') {
            steps {
                withCredentials([file(credentialsId: 'gcp-service-account-key', variable: 'GCP_KEY_FILE')]) {
                    sh 'gcloud auth activate-service-account --key-file=$GCP_KEY_FILE'
                    sh 'gcloud config set project ${PROJECT_ID}'
                    sh 'gcloud auth configure-docker ${REGION}-docker.pkg.dev --quiet'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                sh 'docker push ${IMAGE_URI}'
            }
        }

        stage('Get GKE Credentials') {
            steps {
                sh 'gcloud container clusters get-credentials ${CLUSTER_NAME} --zone ${ZONE} --project ${PROJECT_ID}'
            }
        }

        stage('Deploy to GKE') {
            steps {
                sh '''
                    kubectl apply -f k8s/deployment.yaml
                    kubectl apply -f k8s/service.yaml
                    kubectl set image deployment/matchify-backend matchify-backend=${IMAGE_URI}
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                sh 'kubectl rollout status deployment/matchify-backend'
                sh 'kubectl get deployment matchify-backend -o wide'
                sh 'kubectl get pods -o wide'
                sh 'kubectl get svc'
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Matchify backend built, pushed, deployed, and verified.'
        }
        failure {
            echo 'FAILURE: Pipeline failed. Check Jenkins logs for the exact step.'
        }
    }
}
