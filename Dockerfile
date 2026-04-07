# 베이스 이미지
FROM amazoncorretto:17

# 작업 디렉토리
WORKDIR /app

# 파일 복사
COPY . .

# Gradle Wrapper로 빌드
RUN chmod +x gradlew && ./gradlew clean build -x test

# 80포트 노출
EXPOSE 80

# 프로젝트 정보 환경 변수 설정
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
# JVM 환경 변수
ENV JVM_OPTS=""

# 애플리케이션 실행 명령어 설정
CMD ["sh", "-c", "java $JVM_OPTS -jar build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]