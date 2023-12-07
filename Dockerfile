FROM maven:3.3.9-jdk-8 as build

WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn install -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
WORKDIR /app
VOLUME /tmp
ARG TARGET=/app/target
COPY --from=build ${TARGET}/lib /app/lib
COPY --from=build ${TARGET}/classes .


EXPOSE 8080
ENTRYPOINT ["java","-cp","/app:/app/lib/*","com.interview.jotd.Main"]
