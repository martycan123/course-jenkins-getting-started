pipeline {
    agent any

    triggers { pollSCM('* * * * *') }

    stages {
        stage('Checkout'){
            steps {
                git branch: 'main', url: 'https://github.com/martycan123/jgsu-spring-petclinic.git'
            }
        }
        stage('Build') {
            steps {
                //git branch: 'main', url: 'https://github.com/martycan123/jgsu-spring-petclinic.git'

                // Run Maven on a Unix agent.
                //sh "mvn -Dmaven.test.failure.ignore=true clean package"
                sh "./mvnw clean package"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
                changed{
                    emailext subject: 'Job \'${JOB_NAME}\' (${BUILD_NUMBER}) is waiting for input',
                    body: 'Please go to ${BUILD_URL} and verify the build',
                    attachLog: true,  
                    compressLog: true, 
                    to: "test@jenkins",
                    recipientProviders: [upstreamDevelopers(), requestor()]
                }
                
            }
        }
    }
}
