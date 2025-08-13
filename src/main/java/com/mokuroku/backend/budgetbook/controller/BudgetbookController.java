package com.mokuroku.backend.budgetbook.controller;

// 필요한 클래스와 어노테이션을 위한 import 문
import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO; // 가계부 생성을 위한 DTO
import com.mokuroku.backend.budgetbook.dto.BudgetbookEditDTO; // 가계부 수정을 위한 DTO
import com.mokuroku.backend.budgetbook.service.BudgetbookService; // 비즈니스 로직을 처리하는 서비스 계층
import com.mokuroku.backend.common.ResultDTO; // API 응답을 위한 공통 DTO
import lombok.RequiredArgsConstructor; // Lombok 어노테이션으로 final 필드에 대한 생성자 자동 생성
import org.springframework.http.ResponseEntity; // HTTP 응답을 처리하기 위한 클래스
import org.springframework.web.bind.annotation.*; // Spring Web의 어노테이션 (RestController, RequestMapping 등)

// Spring의 RestController로, HTTP 요청을 처리하는 컨트롤러 클래스임을 나타냄
@RestController
// 모든 요청 경로가 "/budgetbook"으로 시작하도록 설정
@RequestMapping("/budgetbook")
// Lombok의 RequiredArgsConstructor로, final 필드인 budgetbookService에 대한 생성자를 자동 생성
@RequiredArgsConstructor
public class BudgetbookController {

    // 가계부 관련 비즈니스 로직을 처리하는 서비스 객체, final로 선언하여 불변성 보장
    private final BudgetbookService budgetbookService;

    // 새로운 가계부를 등록하기 위한 POST 요청 처리
    // @PostMapping("/") : "/budgetbook/" 경로로 들어오는 POST 요청을 처리
    // @RequestBody : 요청 본문에 포함된 JSON 데이터를 BudgetbookDTO 객체로 변환
    @PostMapping("/")
    public ResponseEntity<ResultDTO> budgetbook(@RequestBody BudgetbookDTO budgetbookDTO){
        // BudgetbookService의 budgetbookRegist 메서드를 호출하여 가계부 등록 처리
        ResponseEntity<ResultDTO> result = budgetbookService.budgetbookRegist(budgetbookDTO);
        // 서비스에서 반환된 응답을 그대로 클라이언트에게 반환
        return result;
    }

    // 특정 가계부를 삭제하기 위한 DELETE 요청 처리
    // @DeleteMapping("/{budgetbookId}") : "/budgetbook/{budgetbookId}" 경로로 들어오는 DELETE 요청을 처리
    // @PathVariable : URL 경로에서 budgetbookId 값을 추출
    // @RequestBody : 이 어노테이션은 DELETE 요청에서 일반적으로 불필요하므로 제거 고려 가능
    @DeleteMapping("/{budgetbookId}")
    public ResponseEntity<ResultDTO> budgetbookDelete(@RequestBody @PathVariable Long budgetbookId) {
        // BudgetbookService의 budgetbookDelete 메서드를 호출하여 가계부 삭제 처리
        ResponseEntity<ResultDTO> result = budgetbookService.budgetbookDelete(budgetbookId);
        // 서비스에서 반환된 응답을 그대로 클라이언트에게 반환
        return result;
    }

    // 특정 가계부를 수정하기 위한 PUT 요청 처리
    // @PutMapping("/{budgetbookId}") : "/budgetbook/{budgetbookId}" 경로로 들어오는 PUT 요청을 처리
    // @PathVariable : URL 경로에서 budgetbookId 값을 추출
    // @RequestBody : 요청 본문에 포함된 JSON 데이터를 BudgetbookEditDTO 객체로 변환
    @PutMapping("/{budgetbookId}")
    public ResponseEntity<ResultDTO> budgetbookEdit(@PathVariable Long budgetbookId, @RequestBody BudgetbookEditDTO budgetbookDTO){
        // BudgetbookService의 budgetbookEdit 메서드를 호출하여 가계부 수정 처리
        ResponseEntity<ResultDTO> result = budgetbookService.budgetbookEdit(budgetbookId, budgetbookDTO);
        // 서비스에서 반환된 응답을 그대로 클라이언트에게 반환
        return result;
    }

    // 특정 가계부 정보를 조회하기 위한 GET 요청 처리
    // @GetMapping("/{budgetbookId}") : "/budgetbook/{budgetbookId}" 경로로 들어오는 GET 요청을 처리
    // @PathVariable : URL 경로에서 budgetbookId 값을 추출
    @GetMapping("/{budgetbookId}")
    public ResponseEntity<ResultDTO> budgetbook(@PathVariable Long budgetbookId){
        // BudgetbookService의 budgetbook 메서드를 호출하여 가계부 정보 조회 처리
        ResponseEntity<ResultDTO> result = budgetbookService.budgetbook(budgetbookId);
        // 서비스에서 반환된 응답을 그대로 클라이언트에게 반환
        return result;
    }
}