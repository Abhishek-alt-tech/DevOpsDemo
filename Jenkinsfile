node {

    def mvnHome = tool 'maven'

    // holds reference to docker image
    def dockerImage
    // ip address of the docker private repository(nexus)
 
    def dockerImageTag = "devopsexample${env.BUILD_NUMBER}"
    
    stage('Clone Repo') { 
      // Get some code from a GitHub repository
      git 'https://github.com/Abhishek-alt-tech/DevOpsDemo.git'
      // Get the Maven tool.

      mvnHome = tool 'maven'
    }    
  
    stage('Build Project') {
      // build project via maven
      sh "'${mvnHome}/bin/mvn' clean install"
    }
		
    stage('Build Docker Image') {
      // build docker image
      dockerImage = docker.build("devopsexample:${env.BUILD_NUMBER}")
    }
   	  
    stage('Deploy Docker Image and login'){
      
      echo "Docker Image Tag Name: ${dockerImageTag}"
	  
        sh "docker images"
        sh "docker login -u abhishekalttech -p Aabhishek@2022"
}
    stage('Docker push'){
        
        sh "docker tag 361590a3dee0   abhishekalttech/myapplication" //must change the name
        sh "docker push   abhishekalttech/myapplication"
  }
}
