FROM openjdk:17
EXPOSE 8080
ADD target/spring-boot-carreg.jar spring-boot-carreg.jar
ENTRYPOINT ["java","-jar","/spring-boot-carreg.jar"]