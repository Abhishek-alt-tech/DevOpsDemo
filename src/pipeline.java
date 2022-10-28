import java.text.SimpleDateFormat; 
 import org.apache.commons.codec.digest.DigestUtils;
  import java.io.FileInputStream; 
  import java.io.IOException;
   import java.math.BigInteger;
    import java.security.MessageDigest;
     import java.security.NoSuchAlgorithmException;
      def startDate
        def endDate  
        def fileContents 
         def md5 
          def projectId= "194" 
           def projectName= "EDMSAngularSampleApp"
             def moduleId= "42"  
             def moduleName= "EDMS FOR INT CERTIFICATES" 
              def newBuildId= "3.0.0.${BUILD_ID}"
                def sonarUrl= "https://devopspprod.statebanktimes.in/sonar" 
                 def tomcatCredIdForJenkins= "tomcat"
                   def remote= [:]  remote.name = "node"  remote.host = "*.*.*.*"  remote.allowAnyHosts = true  
                   def tomcatServer= "https://devopsservicespprod.statebanktimes.in"  
                   def CODE =  readJSON text: '{"url":"https://devopspprod.statebanktimes.in/gitlab/root/edmsangularsampleapp.git","scm":"gitlab","branch":"master","credId":"gitlab"}'
                    def BUILD =  readJSON text: '{"command":"mvn package"}'
                     def ANALYSIS =  readJSON text: '{"sonarkey":"edmsangularsampleapp"}'
                      def BINARY =  readJSON text: '{"url":"https://nexuspprod.statebanktimes.in/"}' 
                      def DEPLOYMENT =  readJSON text: '{"contextPath":"EDMSAngularSampleApp"}' 
                      pipeline 
                      { agent any options { skipDefaultCheckout()  }
                       stages { 
                        stage('CODE'){       
                             steps{       
                                 script{ buildName newBuildId }         
                                    git url: CODE.url, credentialsId: CODE.credId, branch: CODE.branch
                                            } }
                                            
                                             stage('UNIT-TEST'){     
                                                 steps{ 
                                                  

 println "Test code ..." //sh 'karma start --single-run --browsers ChromeHeadless karma.conf.js' //sh 'ng test --single-run --browsers ChromeHeadless' sh 'ng test --no-watch --no-progress --browsers=ChromeHeadless'         } } 
stage('Build'){    
     steps{     
        script { startDate = new Date() 
          sh 'npm install'  
        sh 'ng build --output-path target' 
              sh 'cd target && jar cvf build.war .' echo 'after building'     }
                                     }  
                                     post{   always{  
                                       script{   endDate = new Date()         adoptBuildFeedback buildDisplayName: "${projectName}.${newBuildId}",     buildEndedAt: "${endDate}",     buildStartedAt: "${startDate}",     buildUrl: "${BUILD_URL}",        projectId: projectId,         status: "${currentBuild.currentResult}",         buildHashValue: "${md5}"           }   }     } }
        stage('SonarQube analysis') {
          
             steps { println "Building code ..." 
             script{  def scannerHome = tool 'sonarscanner'; withSonarQubeEnv('SonarQube') { //executeCmd("/opt/apache-maven-3.6.3/bin/mvn sonar:sonar"+" "+"-Dsonar.host.url="+ sonarUrl +" "+"-Dsonar.projectKey="+ANALYSIS.sonarkey+" "+"-Dsonar.projectName="+ANALYSIS.sonarkey);
             sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=${ANALYSIS.sonarkey} -Dsonar.projectName=${ANALYSIS.sonarkey}  -Dsonar.sourceEncoding=UTF-8 -Dsonar.sources=src -Dsonar.exclusions=**/node_modules/** -Dsonar.tests=src -Dsonar.test.inclusions=**/*.spec.ts -Dsonar.typescript.lcov.reportPaths=coverage/lcov.info" } } executeCmd("sleep 10")  timeout(time: 1, unit: 'HOURS') { waitForQualityGate abortPipeline: true } }
             post{   success{   script{ adoptCodeAnalysisFeedback  buildDisplayName: "${projectName}.${newBuildId}", buildUrl: "${BUILD_URL}", sonarKey: "${ANALYSIS.sonarkey}", projectId: "${projectId}"   }   } }   }
            
stage('Binary Management'){   
      steps{            
         println "Uploading packaged code ..."           
           script{ withCredentials([usernamePassword(credentialsId: 'nexus_admin', passwordVariable: 'password', usernameVariable: 'username')]) { sh "curl -v -u ${username}:${password} --upload-file  target/*.war '${BINARY.url}repository/jenkins_rel/${moduleName.replaceAll("\\s+","-")}/${projectName}/${projectName}.${newBuildId}.war'" }  }             } } 
stage('Deployment'){    
       steps{               
                 script { sh "whoami" sh "pwd" echo "${BUILD_ID}"     withCredentials([usernamePassword(credentialsId: 'Ansible_SSH', passwordVariable: 'password', usernameVariable: 'userName')]) {         remote.user = userName         remote.password = password                sshCommand remote: remote, command: 'hostname'  sshCommand remote: remote, command: "ansible-playbook /opt/ansible/playbooks/devops/application_onboarding1.yml -i /opt/ansible/inventories/devops -l *.*.*.* --extra-vars  nexus_repository_url='${BINARY.url}repository/jenkins_rel/${moduleName.replaceAll("\\s+","-")}/${projectName}/${projectName}.${newBuildId}.war'"     }          }    }      
                
                post{              
                    always{ 
                      script{   
                        startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date())                                         adoptDevDeploymentFeedback buildDisplayName: "${projectName}.${newBuildId}",                                           deploymentStartedAt: "${startDate}",                                          environment: "DEV",                                           moduleId: moduleId,                                            moduleName: "${moduleName}",                                             projectName: "${projectName}",                                             status: "${currentBuild.currentResult}" } }                              }          } stage('Performance Testing'){ steps{                          sh '/opt/apache-jmeter-5.4/bin/jmeter -Jjmeter.save.saveservice.output_format=xml -n -t /opt/apache-jmeter-5.4/bin/BeanShellSampler.jmx -l /home/jmeter_report/TestResult1.jtl'   perfReport filterRegex: '', sourceDataFiles: '/home/jmeter_report/TestResult1.jtl' } }  
                      /*stage('Selenium test') {                       
                        
                      steps {                         
                            node('Jenkins_windows_slave') {  
                                                         echo "selenium test is running"        
                                                                             git url: CODE.url, credentialsId: CODE.credId, branch: CODE.branch                                                    
                                                                               bat "F:\\Software_DONT_Delete\\apache-maven-3.6.3\\bin\\mvn test";      }   } }*/ } } //Helper Methods void executeCmd(String CMD){ if(isUnix()){ sh "echo linux" sh CMD } else{ bat "echo windows" bat CMD } }

Get Outlook for iOS
