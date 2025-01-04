FROM openjdk:21
EXPOSE 8080
ADD target/ImageProcessor-0.0.1-SNAPSHOT.jar ImageProcessor-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ImageProcessor-0.0.1-SNAPSHOT.jar"]







