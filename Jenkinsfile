def groovy_script
pipeline {
    agent any
    stages {
        stage('init') {
            steps{
                script { 
                    groovy_script = load "/var/jenkins_home/pipeline.groovy"
                }
            }
        }
        stage('checkout') {
            steps {
                script { 
                    groovy_script.checkoutRepository()
                }
            }
        }
        stage('test') {
            steps {
                script {
                    groovy_script.testApp()
                }
            }
        }
        stage('publish') {
            steps {
                script {
                    groovy_script.publishArtefacts()
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                    groovy_script.deployApp()
                }
            }
        }
    }
}