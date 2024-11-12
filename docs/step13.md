**동시성 문제와 함께 다량의 트래픽을 처리하기 위해 어떻게 DB 에 적은 부하를 주면서 기능을 구현할 수 있을까?**

### 💡 Caching
- 데이터를 임시로 복사
- 적은 부하로 API 응답을 빠르게 처리할 수 있다.

> **우리 주변에서 볼 수 있는 Cache 사례**
* DNS : 웹사이트 IP 를 기록해두어 웹사이트 접근을 위한 DNS 조회 수를 줄인다.
* CPU : 자주 접근하는 데이터의 경우, 메모리에 저장해두고 빠르게 접근할 수 있도록 한다.
* CDN : 이미지나 영상과 같은 컨텐츠를 CDN 서버에 저장해두고 애플리케이션에 접근하지 않고 컨텐츠를 가져오도록 해 부하를 줄인다.

### ✅ 메모리 캐시의 원리
**< 요청 1 >**
1. 유저의 API 요청이 들어왔을 때 메모리에 Cache Hit 확인
2. Cache Miss 시 비즈니스 로직을 수행한 후 메모리에 데이터를 저장
3. 응답

**< 요청 2 >**
1. 유저의 API 요청이 들어왔을 때 메모리에 응답이 있는지 없는지 확인
2. 있으면 해당 캐시 값을 반환


### **🤔 캐시 히트를 어떻게하면 높일 수 있을까? **
**_Cache Hit 율이 높아야한다. 때문에 이번엔 이 방법에 대해 많은 고민이 필요하다.
한 사람이 한 번만 조회를 하고 서비스를 나간다? 그렇다면 Cache 가 필요할까? 그렇지 않다. Cache 는 다수의 유저가 여러번 접근하는 경우를 우선적으로 고려해봐야한다._**

### 💡 조회가 오래 걸리는 쿼리에 대한 캐싱, 혹은 Redis 를 이용한 로직 이관을 통해 성능 개선할 수 있는 곳은?
> 일단 나의 서비스에 있는 API 를 먼저 살펴볼 필요가 있다.
- 유저 생성 API
- 유저 잔액 충전 API
- 유저의 대기열 순서 조회 API
- 유저의 잔액을 조회 API
- 콘서트 생성 API
- 콘서트 이벤트 생성 API
- 콘서트 대기열 생성 API
- 예약 생성 API
- 콘서트 좌석 조회 API
- 콘서트 예약 가능 날짜 조회 API
- 콘서트 결제 API


### ❓ 어떤 것에 적용해야하고 그 이유는 뭔데 ❓
**먼저 나는 위의 API 에서 어떤 것이 데이터 변경이 적으면서도 접근이 많을까에 대해 생각해보았다.**
>
**1. 콘서트 좌석 조회 API & 콘서트 예약 가능 날짜 조회 API
2. 대기열 조회**

### 💡 이유 💡
- 먼저 콘서트의 정보, 즉 좌석이나 예약가능한 날짜는 데이터의 변경이 적을 수 밖에 없다. 그리고 서비스 특성 상 조회가 빈번하게 이루어질 것이다. 때문에 매번 DB 에서 조회하는 것은 부담이 될 것이라고 생각하였고, 이 부분을 캐싱하기로 생각했다. 콘서트의 정보를 Redis 에 캐시하고, 좌석 예약 변경이 발생하는 경우만 업데이트를 하게할 수 있다. 이렇게 하면 예약 요청이 들어올 때마다 DB에 직접 접근할 필요없이 Redis 에서 빠르게 조회를 확인할 수 있을 것이라고 판단했다.

- 대기열의 상태는 많은 사용자가 동시에 접근할 수 있다. 역시 DB 에 직접 조회하는 것은 무리가 될 수 있다. 때문에 대기열 정보를 Redis 에 저장을 하고, 대기열에 새로운 유저가 추가되거나 예약으로 빠지게 되는 경우 Redis 의 상태를 업데이트하면 좋을 것 같다고 생각하였고 적용하고자 한다. (Redis 의 TTL 기능으로 오래된 대기열 데이터들을 관리해준다면 좋지 않을까?)