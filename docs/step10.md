#### 🔗 [github 링크](https://github.com/kyungmin1221/concert-reservation-system)

### 1. 대기열에 대한 고민
#### **1. 대기열 진입 과정의 시나리오 ( 대기열에 누군가 있을 경우? )**

- **유저가 대기열에 진입하지 않은 경우**:
    1. 유저가 좌석 조회를 시도.
    2. 서버가 대기열에 진입하지 않았음을 확인하고, **대기열 토큰 발급을 안내**.
    3. 유저가 대기열에 진입하여 **대기열 토큰을 발급받은 후** 좌석 조회 API를 호출.

  → 잘못 생각한 부분 ( 수정 )

    1. 유저는 바로 대기열에 진입한다.
    2. 대기열에 진입한 유저는 **대기열 토큰** 을 부여받는다.
    3. 유저는 그 토큰을 들고 좌석 예약 및 콘서트 결제, 잔액 충전을 진행한다.
- **유저가 대기열에 진입한 후**:
    1. 유저는 대기 상태에서 **폴링 API**를 통해 자신의 대기 상태를 확인.
    2. 대기 순번이 도착하면 좌석 조회 및 예약을 진행할 수 있습니다.

> - 유저가 들고 있는 대기열 토큰이 유효한가 ??
    - 유저는 본인의 uuid 를 굳이 검증받을 필요가 있을까? → No(?) : 유저는 처음 본인의 uuid 로 대기열에 진입하여 토큰을 부여받는다. 그렇다면 그 이후부터는 대기열 토큰으로만 api 검증을 해도 상관없다 생각( uuid 로 또 검증을 받는 건 비효율적이고 불필요하다 생각)
- 예약하려는 좌석이 유효한 좌석인가?
    - 이미 예약이 되어있는 좌석은 아닌지?
    - 예약을 처리하고 있는 좌석은 아닌지?
- 콘서트 결제 API 를 처리하는 컨트롤러가 필요할까? 아니면 예약 트랜잭션 안에서 결제도 같이 진행되어야 할까?
    - 예약 후 결제가 가능한 상태로 만들자 !
        - →  만약 유저가 결제 가능한 상태에서 결제를 하지않으면 예약이 취소되도록 만들어야한다.
        - → 그래서 대기열에서 대기중이었던 사람들이 예약이 가능하도록 해주어야함
        - →  유저가 대기열 토큰을 받고 예약이 가능한 상태가 되면, 무제한 제공하는 것이 아닌 일정시간을 배정한다 (5분..? ) → 이 시간동안  아무것도 하                  지않으면 예약을 취소시켜야한다.
        - →  유저는 결제를 진행하고(결제 API) 이때, 결제완료와 좌석의 최종선택확정을 함
        - → 결제가 완료되면 예약을 확정 상태로 만들고, 좌석을 배정
- 유저가 결제를 하지 않는다면? ( 만약 유저가 결제 가능한 상태에서 결제를 하지않으면 예약이 취소 )
    - 해당 좌석은 다시 예약 가능한 상태로 변경 되어야 한다.
        - 반응이 없을 때 주기적으로 예약이 걸려있는 좌석을 취소해주는 건 어떻게 할것인가? ⇒ 스케쥴러
        - 또한 대기열에서 대기중인 사람들한테 자동으로 예약을 할 수 있게 배정은 어떻게 할 것 인가?


#### **2. 대기열 진입 과정의 시나리오 ( 대기열에 아무도 없을 경우? )**

- 대기열 진입
    - 유저는 대기열에 진입하는 요청을 보낸다
    - 대기열에 아무도 없다면 → 서버는 유저에게 대기 1번을 할당하고 즉시 처리 가능한 상태로 만들어준다.
- 바로 죄석 조회 및 콘서트 예약 가능
    - 대기가 1번이었던 유저는 대기할 필요가 없다. 따라서 **바로 좌석 조회 및 콘서트 예약이 가능** 해야 한다.
    - 이 과정은 유저가 바로 대기열에서 빠져나오는 것처럼 처리되어야 하고, 바로 토큰을 이용해 다른 기능들을 정상적으로 이용 가능해야한다.


-> 내가 대기열을 어떤식으로 구현해야할까를 고민하면서 작성했던 내용

---
### 2. 두 번째 고민
예약을 할 수 있는 대기열의 칸이 5칸이라고 했을 경우

                  —————
    <—         1 2 3 4 5         <—  이렇게 있겠지 ?   
                  ————— 

이 때, 대기열에서 누군가 빠졌을 때,  시나리오 가정

#### 시나리오 1 ) 대기열에서 순서대로 기다리고 있는데 중간에 누가 빠진다면 ?

                —————
    <—         1 2 x 4 5         <—      
                  ————— 

