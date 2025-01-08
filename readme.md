# WebFlux Payment Server
https://github.com/user-attachments/assets/6db6132c-0fb1-4b0b-942e-5f8ef11ce4e7
## 프로젝트 개요
WebFlux Payment Server는 토스페이먼츠 SDK를 활용하여 결제 플로우를 구현한 프로젝트입니다. 주요 기능으로는 주문을 생성하고 해당 정보를 기반으로 결제를 승인 처리하는 기능을 제공합니다.

## 주요 기능
주문 생성: 사용자 주문 정보를 데이터베이스에 저장.
결제 승인 처리: 생성된 주문 정보를 기반으로 결제를 승인.

## 기술 스택

### 서버
- Kotlin: 서버 애플리케이션 구현.
- Spring WebFlux: 비동기 논블로킹 서버 구현.
- R2DBC: 리액티브 데이터베이스 드라이버.
- PostgreSQL: 데이터베이스.

### 클라이언트
- React: 사용자 인터페이스 구현.

## 설치 및 실행
1. PostgreSQL 데이터베이스 구성
   1. PostgreSQL을 설치합니다.
   2. 아래 정보에 따라 payment라는 이름의 데이터베이스를 생성합니다:
   ```text
   URL: r2dbc:postgresql://localhost:5432/payment
   Username: postgres
   Password: 1234
   ```
   `CREATE DATABASE payment;`
2. 서버 실행
   1. 프로젝트 루트 디렉토리에서 다음 명령어를 실행합니다: `./gradlew bootRun`
3. 클라이언트 실행
   1. 프로젝트 루트 디렉토리에서 front 디렉토리로 이동합니다: `cd front`
   2. 의존성을 설치합니다: `npm install`
   3. 애플리케이션을 실행합니다: `npm start`
   4. 브라우저에서 `http://localhost:3000` 으로 접속하여 애플리케이션을 확인합니다.

## 기타 사항
데이터베이스 정보는 application.yml에 저장되어 있습니다. 필요에 따라 적절히 수정해 주세요.