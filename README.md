# Computer Database

## Installation notes

### Web application

In order to work, the application needs to be able to connect to a database server. You must provide a copy of `persistence.properties` in the web application classpath. Check out the *computer-database-persistence* module for a sample configuration file.

### Web Service

Just like the web application, the web service server needs to have access to a database server. You must provide a copy of `persistence.properties` in the application classpath.

### Console application

The console application of the Computer Database project has a configuration file called `console-config.properties`. Be sure to add this file to the classpath of the application.

### All

The application won't work if you do not provide a `config.properties` file in the classpath. Please see the *computer-database-core* module for more information about this file.

