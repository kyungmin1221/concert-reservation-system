### 항해 플러스 서버 구축
## **🎤 콘서트 예약 시스템 🎤**


<details>
<summary><b>step6 에서 한 번더 고민해본 대기열 시나리오(대기열에 아무도 없다면?)</b></summary>
    
#### 유저는 회원가입 시 서버에게 UUID 를 반환받는다.

#### 유저는 UUID 를 가지고 대기열에 진입한다. (바로 대기열 진입이 킥)
 

#### **1. 대기열 진입 과정의 시나리오 ( 대기열에 누군가 있을 경우? )**

- **유저가 대기열에 진입하지 않은 경우**:
    1. 유저가 좌석 조회를 시도.
    2. 서버가 대기열에 진입하지 않았음을 확인하고, **대기열 토큰 발급을 안내**.
    3. 유저가 대기열에 진입하여 **대기열 토큰을 발급받은 후** 좌석 조회 API를 호출.
- **유저가 대기열에 진입한 후**:
    1. 유저는 대기 상태에서 **폴링 API**를 통해 자신의 대기 상태를 확인.
    2. 대기 순번이 도착하면 좌석 조회 및 예약을 진행할 수 있습니다.

#### **2. 대기열 진입 과정의 시나리오 ( 대기열에 아무도 없을 경우? )**

- 대기열 진입
    - 유저는 대기열에 진입하는 요청을 보낸다
    - 대기열에 아무도 없다면 → 서버는 유저에게 대기 1번을 할당하고 즉시 처리 가능한 상태로 만들어준다.
- 바로 죄석 조회 및 콘서트 예약 가능
    - 대기가 1번이었던 유저는 대기할 필요가 없다. 따라서 **바로 좌석 조회 및 콘서트 예약이 가능**해야 한다.
    - 이 과정은 유저가 바로 대기열에서 빠져나오는 것처럼 처리되어야 하고, 바로 토큰을 이용해 다른 기능들을 정상적으로 이용 가능해야한다.

</details>

<details>
<summary><b>🛠️ 기술 스택</b></summary>
    
**Architecture**

    - Testable Business logics
    - Layered Architecture Based
    - Clean Architecture

**DB ORM**

    - Spring JPA
    - MYSQL
    
**Test**

    - JUnit 

</details>

<details>
<summary><b>📝 API 명세</b></summary>
<img width="1002" alt="스크린샷 2024-10-10 오후 9 46 21" src="https://github.com/user-attachments/assets/ee3f4bd5-e8e0-4d2b-a8a8-a5348b3746aa">


</details>


<details>
<summary><b>🤔 ERD</b></summary>
<img width="1635" alt="스크린샷 2024-10-10 오후 10 24 31" src="https://github.com/user-attachments/assets/7533852d-4f53-4c48-a0df-be41c8f85b97">


</details>



