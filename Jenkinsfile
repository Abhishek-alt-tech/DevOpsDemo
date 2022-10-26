node {
    // reference to maven
    // ** NOTE: This 'maven-3.5.2' Maven tool must be configured in the Jenkins Global Configuration.   
    def mvnHome = tool 'maven'

    // holds reference to docker image
    def dockerImage
    // ip address of the docker private repository(nexus)
 
    def dockerImageTag = "devopsexample${env.BUILD_NUMBER}"
    
    stage('Clone Repo') { // for display purposes
      // Get some code from a GitHub repository
      git 'https://github.com/vikas4cloud/DevOps-Example.git'
      // Get the Maven tool.
      // ** NOTE: This 'maven-3.5.2' Maven tool must be configured
      // **       in the global configuration.           
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
