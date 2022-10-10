# 
# build
#
from maven:3.8.3-openjdk-17 as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true

# 
# package
#
FROM openjdk:17-jdk-slim-buster
COPY --from=build /home/app/target/nft.metadata-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
ENTRYPOINT [ "java","-jar","/usr/local/lib/app.jar" ]