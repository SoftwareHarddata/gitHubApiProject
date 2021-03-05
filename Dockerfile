FROM openjdk:15

MAINTAINER REM Java 21 1 <fabian.schmauder@neuefische.de>

ADD backend/target/github-bingo-master.jar app.jar

CMD ["sh", "-c", "java -Dserver.port=$PORT -Dspring.data.mongodb.uri=$MONGODB_URI -Dsecurity.jwt.secret=$JWT_SECRET -jar /app.jar"]
