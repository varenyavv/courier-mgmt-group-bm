FROM docker.repo1.uhc.com/eclipse-temurin:11
COPY build/libs/couriermgmt-*.jar couriermgmt.jar
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar couriermgmt.jar