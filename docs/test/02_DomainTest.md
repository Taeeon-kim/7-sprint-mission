
        Domain(Entity) 테스트 철학
        ──────────────────────
        ▣ 테스트 철학 (Classicist / State-Oriented)
          - 도메인 단위 테스트는 Classicist 접근법을 따르며, “상태(State)-중심” 검증에 집중한다.
            -> 외부 협력자(Mock, Stub)를 사용하지 않고, 결과 상태와 불변식으로 검증.
          - 엔티티는 외부 협력자 없이 스스로의 상태 일관성을 유지해야 하므로,
            테스트는 내부 상태 변화와 제약(불변식, 규칙, 상태 전이, 값 동등성)이 올바르게 작동하는지를 검증한다.

          - 즉, 서비스 테스트가 “협력자 간 행위 조율(Mockist)”이라면,
            도메인 테스트는 “객체 스스로의 일관성(Classicist) 보장”을 검증한다.

        1) 불변식(Invariant)
           - 객체가 항상 만족해야 하는 상태 제약이 유지되는가?
             ex) 잔액은 음수가 될 수 없다 / 이메일 형식이 유효해야 한다
           - 생성자 및 상태 변경 메서드 호출 후, 엔티티가 유효한 상태를 유지하는지 검증

        2) 규칙(Rule)
           - 도메인 정책과 비즈니스 규칙이 정확히 반영되는가?
             ex) 친구는 중복 추가 불가 / 주문은 최소 1개 상품을 포함해야 한다

        3) 상태 전이(State Transition)
           - 메서드 호출 후 상태 변화가 기대한 대로 일어나는가?
             ex) deposit 호출 시 balance가 증가하는가?
             ex) cancel 호출 시 status가 CANCELLED로 변경되는가?

        4) 동등성/값(Value)
           - 동일성(==)과 동등성(equals)의 정의가 올바른가?
             ex) 같은 id를 가진 두 User는 equals=true
           - Value Object는 불변이어야 하며, equals/hashCode가 값 기반으로 작동하는지 검증

        ※ 참고사항
         ──────────────────────
         • 도메인 테스트는 외부 협력자(Mock/Stub)를 사용하지 않는다.
           → 모든 검증은 순수 객체 상태와 반환값 기반으로 한다.
         • 엔티티의 규칙은 서비스 테스트보다 우선적으로 단위 테스트 대상이 된다.
           (서비스는 규칙 조합/조율, 도메인은 규칙 정의/보장)
         • 한 테스트는 한 주장(사실)만 검증하며, 성공/실패(Positive/Negative) 페어로 작성한다.
   

    /*
        테스트케이스 명명 규칙
        ──────────────────────
        1) 메서드명: 기능_should결과_when상황 (영문 권장)
           - ex) deposit_shouldIncreaseBalance_whenPositiveAmount
           - ex) withdraw_shouldThrowException_whenInsufficientFunds
           - ex) addFriend_shouldRejectDuplicate_whenAlreadyFriend

        2) @DisplayName: 의도(Intent) 중심으로 명확하게
           - ex) [Rule] 입금 - 양수 금액이면 잔액 증가
           - ex) [Rule][Negative] 입금 - 음수 금액이면 예외 발생
           - ex) [Invariant] 회원 생성 - 필수 필드 누락 시 예외 발생
           - ex) [State] 주문 - 결제 후 상태가 PAID로 변경
           - ex) [Value] Money - 같은 금액이면 equals=true

        ※ [Rule]/[Invariant]/[State]/[Value] 태그 사용 권장
           (서비스의 [Behavior]/[Branch]/[Flow]/[Exception]과 구분)

        3) 도메인 중심 vs 구현 중심
           - 도메인 테스트는 "규칙/상태 중심"으로 네이밍
             ex) deposit_shouldIncreaseBalance_whenPositiveAmount (O)
           - 구현(메서드 호출) 중심 표현(예: callSomething_whenCondition)은 서비스 테스트용

        4) 한 테스트 = 한 사실(AAA/GWT 분리)
           - Given(픽스처) → When(행동) → Then(검증)
    */