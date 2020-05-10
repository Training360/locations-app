FROM adoptopenjdk:14-jre-hotspot
RUN mkdir /opt/app
ADD target/locations-app.jar /opt/app/locations-app.jar
CMD ["java", "-jar", "/opt/app/locations-app.jar"]
