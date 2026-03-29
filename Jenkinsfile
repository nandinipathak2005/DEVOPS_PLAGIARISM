pipeline {

    agent any

    stages {

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Member 5 - Async & Delta Tests') {
            steps {
                echo 'Running unit tests for AsyncJobService and DeltaAnalysisService...'
                bat 'mvn test -Dtest="DeltaAnalysisServiceTest,AsyncJobServiceTest" -Dsurefire.failIfNoSpecifiedTests=false'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
                success {
                    echo 'All async/delta tests passed.'
                }
                failure {
                    echo 'Tests failed - check surefire report.'
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

    }

}