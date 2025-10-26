# Redis 학습

## 로컬에서 Redis 설치
- homebrew 설치
  - /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
  - 설치 확인
    - brew --version
- redis 설치
  - brew install redis
  - 설치 확인
    - brew services info redis
- redis 실행
  - brew services start redis!
  - [스크린샷 2025-10-26 오후 12.58.25.png](../../../../../var/folders/lv/jf0s4s1522j_qsk544z_6lpw0000gn/T/TemporaryItems/NSIRD_screencaptureui_jmq3Dw/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-10-26%20%EC%98%A4%ED%9B%84%2012.58.25.png)
- redis 중단
  - brew services stop redis
- redis-cli 접속
  - 터미널에서 redis-cli 입력
- ping 테스트 
  - 응답 PONG 확인

## Redis 기본 명령어
### 키-값 설정
- SET key value
  - 예: set jkjeong:name "jkjeong"
### 키-값 조회
- GET key
  - 예: get jkjeong:name
  - 없을 경우 응답 (nil)
### 키 목록 확인
- KEYS pattern
  - 예: keys *
### 키 삭제
- DEL key
  - 예: del jkjeong:name
### ttl 키-값 설정
- EX key seconds
  - 예: set jkjeong:pet dog ex 30
### ttl 조회
- TTL key
  - 예: ttl jkjeong:pet
- 만료된 경우
  - 응답 -2
- ttl 설정 안된 경우
  - 응답 -1
### 모든 키 삭제
- flushall

## Redis Key 네이밍 컨벤션
### 콜론(:) 활용해 계층적으로 의미를 구분해서 사용
- users:100:profile
  - 사용자들(users)중에서 PK가 100인 사용자(user)의 프로필(profile)
- products:123:details
  - 상품들(products)중에서 PK가 123인 상품(product)의 상세정보(details)

### 컨벤션 장점
1. 가독성: 데이터의 의미와 용도를 쉽게 파악할 수 있다.
2. 일관성: 컨벤션을 따름으로써 코드의 일관성이 높아지고 유지보수가 쉬워진다.
3. 검색 및 필터링 용이성: 패턴 매칭을 사용해 특정 유형의 Key를 쉽게 찾을 수 있다.
4. 확장성: 서로다른 Key와 이름이 겹쳐 충돌할 일이 적어진다.

## 데이터 캐싱 전략 (Cache Aside, Write Around)
### Cache Aside 패턴
캐시에서 데이터를 확인하고, 없다면 DB를 통해 조회해오는 방식
1. 캐시에 데이터가 있을 경우 (= Cache Hit)
   - 캐시에서 데이터를 읽어옴
   - 애플리케이션에 데이터 반환
2. 캐시에 데이터가 없을 경우 (= Cache Miss)
   - 데이터베이스에서 데이터를 읽어옴
   - 애플리케이션에 데이터 반환
   - 읽어온 데이터를 캐시에 저장

### Write Around 패턴
데이터를 어떻게 쓸 지에 대한 전략  
Write Around 전략은 쓰기 작업(저장, 수정, 삭제)을 캐시에는 반영하지 않고, DB에만 반영하는 방식을 뜻한다.

### Cache Aside, Write Around 전략의 한계점 / 해결 방법
- Cache Aside, Write Around 전략의 한계점
  1. 캐시 데이터와 DB 데이터가 일치하지 않을 수 있다. -> 데이터의 일관성을 보장할 수 없다.
  - Write Around 전략에서는 캐시가 최신 상태를 반영하지 않으므로, 자주 변경되는 데이터에 대해 캐시의 효율성이 떨어질 수 있다.
  2. 캐시에 저장할 수 있는 공간이 비교적 작다.
  - DB는 디스크에 저장해서 많은 양을 저장하기 용이하지만, 캐시는 메모리(RAM)에 저장하기 때문에 저장 공간이 제한적이다.
- 해결 방법
  - 데이터 조회 성능 개선 목적으로 레디스를 쓰는 경우에는 데이터의 일관성을 포기하고 성능 향상을 택한 것이다.
  - 캐시를 적용시키기에 적절한 데이터
    - 자주 조회되는 데이터
    - 잘 변하지 않는 데이터
    - 실시간으로 정확하게 일치하지 않아도 되는 데이터
  - 적절한 주기로 데이터를 동기화 시켜주어야 한다.
    - 레디스의 TTL 기능을 활용해 일정 시간이 지나면 캐시 데이터를 자동으로 삭제되도록 설정
    - 애플리케이션 레벨에서 주기적으로 캐시를 갱신
  - 캐시에 저장할 수 있는 공간이 비교적 작으므로 TTL 기능을 활용하면 캐시의 공간을 효율적으로 쓸 수 있다.

## 캐싱으로 조회 성능 개선을 하기 전 SQL 튜닝을 먼저 고려
- 추가적인 시스템 구축은 금전적, 시간적 비용이 추가적으로 발생.  
- SQL 자체가 비효율적으로 작성됐다면 아무리 시스템적으로 성능을 개선한다고 하더라도 한계가 있다.

## 서버 부하테스트
- k6 스크립트 작성
```javascript
import http from 'k6/http';
import { sleep } from 'k6';

export default function () {
  http.get('http://13.125.227.183:8080/boards');
}
```
    
- k6 스크립트 실행 명령어  
k6 run --vus 30 --duration 10s script.js
- --vus 30: 가상 유저(Virtual Users)를 30명으로 설정
- --duration 10s: 지속 시간을 10초로 설정