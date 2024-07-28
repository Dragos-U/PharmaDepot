FROM openjdk:17-jdk
WORKDIR /app
COPY target/PharmaDepot-0.0.1-SNAPSHOT.jar ./pharmadepot.jar
EXPOSE 443
CMD ["java","-jar","pharmadepot.jar"]