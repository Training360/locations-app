FROM eclipse-temurin:17 as builder
WORKDIR application
COPY target/*.jar locations-app.jar
RUN java -Djarmode=layertools -jar locations-app.jar extract

FROM eclipse-temurin:17
WORKDIR application
RUN curl https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh -s -o ./wait-for-it.sh && chmod +x ./wait-for-it.sh
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
# COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

