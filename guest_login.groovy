#!/groovy

pipeline {
    agent any
    //triggers {
      // cron(*/1 * * * *)
    //}
    parameters{
       string(name: 'PERSON', defaultValue: 'Abinaya', description: 'Running from my local branch') 
       choice(name: 'Env', choices: ['Dev', 'QA', 'PROD'], description: 'choose your environment')
       gitParameter(branch: '', branchFilter: '.*', defaultValue: 'master', description: 'enter the branch name', name: 'branch', quickFilterEnabled: false, selectedValue: 'NONE', sortMode: 'NONE', tagFilter: '*', type: 'PT_BRANCH')
       booleanParam(name: 'BUILD', defaultValue: true, description: 'Build the code')
    }
    tools{
        jdk 'java_home'
        maven 'maven_home'
    }
    stages{
        stage('checkout'){
            steps{
                echo "checkout"
                checkout([$class: 'GitSCM', branches: [[name: "${params.branch}"]],
                extensions: [], 
                userRemoteConfigs: [[url: 'https://github.com/AbinayaRavikannan/webapplication.git']]])
        }
        }
        stage('test case'){
            steps{
                echo "test case skipping"
                echo "HELLO ${params.PERSON}"
        }
        }
        stage('code quality'){
            steps{
                echo "test case skipping"
        }
        }
        stage('publish report'){
            steps{
                echo "publish report skipping"
        }
        }
        stage('build'){
            when {
                  expression { return params.BUILD }
            }
            steps{
                    echo "build  ${params.BUILD}"
                    sh 'mvn clean install'
        }
        }
        stage('Publish to artifact'){
            steps{
                echo "Publish to artifact"
        }
        }
        stage('Build docker image'){
            steps{
                echo "Build docker image"
        }
        }
        stage('deploy'){
            steps{
                echo "deploy"
                sh 'cp target/java-tomcat-maven-example.war /var/lib/tomcat/webapps' 
        }
        }
    } 
    post {
        always {
            cleanWs() /* clean up our workspace */
        }
        success {
            echo 'I succeeded!'
        }
        failure {
            echo 'I failed :('
        }
        changed {
            echo 'Things were different before...'
        }
    }
}
