# Entry-BE-Assignment

### 프로젝트 소개

1. spring Boot 기반으로 RESTful API 서버 및 gRPC 서버를 구축하였습니다.
2. server A(자원 서버)와 server B(인증 서버) 모듈을 각각 분리하여 개발하였습니다.

<br>

## 💻 개발환경
- **Version** : Java 17
- **IDE** : IntelliJ
- **Framework** : SpringBoot 3.3.3
- **ORM** : JPA

<br>
<br>


## ✨ Entity Diagram
<img width="767" alt="image" src="https://github.com/user-attachments/assets/7df3a91c-cc42-4614-aaf6-0b8b1c46febe">

<br>
<br>

## 👉Quick Start

### 1. Git Clone
```shell
git clone https://github.com/TirTir/Entry-BE-Assignment.git
```
   
### 2. application.properties
`application-serverA.properties`, `application-serverB.properties` 파일을 각 서버 resource 디렉토리 내에 넣어주세요.

### 3. Run Application

<br>

## 📌 주요 기능

### 유저 회원가입 및 로그인

> 회원가입 요구 사항
> 
  ```json
  {
    "userName": "필수 입력값 (2~8자)",
    "password": "영문자, 특수문자, 숫자를 포함한 8자 이상 20자 이하",
    "role": "ADMIN 또는 USER"
  }
  ```
- 아이디 중복 검사: 중복되지 않는 아이디로만 회원가입이 가능합니다.
- 역할(role) 설정: ADMIN은 판매자, USER는 구매자로 역할이 설정됩니다.
- 로그인: 등록된 userName과 password를 통해 로그인을 진행하며, 성공 시 Access Token 및 Refresh Token 이 발급됩니다.

<br>

### Grpc를 통한 JWT 토큰 인증
> AuthResponse 예시
  ```json
 {
  "isValid": true,
  "username": "user123",
  "role": "USER"
}
  ```

</t>**gRPC 통신**

- A 서버는 B 서버에 Access Token을 전송합니다.
- B서버는 해당 토큰의 유효성을 검사한 후 사용자 정보를 전송합니다.

<br>

### 상품 구매 및 판매
- 인증된 사용자 정보: gRPC 통신을 통해 받은 사용자 정보를 Spring Security Context에 저장하여 사용합니다.
- 주문 번호 생성: 날짜, 상품 ID, 랜덤 숫자를 조합하여 고유한 주문 번호를 생성합니다.
- 주문 상태 설정: 사용자의 역할(Role)에 따라 기본 주문 상태(
`USER: ORDER_PLACED`
`ADMIN: ORDER_RECEIVED`
)를 자동으로 설정합니다.

<br>

### 주문 상세 조회 및 페이징 처리
- Invoice 타입 유효성 검사: 사용자의 역할(Role)에 따라 Invoice 타입(
`ADMIN: 판매(SALE)`
`USER: 구매(PURCHASE)`
을 검증합니다.
- 주문 내역 필터링: 사용자의 주문 목록을 날짜와 페이지네이션 정보를 기준으로 필터링하여 반환합니다.
  ```json
      {
        "success": true,
        "message": "Success to search invoices",
        "data": [
          {
          },
        ],
        "paginationLinks": {
        "first": "/api/order?offset=0&limit=10",
        "last": "/api/order?offset=10&limit=10",
        "next": "/api/order?offset=10&limit=10",
        "prev": null
        }
    }
  ```

<br>

## 🖋 API 명세서
* `Postman`
  * https://documenter.getpostman.com/view/27206141/2sAXqmB5iC

<br>

## 📖 Commit Message Convention
- Feat : 기능 추가
- Add : 코드 추가 ( 어떠한 기능 내에 기능을 더 추가할 때 )
- Modify : 코드 수정 ( 버그 등 수정하는 모든 과정들 )
- Delete : 코드 삭제

<br>
<br>
