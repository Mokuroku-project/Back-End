package com.mokuroku.backend.budgetbook.service;

// 필요한 클래스와 어노테이션을 위한 import 문
import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO; // 가계부 생성을 위한 DTO
import com.mokuroku.backend.budgetbook.dto.BudgetbookEditDTO; // 가계부 수정을 위한 DTO
import com.mokuroku.backend.common.ResultDTO; // API 응답을 위한 공통 DTO
import org.springframework.http.ResponseEntity; // HTTP 응답을 처리하기 위한 클래스
import org.springframework.stereotype.Service; // Spring의 서비스 컴포넌트를 나타내는 어노테이션

// Spring의 서비스 계층임을 나타내는 어노테이션
// 이 인터페이스는 Spring 빈으로 등록되어 의존성 주입에 사용됨
@Service
public interface BudgetbookService {

    // 새로운 가계부를 등록하는 메서드
    // @param budgetbookDTO : 클라이언트로부터 받은 가계부 생성 데이터를 담은 DTO
    // @return ResponseEntity<ResultDTO> : 가계부 등록 결과와 HTTP 상태 코드를 포함한 응답
    ResponseEntity<ResultDTO> budgetbookRegist(BudgetbookDTO budgetbookDTO);

    // 특정 가계부를 삭제하는 메서드
    // @param budgetbookId : 삭제할 가계부의 고유 식별자 (ID)
    // @return ResponseEntity<ResultDTO> : 가계부 삭제 결과와 HTTP 상태 코드를 포함한 응답
    ResponseEntity<ResultDTO> budgetbookDelete(Long budgetbookId);

    // 특정 가계부를 수정하는 메서드
    // @param budgetbookId : 수정할 가계부의 고유 식별자 (ID)
    // @param budgetbookEditDTO : 수정할 가계부 데이터를 담은 DTO
    // @return ResponseEntity<ResultDTO> : 가계부 수정 결과와 HTTP 상태 코드를 포함한 응답
    ResponseEntity<ResultDTO> budgetbookEdit(Long budgetbookId, BudgetbookEditDTO budgetbookEditDTO);

    // 특정 가계부 정보를 조회하는 메서드
    // @param budgetbookId : 조회할 가계부의 고유 식별자 (ID)
    // @return ResponseEntity<ResultDTO> : 가계부 조회 결과와 HTTP 상태 코드를 포함한 응답
    ResponseEntity<ResultDTO> budgetbook(Long budgetbookId);
}