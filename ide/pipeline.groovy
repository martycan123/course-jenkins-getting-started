pipeline {
    agent any

    //tools {
    //    // Install the Maven version configured as "M3" and add it to the path.
    //    maven "M3"
    //}

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
            }
        }
    }
}
