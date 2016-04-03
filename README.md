example-jee-user-registration: Example Using Multiple Java EE 7 Technologies Deployed as an EAR
==============================================================================================
Author: Fredy Setz
Technologies: JSF 2.2, CDI 1.1, EJB 3.2, JPA 2.1, REST
Target Application Server: WildFly
Source: <https://github.com/wildfly/quickstart/>


What is it?
-----------

It is a sample Java EE application, which can be run on JBoss Wildfly Application Server. This project was tested with JBoss Wildfly 10.0.0.final.

This project allows a user to register. When the User is registered a validation email is sent to his email address. In the email the new member will find a validation URL where he can validate his email address.

The Data will be stored in memory with a H2 Datasource.


How to run it?
--------------
All you need to do is: 
-Install Java 7.0 (Java SDK 1.7) or better
-Install Maven 3.1 or better
-Download JBoss Wildfly and unpack it to a folder of your choice.
-Configure JBOSS_HOME environment varible accordingly.
-Configure mail-smtp on JBoss Wildfly 
-Start JBoss WildFly
-Checkout the git source of this project
-Build and Deploy the Enterprise Application
-See How to use the Enterprise Application


Configure mail-smtp
-------------------
the mail service need to be setup under the following jndi name java:jboss/mail/gmail. 
See this nice blogpost how you can do that http://khozzy.blogspot.ch/2013/10/how-to-send-mails-from-jboss-wildfly.html
 

Start JBoss WildFly with the Web Profile
----------------------------------------
1. The JBOSS_HOME is set correctly.
2. Open a command line and navigate to the root of the JBoss server directory.
3. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Application
--------------------------------
There is WildFly Maven Plugin (wildfly-maven-plugin) which will deploy the ear.

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/example-jee-user-registration.ear` to the running instance of the server.

For more details about the deployment see: https://docs.jboss.org/wildfly/plugins/maven/latest/


How to use the application 
--------------------------

The application can be accessed unter the following URL: <http://localhost:8080/example-jee-user-registration-web>.

1. Enter data into
	-First name(2-50 characters)
	-Last name(2-50 characters)
	-Email address
	-Password
	-At least one phone nubmer
and click the _Register_ button. Both name fields and password must have lower and upper case letters. The password must have at least one special character @#$%!&. 
2. If the data entered is valid, the new member will be registered and added to the _Members_ display list.
3. If the data is not valid, you must fix the validation errors and can try to add the member again.
4. When the registration is successful, the new member will receive an email.
5. Use the URL from your received email to validate the email address of the new user.
6. Reload the start page. You will see that the email address of the new user is now validated.


Configure Arquillian Tests
--------------------------
If you like to receive an email every Time the Arquillian Tests are executed. Just replace the value of the constant INSERT_HERE_YOUR_EMAIL_ADDRESS in Class org.example.jee.test.MailTest


Additional Maven Commands for Developers
----------------------------------------
Execute all commands from root directory of this project.


Only build the Application:

	mvn clean install	For building the application 

Import Java source code to eclipse (Requires you to first build the application):

	mvn eclipse:clean
	mvn eclipse:eclipse


Run the arquillian Tests (Make sure you have started the JBoss Server as described above):

	mvn clean test -Parq-wildfly-remote


Download source and javadoc:

	mvn dependency:sources
	mvn dependency:resolve -Dclassifier=javadoc
	
Undeploy the Archive (Make sure you have started the JBoss Server as described above):

        mvn wildfly:undeploy