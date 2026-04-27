FROM eclipse-temurin:8-jdk-jammy as builder

ENV GRAILS_VERSION=2.4.2
RUN apt-get update && apt-get install -y unzip curl && \
    curl -L https://github.com/apache/grails-core/releases/download/v${GRAILS_VERSION}/grails-${GRAILS_VERSION}.zip -o grails.zip && \
    unzip grails.zip && \
    mv grails-${GRAILS_VERSION} /opt/grails

ENV PATH="/opt/grails/bin:${PATH}"

WORKDIR /app

COPY . .

RUN grails war

ENV TOMCAT_VERSION=8.5.96
RUN apt-get update && apt-get install -y curl && \
    curl -O https://archive.apache.org/dist/tomcat/tomcat-8/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    tar xvf apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    mv apache-tomcat-${TOMCAT_VERSION} /opt/tomcat

RUN rm -rf /opt/tomcat/webapps/*

COPY --from=builder /app/target/*.war /opt/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["/opt/tomcat/bin/catalina.sh", "run"]