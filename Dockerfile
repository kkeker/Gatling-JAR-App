FROM openjdk:8-jdk-alpine AS builder
ARG MVN_VER=3.6.3
RUN wget https://downloads.apache.org/maven/maven-3/${MVN_VER}/binaries/apache-maven-${MVN_VER}-bin.zip -O /tmp/maven.zip && unzip /tmp/maven.zip -d /opt/ && rm -f /tmp/maven.zip && export PATH=$PATH:/opt/apache-maven-${MVN_VER}/bin/
COPY . /app
WORKDIR /app
RUN /opt/apache-maven-${MVN_VER}/bin/mvn clean install

FROM openjdk:8-jre-alpine AS runner
ARG TEST_NAME=OtusHw3
COPY --from=builder /app/target/Gattling-JAR-1.0-SNAPSHOT.jar /app/Gattling.jar
WORKDIR /app
RUN java -jar Gattling.jar -s ${TEST_NAME}

FROM nginx:stable-alpine
COPY --from=runner /app/results/*/ /usr/share/nginx/html/
