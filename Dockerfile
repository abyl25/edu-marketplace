# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="abylay.tastanbekov@nu.edu.kz"

# Add a volume pointing to /tmp
#VOLUME /tmp

# Make port 8080 available to the world outside this container
#EXPOSE 8080

# Add the application's jar to the container
ADD target/education-platform-0.0.1-SNAPSHOT.jar.original spring-app.jar

# Run the jar file 
ENTRYPOINT ["java", "-jar", "spring-app.jar"]
