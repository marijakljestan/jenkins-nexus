def checkoutRepository(){
    git credentialsId: 'username-password-token', url: "${GITLAB_PROTOCOL}://${GITLAB_USERNAME}:${GITLAB_TOKEN}@${GITLAB_HOST_URL}/${GITLAB_REPOSITORY}"
}

def testApp(){
    echo 'Tests passed successfully'
}

def publishArtefacts(){
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

def deployApp(){
    withCredentials([usernamePassword(credentialsId: 'nexus-credentials', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USERNAME')]){
        echo 'Login to Nexus...'
        sh "echo ${NEXUS_PSW} | docker login -u ${NEXUS_USERNAME} --password-stdin ${NEXUS_REPOSITORY_URL}"
        echo 'Pulling image from docker hosted repository on Nexus...'
        sh "docker pull ${NEXUS_REPOSITORY_URL}/${NEXUS_REPOSITORY_ID}/pizza-app:${BUILD_NUMBER}"
    }
}

return this