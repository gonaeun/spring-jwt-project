# 🔐spring-jwt-project
이 프로젝트는 **Spring Boot 기반 JWT 인증/인가 시스템**을 구현하고,  
**Swagger API 문서화** 및 **AWS EC2 배포**까지 완료한 백엔드 개발 과제입니다.  

---

## 📖 개요
- 프로젝트 형태 : 1인 프로젝트
- 진행 기간 : 2025.08.19 ~ 2025.08.21

---

## 🔑 주요 기능
- 회원가입 (User/Admin)
- 로그인 (JWT 발급)
- JWT 기반 인증/인가 처리
- ROLE 기반 접근 제어 (USER / ADMIN)
- 관리자 권한 부여 API
- JUnit5 기반 테스트 코드
- Swagger UI 기반 API 문서화
- AWS EC2 배포

---

## ⚙️ 기술 스택
- **Language**: Java 17 이상
- **Framework**: Spring Boot 3.x
- **Build Tool**: Gradle 8.x 이상
- **Security**: Spring Security, JWT
- **Documentation**: Swagger (springdoc-openapi)
- **Testing**: JUnit5, MockMvc
- **Infra**: AWS EC2 (Ubuntu), OpenJDK 17

---

## 🚀 실행 방법
### 사전 요구사항
- Java 17 이상
- Gradle 8.x 이상

### 로컬 실행

1. 저장소 클론
```bash
git clone https://github.com/gonaeun/spring-jwt-project.git
cd spring-jwt-project
```
2. 프로젝트 빌드
```bash
./gradlew clean build
```

3. 애플리케이션 실행
```bash
java -jar build/libs/spring-jwt-project-0.0.1-SNAPSHOT.jar
```

---

## 📖 API 엔드 포인트
- **Swagger UI**: [http://54.252.161.213:8080/swagger-ui/index.html](http://54.252.161.213:8080/swagger-ui/index.html)
### 회원가입
- **POST** `/signup`  
- 요청: `username`, `password`, `nickname`  
- 응답: `username`, `nickname`, `기본 Role(USER)`

### 로그인
- **POST** `/login`  
- 요청: `username`, `password`  
- 응답: `JWT Access Token`

### 관리자 권한 부여
- **PATCH** `/admin/users/{userId}/roles`  
- 요청: `userId(PathVariable)`  
- 응답: 해당 유저의 Role을 ADMIN으로 변경  
- 접근 제한: ADMIN Role 필요

---

## 🔒 보안 설계
- 로그인 성공 시 JWT Access Token 발급
- 토큰 만료 시간 설정 (2시간)
- Spring Security 필터 적용
  - Authorization Header: `Bearer <token>`
  - 서명 검증, 만료 검증, 권한(Role) 확인
- Role 기반 접근 제어
  - USER : 기본 기능 접근
  - ADMIN : 관리자 권한 API 접근 가능

---

## 🧪 테스트 항목
- 회원가입: 성공 / 실패(중복된 사용자)
- 로그인: 성공 / 실패(잘못된 계정 정보)
- 관리자 권한 부여:
  - 관리자 요청 시 성공
  - 일반 사용자 요청 시 접근 거부 (ACCESS_DENIED)
  - 존재하지 않는 사용자 요청 시 오류 (USER_NOT_FOUND)
 
📌 각 테스트 결과는 Postman으로 검증되었으며, 테스트 화면은 GitHub Pull Request(PR)에 첨부됨

---
## 🏗️ 프로젝트 구조
```bash
src
 ├─ main
 │   ├─ java/com/gonaeun/springjwtproject
 │   │   ├─ common
 │   │   │   ├─ exception     # 예외 처리
 │   │   │   ├─ response      # 공통 응답 DTO
 │   │   │   └─ util          # JWT 유틸
 │   │   ├─ config            # 보안 및 Swagger 설정
 │   │   ├─ controller        # API 엔드포인트
 │   │   ├─ domain            # User, Role 엔티티
 │   │   ├─ dto
 │   │   │   ├─ request       # 요청 DTO
 │   │   │   └─ response      # 응답 DTO
 │   │   ├─ repository        # 메모리 저장소
 │   │   ├─ security          # JWT 필터 및 Provider
 │   │   ├─ service           # 비즈니스 로직
 │   │   └─ SpringJwtProjectApplication
 │   └─ resources             # 설정 파일
 └─ test/java                 # JUnit 테스트 코드
```

---

## 🌟 주요 특징

### 1. Spring Security 통합
- `@PreAuthorize("hasRole('ADMIN')")` 어노테이션을 활용한 접근 제어
- 관리자 권한이 필요한 엔드포인트(`/admin/users/{userId}/roles`) 보호
- 사용자(Role)에 따른 API 접근 제어 적용

### 2. JWT 기반 인증
- 로그인 성공 시 JWT Access Token 발급
- 토큰 만료 시간 설정 (2시간)
- Custom JWT Filter를 통한 토큰 검증 및 SecurityContext 반영
- Authorization Header(`Bearer <token>`) 기반 인증 처리

### 3. 예외 처리
- GlobalExceptionHandler를 통한 전역 예외 처리
- CustomException 및 ErrorCode를 정의하여 일관된 에러 응답 제공
- 에러 발생 시 JSON 형식의 표준 응답 반환

### 4. 도메인 중심 설계
- User, Role 중심으로 엔티티 및 서비스 구성
- DTO를 통한 요청/응답 데이터 분리
- Repository, Service, Controller 계층 구조 명확히 구분
- security 패키지를 분리하여 JWT Provider, Filter 등 보안 로직을 독립적으로 관리
- common 패키지를 도입하여 전역 예외 처리, 통일된 응답 포맷을 통해 일관성 있는 운영

### 5. 문서화 및 테스트
- Swagger UI 연동으로 API 문서 자동화
- JUnit5 + MockMvc를 통한 API 단위 테스트 구현

### 6. 배포
- AWS EC2(Ubuntu) 환경에 배포
- OpenJDK 17 기반 실행 (`java -jar`)
- 외부에서 Swagger UI를 통해 API 접근 가능

---

## ☁️ 배포 정보
- **AWS EC2 인스턴스**: Ubuntu 기반
- **JDK 설치**: OpenJDK 17
- **실행 명령어**
  ```bash
  java -jar spring-jwt-project-0.0.1-SNAPSHOT.jar
  ```
- **기본 서버 주소 및 포트**: 0.0.0.0:8080 (가이드라인 준수)
- **실행 중인 서비스**: [http://54.252.161.213:8080](http://54.252.161.213:8080)
- **Swagger UI**: [http://54.252.161.213:8080/swagger-ui/index.html](http://54.252.161.213:8080/swagger-ui/index.html)
  
⚠️ 실제 API 확인은 Swagger UI를 통해 가능합니다.

