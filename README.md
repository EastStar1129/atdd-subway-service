<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.


## 인수 테스트 기반 리펙터링 (STEP 1)
### 기능 요구사항 
- [X] LineService 리팩터링
- [X] (선택) LineSectionAcceptanceTest 리팩터링

- LineService의 비즈니스 로직을 도메인으로 옮기기
- 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
- 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

### 힌트 (STEP 1)
#### 1. Domain으로 옮길 로직을 찾기
- 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
- 객체지향 생활체조를 참고

#### 2. Domain의 단위 테스트를 작성하기
- 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
- SectionsTest나 LineTest 클래스가 생성될 수 있음

#### 3. 로직을 옮기기
- 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
- 정상 동작 확인 후 기존 로직 제거

#### (선택) 인수 테스트 통합
- API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
- 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음

``` 
인수 조건 예시
Feature: 지하철 구간 관련 기능

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음

  Scenario: 지하철 구간을 관리
    When 지하철 구간 등록 요청
    Then 지하철 구간 등록됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
    When 지하철 구간 삭제 요청
    Then 지하철 구간 삭제됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
```

## 경로 조회 기능 (STEP 2)
### 기능 요구사항
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기
* jgrapht 라이브러리 사용
* mock 서버와 dto를 정의하여 인수 테스트 성공 시키기

### 예외사항
- [x] 출발역과 도착역이 같은 경우
- [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
- [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우

### Outside In 경우
* 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
* 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
* 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
* Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)

### Inside Out 경우
* 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
* 해당 도메인의 단위 테스트를 통해 도메인의 역할과 경계를 설계
* 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작

### Request
``` Request 
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

### Response
``` Response
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 09 May 2020 14:54:11 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "stations": [
        {
            "id": 5,
            "name": "양재시민의숲역",
            "createdAt": "2020-05-09T23:54:12.007"
        },
        {
            "id": 4,
            "name": "양재역",
            "createdAt": "2020-05-09T23:54:11.995"
        },
        {
            "id": 1,
            "name": "강남역",
            "createdAt": "2020-05-09T23:54:11.855"
        },
        {
            "id": 2,
            "name": "역삼역",
            "createdAt": "2020-05-09T23:54:11.876"
        },
        {
            "id": 3,
            "name": "선릉역",
            "createdAt": "2020-05-09T23:54:11.893"
        }
    ],
    "distance": 40
}
```


### 인증 (step3)
- [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [x] 인증 - 내 정보 조회 기능 완성하기
- [x] 토큰 발급 인수 테스트
- [x] 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
- [x] AuthAcceptanceTest 인수 테스트 만들기

### 인수 조건
```text
인수 조건
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
```

### request
```text
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```

### response
```text
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 27 Dec 2020 04:32:26 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY"
}
```

### 즐겨 찾기 기능 구현하기
- [x] 인증 - 즐겨 찾기 기능 완성하기
- [x] 즐겨찾기 기능을 완성하기
- [x] 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

### 인수 조건
```
Feature: 즐겨찾기를 관리한다.

Background
Given 지하철역 등록되어 있음
And 지하철 노선 등록되어 있음
And 지하철 노선에 지하철역 등록되어 있음
And 회원 등록되어 있음
And 로그인 되어있음

Scenario: 즐겨찾기를 관리
When 즐겨찾기 생성을 요청
Then 즐겨찾기 생성됨
When 즐겨찾기 목록 조회 요청
Then 즐겨찾기 목록 조회됨
When 즐겨찾기 삭제 요청
Then 즐겨찾기 삭제됨
```

### 생성 요청/응답
``` text
POST /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: */*
content-type: application/json; charset=UTF-8
content-length: 27
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
{
    "source": "1",
    "target": "3"
}

HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Sun, 27 Dec 2020 04:32:26 GMT
Location: /favorites/1
```

### 목록 조회 요청/응답
``` text
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: application/json
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 27 Dec 2020 04:32:26 GMT
Keep-Alive: timeout=60
Connection: keep-alive

[
    {
        "id": 1,
        "source": {
            "id": 1,
            "name": "강남역",
            "createdDate": "2020-12-27T13:32:26.364439",
            "modifiedDate": "2020-12-27T13:32:26.364439"
        },
        "target": {
            "id": 3,
            "name": "정자역",
            "createdDate": "2020-12-27T13:32:26.486256",
            "modifiedDate": "2020-12-27T13:32:26.486256"
        }
    }
]
```

### 삭제 요청/응답
``` text
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: */*
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate

HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Date: Sun, 27 Dec 2020 04:32:26 GMT

``` 

### 요금조회 (step4)
- [ ] 경로 조회 시 거리 기준 요금 정보 포함하기
- [ ] 노선별 추가 요금 정책 추가
- [ ] 연령별 할인 정책 추가

### 힌트
#### 거리별 요금
``` text
거리별 요금 정책
기본운임(10㎞ 이내) : 기본운임 1,250원
이용 거리초과 시 추가운임 부과
10km초과∼50km까지(5km마다 100원)
50km초과 시 (8km마다 100원)
```

#### 수정된 인수 조건
```text
Feature: 지하철 경로 검색

  Scenario: 두 역의 최단 거리 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
    Then 최단 거리 경로를 응답
    And 총 거리도 함께 응답함
    And ** 지하철 이용 요금도 함께 응답함 **
```

#### 노선별 추가 요금 정책
```text
노선에 추가 요금 필드를 추가
추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

로그인 사용자의 경우 연령별 요금 할인 적용
청소년: 운임에서 350원을 공제한 금액의 20%할인
어린이: 운임에서 350원을 공제한 금액의 50%할인
- 청소년: 13세 이상~19세 미만
- 어린이: 6세 이상~ 13세 미만
```

#### /paths 요청 시 LoginMember 객체 처리
- 로그인 시 LoginMember 객체 를 활용하여 연령별 요금 할인을 적용할 수 있음
- 비 로그인 시 LoginMember는 비어있는 객체가 넘어가므로 별도의 처리가 필요함
- 필요 시 아래 구문에서 null object를 리턴해주는 부분을 예외를 던지도록 수정해도 무방함

``` java
    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new LoginMember(); // <--- 이 부분 변경 가능
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

```

#### 5km 마다 100원 추가 로직
``` java
    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
```