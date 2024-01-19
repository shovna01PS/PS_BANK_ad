pipeline {
    agent any
	tools {
        // Use the Maven installation defined in the tool configuration
        maven 'maven'
    }
    stages {
        // stage('Git Checkout') {
        //     steps {
        //         git url: 'https://github.com/rchidana/calcwebapp.git'    
		      //       echo "Code Checked-out Successfully!!";
        //     }
        // }
        
        stage('Package') {
            steps {
                bat 'mvn package'    
		            echo "Maven Package Goal Executed Successfully!";
            }
        }
        
        // stage('JUNit Reports') {
        //     steps {
        //             junit 'target/surefire-reports/*.xml'
		      //           echo "Publishing JUnit reports"
        //     }
        // }
        
        // stage('Jacoco Reports') {
        //     steps {
        //           jacoco()
        //           echo "Publishing Jacoco Code Coverage Reports";
        //     }
        // }

	stage('SonarQube analysis') {
            steps {
		// Change this as per your Jenkins Configuration
                withSonarQubeEnv('SonarQube') {
                    bat 'mvn package sonar:sonar'
                }
            }
        }

	stage("Quality gate") {
    steps {
        script {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
                error "Quality Gate failed: ${qg.status}"
            }
        }
    }
}
        
    }
    post {
        
        success {
            echo 'This will run only if successful'
        }
        failure {
            echo 'This will run only if failed'
        }
    
    }
}
