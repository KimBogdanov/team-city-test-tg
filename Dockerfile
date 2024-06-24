FROM amazoncorretto:17-alpine-jdk
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]