# This is a sample project to build a [Gatling](https://gatling.io) application test as a Java JAR application (not a JUnit test)





#### Requirements:

1. JDK v8
2. Maven v3.x

#### Build the test as a jar file:

- `mvn clean install` 

#### Test run:

- `java -jar target/Gattling-JAR-1.0-SNAPSHOT.jar -s OtusHw3`

Where instead of `OtusHw3` you have to give the name of your test script from the folder `src/main/scala/`

#### Test result:

- After a successful test run, the result of its execution will be located in the `results/{your_test_name}-{date_and_time_start}/index.html`





#### Notes:

- Before creating a new test, I recommend updating all dependencies in the `pom.xml` file.
- The main entry point in the Jar file is already specified in the pom.xml file and does not need to be specified when running the compiled application.

#### Useful links:

- https://github.com/satsie/gatling-jar-example
- https://github.com/gatling/gatling-maven-plugin-demo
- https://stackoverflow.com/questions/27893423/build-executable-jar-for-gatling-load-test