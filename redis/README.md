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