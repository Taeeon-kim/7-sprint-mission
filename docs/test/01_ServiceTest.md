    /*
        Service 테스트 범위(목적)
        ──────────────────────
        ▣ 테스트 철학 (Mockist / Intent-Oriented)
          - 서비스 단위 테스트는 Mockist 접근법을 따르며, 
            “의도 중심(Intent-Oriented)”과 “행위(Behavior)”를 검증에 초점을 둔다.
          - 즉, 서비스의 주요 역할이 ‘협력자 간 행위 조율’이므로,
            Mock/Stub을 활용해 호출·분기·전파를 검증.
            테스트는 행위가 언제(Branch), 어떤 순서로(Flow),
            어떤 예외 상황에서(Propagation) 발생하는지를 함께 검증한다.

            즉, 도메인 테스트가 "상태(Classicist)" 중심이라면,
            서비스 테스트는 "행위(Mockist)" 중심이다.

        1) 행위(Behavior)
           - 올바른 협력자를 호출/미호출 하는가? 위임이 일어났는지 확인
             ex) verify(repo).save(user) / verify(repo, never()).save(any()) / ArgumentCaptor, argThat

        2) 흐름/순서(Flow/Sequence)
           - 서비스가 정상 흐름을 따라 동작했는가?
             ex) userService.getUserById(id) -> mock/stub이 설정된 상태에서 서비스 메서드가 정상적으로 실행되고, 중간에 예외나 빠진 단계 없이 “한 사이클이 돈다”는 걸 검증
           - 호출 순서가 의미가 있으면 InOrder로 검증
             ex) inOrder(reader, repo).verify(reader).find... → verify(repo).save(...)

        3) 분기(Branch)
           - 입력/상태에 따라 서로 다른 경로를 타는가?
             * Positive(정상): 조건 충족 시 기대 동작 수행 (예: 변경 O → save 호출)
             * Negative(부정): 조건 불충족 시 동작 차단
               - 입력 가드(예: id == null → IllegalArgumentException, 협력자 미호출)
               - 행위 부정(예: 변경 없음 → save 미호출)
            - 테스트에서는 해당 조건 분기의 결과를 assertion/verify로 검증한다.

        4) 전파(Propagation)
           - 하위 계층(Reader/Repository/Gateway 등)에서 발생한 예외가 서비스에서 적절히 전파되는가?
             ex) when(reader.find...).thenThrow(...) → assertThrows(...)

        ※ 참고사항
         ──────────────────────
        • 위의 테스트 목적들은 서로 독립적이지만, 실제 코드에서는
          하나의 테스트가 두 가지 이상의 목적(행위, 분기 등)을 동시에 검증할 수도 있다.
          같은 검증 코드라도 “테스트 의도”에 따라 해석이 달라질 수 있다.

          ex)
            (1) 단순 위임 확인 → 행위 검증
                verify(repo).save(user);

            (2) 특정 조건 충족 시 호출 → 분기 결과 검증
                verify(repo).save(user);

            (3) 일반적인 서비스 테스트 → 행위 + 분기 검증
                verify(repo).save(user); // 위임이면서 조건 결과

        ※ 엔티티 규칙(값 변경, 불변식 등)은 도메인 테스트에서 검증하고, 서비스 테스트는 협력자 호출/흐름/분기에 집중한다.
    */

    /*
        테스트케이스 명명 규칙
        ──────────────────────
        1) 메서드명: 기능_should결과_when상황  (영문 권장)
           - ex) signUp_shouldCallUserRepositorySave_whenValidInput
           - ex) updateUser_shouldNotCallRepositorySave_whenNoFieldChanged
           - ex) getUserById_shouldPropagateException_whenNotFound

        2) @DisplayName: 의도(Intent) 중심으로 명확하고 가독성 있게 기술
            사용 태그: [Behavior], [Branch], [Flow], [Exception], [Positive], [Negative]

           - ex) [Behavior] 회원가입 - userRepository.save() 호출 (저장 위임 검증)
           - ex) [Branch][Positive] 회원수정 - 필드 변경 시 save 호출 (조건 충족 경로)
           - ex) [Branch][Negative] 회원수정 - 변경 없음이면 save 미호출 (조건 불충족 경로)
           - ex) [Flow] 회원조회 - 정상 입력값일 때 서비스 흐름이 정상적으로 반환됨
           - ex) [Exception] 회원조회 - 미존재 시 NoSuchElementException 전파

        ※ [Positive]/[Negative] 태그는 주로 [Branch] 테스트에서 사용하며,
           [Behavior], [Flow], [Exception]은 일반적으로 단독 사용 가능.
           필요 시 조합 형태([Branch+Behavior])로도 명시 가능.

        3) 도메인 중심 vs 구현 중심
           - 서비스 테스트는 보통 "구현/행위 중심"으로 네이밍(협력자 호출/전파/분기 확인)
             ex) signUp_shouldCallUserRepositorySave_whenValidInput (O)
           - "도메인 결과 중심" 표현(예: ~shouldSaveNewUser)은 엔티티/통합 테스트에서 사용 권장 (서비스 단위에선 혼동 유발)

        4) 한 테스트 = 한 사실(주장)만 검증(AAA/GWT 분리)
           - Given(픽스처/Stub) → When(한 줄) → Then(단언/verify)
    */
