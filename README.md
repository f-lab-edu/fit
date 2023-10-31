# Fit

- 운동시설 및 운동 관련 원데이 클래스를 제공하는 프로젝트입니다.
- 요구사항에 맞게 중복이 없고 명확한 코드를 구현하는 것을 목표로 개발하였습니다.

## 설계

멀티 모듈 설계
- Application, Domain Model, JPA 파트를 멀티 모듈로 나누어 설계하였습니다.
- 모듈간에 의존성을 최대한 가지지 않도록 설계하였습니다.
- Domain Model의 경우 스프링에 종속성이 없습니다.

## 브랜치 관리 전략

트렁크 기반 개발(Trunck-Based Development)
- 메인 브렌치에서 직접 모든 작업을 진행합니다.
- 작업 브랜치에서 Pull Request에 리뷰를 진행한 후 merge를 진행합니다.
- 다른 사람이 리뷰하기 좋은 쿠러리티로 코드 베이스를 유지합니다.


## 테스트
- TDD를 통해 불필요한 설계를 피하고, 요구 사항에 집중하여 중복이 없고 명확한 코드를 작성합니다.

- RestTemplate을 통해 API 사용자 입장에서 클라이언트 테스트 코드를 작성합니다.


## 사용 기술 및 개발 환경

- Java, Spring Boot, Gradle, JPA, H2


## 주요 기능

- [각 기능별 Use Case](https://github.com/f-lab-edu/fit/wiki/Use-Case)
