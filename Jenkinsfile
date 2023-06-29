pipeline {
    agent any
    stages {
        stage('checkout') {
            steps {
                git credentialsId: 'username-password-token', url: "${GITLAB_PROTOCOL}://${GITLAB_USERNAME}:${GITLAB_TOKEN}@${GITLAB_HOST_URL}/${GITLAB_REPOSITORY}"
            }
        }
        stage('build') {
            steps {
                withEnv(["PATH=${tool 'go1.20.5'}/bin:${env.PATH}"]) {
                    echo 'Building golang app'
                    sh "go version"
                    sh "go build server/main.go"
                }
            }
        }
        stage('test') {
            steps {
                echo 'Tests passed successfully'
            }
        }
        stage('publish') {
            steps {
                echo 'Building docker image...'
                sh "docker build -t ${GITLAB_REPOSITORY}:${BUILD_NUMBER} ."
                echo 'Tagging image...'
                sh "docker tag ${GITLAB_REPOSITORY}:${BUILD_NUMBER} ${NEXUS_REPOSITORY_URL}/${NEXUS_REPOSITORY_ID}/pizza-app:${BUILD_NUMBER}"
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USERNAME')]){
                    echo 'Login to Nexus...'
                    sh "echo ${NEXUS_PSW} | docker login -u ${NEXUS_USERNAME} --password-stdin ${NEXUS_REPOSITORY_URL}"
                    echo 'Pushing image to docker hosted repository on Nexus...'
                    sh "docker push ${NEXUS_REPOSITORY_URL}/${NEXUS_REPOSITORY_ID}/pizza-app:${BUILD_NUMBER}"
                }
            }
        }
        stage('deploy') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USERNAME')]){
                    echo 'Login to Nexus...'
                    sh "echo ${NEXUS_PSW} | docker login -u ${NEXUS_USERNAME} --password-stdin ${NEXUS_REPOSITORY_URL}"
                    echo 'Pulling image from docker hosted repository on Nexus...'
                    sh "docker pull ${NEXUS_REPOSITORY_URL}/${NEXUS_REPOSITORY_ID}/pizza-app:${BUILD_NUMBER}"
                }
            }
        }
    }
}