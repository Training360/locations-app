FROM adoptopenjdk:14-jre-hotspot as builder
WORKDIR application
COPY target/*.jar locations-app.jar
RUN java -Djarmode=layertools -jar locations-app.jar extract

FROM adoptopenjdk:14-jre-hotspot
WORKDIR application
RUN  apt-get update \
     && apt-get install wget \
     && apt-get install -y netcat \
     && wget https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
     && chmod +x ./wait-for-it.sh
COPY --from=builder application/dependencies/ ./
RUN true
COPY --from=builder application/spring-boot-loader/ ./
RUN true
COPY --from=builder application/snapshot-dependencies/ ./
RUN true
COPY --from=builder application/application/ ./
RUN true
ENTRYPOINT ["java", \
  "org.springframework.boot.loader.JarLauncher"]

