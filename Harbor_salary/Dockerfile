FROM openjdk:11 as stage1

WORKDIR /Harbor_salary

# gradle 빌드시에 필요한 파일들을 workdir로 copy
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Gradle Wrapper 스크립트에 실행 권한을 부여
RUN chmod +x gradlew

# /app/build/libs/*.jar
RUN ./gradlew bootJar

FROM openjdk:11

WORKDIR /Harbor_salary

# 빌드된 JAR 파일을 복사
COPY --from=stage1 /Harbor_salary/build/libs/*.jar Harbor_salary.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "Harbor_salary.jar"]