==> 뒤 사람들을 앞으로 대기열 순서를 땡긴 후, 예약 대기열 1번 삽입 ?


#### 시나리오 2 ) 대기열에서 맨 뒤에 기다리고 있는 사람이 빠진다면 ?
                —————
    <—         1 2 3 4          <—   
                  ————— 

==>    스케쥴러를 이용하여 대기하고 있는 1번 뒤에 삽입

--- 

1 2 3 4 5  -> 대기열에 있는 사람들

1.
대기열에 있는 1~ 5 유저 중, 시간이 지나 누군가 대기열에서 빠져나갔다고(CANCELED) 하자.
만약 대기열 1번이 취소가 되어 대기열에서 빠져나갔다고 하면 현재 대기열 1번이었던 사람이 빠져나가 대기열1번인 사람은 없어지고 2번부터 있다. 그렇다면 2~5 번 대기열사람들은 대기열 순서가 한단계씩 앞으로 가야한다.

2.
1 2 3 4 5 -> 대기열
대기열에 있는 사람들이, 즉 예약중인 사람들?
( 대기열 대기 = 예약 대기 )

> 처음에 대기열을 고민하면서 구현은 했었지만, 구현을 하다보니 예약을 기다리는 사람들과 대기열에서 기다리고 있는 사람들은 뭐가 다른거지? 라는 고민을 하게되었다. 지금 생각하면 너무나 당연하게도 대기열에 있는 사람들은 예약을 하기를 기다리는 사람들인데 나는 (대기열에 있는 사람들 = 예약을 기다리는 사람들) 이라고 갑자기 생각이 들어 잠깐 고민을 했었다.

---
### 3. Interceptor
콘서트 서비스를 개발하면서 Interceptor 에 대해 공부하고 적용하고자 하였다. Interceptor 는 컨트롤러 전후로 요청을 가로채 처리해주는 역할을 하는데, 나는 여기서 대기열 토큰의 여부를 확인하고자 하였고, 확인하는 로직을 구현하였다.

> 하지만 고민,, 이미 나의 서비스에서는 유저등록을 제외한 모든 로직이 대기열 토큰을 확인하도록 구현되어 있었다. Interceptor 를 적용하면서 서비스 로직에서 대기열 토큰을 확인하는 로직이 이제 필요하지 않다고 생각하였고, 제거하였는데..


#### Interceptor 를 사용하면서 변경한 코드
```
// Interceptor 에서 토큰을 확인하기 위한 메서드
public boolean isValidToken(String queueToken) {
        return queueRepository.existsByQueueToken(queueToken);
    }
```

```
// QueueTokenInterceptor.java
@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("======== 대기열 토큰 인터셉터 실행 ==========");

        String queueToken = request.getHeader("token");
        
		// 여기서 대기열 토큰을 확인 
        if (queueToken == null || !reservationService.isValidToken(queueToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 토큰이 유효하지 않으면 401 에러 반환
            return false;
        }

        log.info("======== 대기열 토큰 인터셉터 종료 ==========");
        
        // 
        User user = reservationService.validateToken(queueToken);
        if(user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        request.setAttribute("user", user);
        return true;
    }
```
더 자세한 코드는 생략하겠다. 어찌됐건 인터셉터에서 정상적으로 토큰값을 받아오고
서비스 로직에서 유저를 불러오는데 ,,,
```
 // 잔액 충전 (대기열 토큰 필수)
    @Override
    @Transactional
    public UserPointResponseDto chargePoint(HttpServletRequest request, UserPointRequestDto requestDto) {

        // 대기열 토큰 검증 ( 문제 발생 지점 )
        User user = (User) request.getAttribute("user");

        // 잔액 충전
        user.addPoints(requestDto.getPoint());
        userRepository.save(user);

        return new UserPointResponseDto(user.getPoint());
    }
```
![](https://velog.velcdn.com/images/kyungmin/post/012cd59e-662f-427e-8017-8b237adfbc71/image.png)
> Method threw 'org.hibernate.LazyInitializationException' exception. Cannot evaluate com.example.concertreservationsystem.domain.model.User$HibernateProxy$SLfKFS4D.toString()

**이 부분까지 정상적으로 유저의 정보를 받아오나 했지만 다음과 같은 오류가 발생했다.**

-> 이 부분은 아직 이유를 몰라 코치님들한테 여쭤보거나 더 알아봐야 할 것 같다..
찾아보니 지연로딩 문제라 fetch 타입을 EAGER 로 바꾸어보라해서 바꾸어보았는데 해결은 되지 않았다. 좀 더 알아보자!
