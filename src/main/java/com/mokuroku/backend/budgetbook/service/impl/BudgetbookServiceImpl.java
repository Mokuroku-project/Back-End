package com.mokuroku.backend.budgetbook.service.impl;

// 필요한 클래스와 어노테이션을 위한 import 문
import com.mokuroku.backend.budgetbook.dto.BudgetbookDTO; // 가계부 생성을 위한 DTO
import com.mokuroku.backend.budgetbook.dto.BudgetbookEditDTO; // 가계부 수정을 위한 DTO
import com.mokuroku.backend.budgetbook.entity.BudgetbookEntity; // 가계부 데이터의 엔티티 클래스
import com.mokuroku.backend.budgetbook.repository.BudgetbookRepository; // 가계부 데이터 접근을 위한 리포지토리
import com.mokuroku.backend.budgetbook.service.BudgetbookService; // 가계부 서비스 인터페이스
import com.mokuroku.backend.common.ResultDTO; // API 응답을 위한 공통 DTO
import com.mokuroku.backend.exception.ErrorCode; // 커스텀 예외의 에러 코드
import com.mokuroku.backend.exception.impl.CustomException; // 커스텀 예외 클래스
import com.mokuroku.backend.member.entity.Member; // 회원 엔티티 클래스
import com.mokuroku.backend.member.repository.MemberRepository; // 회원 데이터 접근을 위한 리포지토리
import lombok.RequiredArgsConstructor; // Lombok으로 final 필드에 대한 생성자 자동 생성
import lombok.extern.slf4j.Slf4j; // 로깅을 위한 SLF4J 어노테이션
import org.springframework.context.MessageSource; // 다국어 메시지 처리를 위한 Spring의 MessageSource
import org.springframework.http.ResponseEntity; // HTTP 응답을 처리하기 위한 클래스
import org.springframework.stereotype.Service; // Spring의 서비스 컴포넌트를 나타내는 어노테이션
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 관리를 위한 어노테이션
import java.time.LocalDateTime; // 날짜 및 시간 처리를 위한 클래스

// SLF4J 로깅을 활성화하여 로그 메시지 출력 가능
@Slf4j
// Spring의 서비스 계층임을 나타내며, Spring 컨텍스트에 빈으로 등록
@Service
// Lombok의 RequiredArgsConstructor로 final 필드에 대한 생성자를 자동 생성
@RequiredArgsConstructor
public class BudgetbookServiceImpl implements BudgetbookService {

    // 회원 데이터를 조회하기 위한 리포지토리
    private final MemberRepository memberRepository;
    // 가계부 데이터를 처리하기 위한 리포지토리
    private final BudgetbookRepository budgetbookRepository;
    // 다국어 메시지 처리를 위한 MessageSource
    private final MessageSource messageSource;

    // 가계부 생성 메서드
    // @Transactional : 데이터베이스 트랜잭션을 관리하여 메서드 실행 중 예외 발생 시 롤백
    @Override
    @Transactional
    public ResponseEntity<ResultDTO> budgetbookRegist(BudgetbookDTO budgetbookDTO) {
        // 테스트용 이메일, 실제 구현에서는 인증된 사용자의 이메일을 동적으로 가져와야 함
        String email = "test@gmail.com";
        // 이메일로 회원 조회, 존재하지 않으면 NOT_FOUND_MEMBER 예외 발생
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER, messageSource));

        // BudgetbookDTO 데이터를 기반으로 BudgetbookEntity 객체 생성
        BudgetbookEntity budgetbook = BudgetbookEntity.builder()
                .member(member) // 가계부 소유 회원 설정
                .type(budgetbookDTO.getType()) // 가계부 유형 (수입/지출 등)
                .amount(budgetbookDTO.getAmount()) // 금액
                .category(budgetbookDTO.getCategory()) // 카테고리
                .memo(budgetbookDTO.getMemo()) // 메모
                .date(budgetbookDTO.getDate()) // 거래 날짜
                .build();

        // 가계부 데이터를 데이터베이스에 저장
        budgetbookRepository.save(budgetbook);

        // 성공 메시지와 저장된 가계부 데이터를 포함한 응답 반환
        return ResponseEntity.ok(new ResultDTO<>("가계부 작성에 성공했습니다", budgetbook));
    }

    // 가계부 삭제 메서드
    @Override
    public ResponseEntity<ResultDTO> budgetbookDelete(Long budgetbookId) {
        // 테스트용 이메일, 실제 구현에서는 인증된 사용자의 이메일을 동적으로 가져와야 함
        String email = "test@gmail.com";
        // 이메일로 회원 조회, 존재하지 않으면 NOT_FOUND_MEMBER 예외 발생
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER, messageSource));

        // 가계부 ID로 가계부 조회, 존재하지 않으면 NOT_FOUND_BUDGETBOOK 예외 발생
        BudgetbookEntity budgetbook = budgetbookRepository.findById(budgetbookId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK, messageSource));

        // 가계부 소유자와 요청자의 이메일이 일치하는지 확인, 불일치 시 예외 발생
        if (!budgetbook.getMember().getEmail().equals(email)) {
            log.warn("가계부 소유자 불일치: budgetbookId={}, email={}", budgetbookId, email);
            throw new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK, messageSource);
        }

        // 가계부 데이터를 데이터베이스에서 삭제
        budgetbookRepository.deleteById(budgetbookId);
        // 삭제 성공 로그 기록
        log.info("가계부 삭제 성공: budgetbookId={}", budgetbookId);

        // 성공 메시지와 빈 데이터를 포함한 응답 반환
        return ResponseEntity.ok(new ResultDTO<>("가계부 삭제에 성공했습니다", ""));
    }

    // 가계부 수정 메서드
    // @Transactional : 데이터베이스 트랜잭션을 관리하여 메서드 실행 중 예외 발생 시 롤백
    @Override
    @Transactional
    public ResponseEntity<ResultDTO> budgetbookEdit(Long budgetbookId, BudgetbookEditDTO budgetbookEditDTO) {
        // 테스트용 이메일, 실제 구현에서는 인증된 사용자의 이메일을 동적으로 가져와야 함
        String email = "test@gmail.com";
        // 이메일로 회원 조회, 존재하지 않으면 NOT_FOUND_MEMBER 예외 발생
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER, messageSource));

        // 가계부 ID로 가계부 조회, 존재하지 않으면 NOT_FOUND_BUDGETBOOK 예외 발생
        BudgetbookEntity budgetbook = budgetbookRepository.findById(budgetbookId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK, messageSource));

        // 가계부 소유자와 요청자의 이메일이 일치하는지 확인, 불일치 시 예외 발생
        if (!budgetbook.getMember().getEmail().equals(email)) {
            log.warn("가계부 소유자 불일치: budgetbookId={}, email={}", budgetbookId, email);
            throw new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK, messageSource);
        }

        // BudgetbookEditDTO 데이터를 기반으로 기존 가계부 데이터 수정
        budgetbook.setType(budgetbookEditDTO.getType()); // 유형 업데이트
        budgetbook.setAmount(budgetbookEditDTO.getAmount()); // 금액 업데이트
        budgetbook.setCategory(budgetbookEditDTO.getCategory()); // 카테고리 업데이트
        budgetbook.setMemo(budgetbookEditDTO.getMemo()); // 메모 업데이트
        budgetbook.setDate(budgetbookEditDTO.getDate()); // 날짜 업데이트

        // 수정된 가계부 데이터를 데이터베이스에 저장
        budgetbookRepository.save(budgetbook);
        // 수정 성공 로그 기록
        log.info("가계부 수정 성공: budgetbookId={}", budgetbookId);

        // 성공 메시지와 수정된 가계부 데이터를 포함한 응답 반환
        return ResponseEntity.ok(new ResultDTO<>("가계부 수정에 성공했습니다", budgetbook));
    }

    // 가계부 조회 메서드
    // @Transactional(readOnly = true) : 읽기 전용 트랜잭션으로 성능 최적화
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResultDTO> budgetbook(Long budgetbookId) {
        // 테스트용 이메일, 실제 구현에서는 인증된 사용자의 이메일을 동적으로 가져와야 함
        String email = "test@gmail.com";
        // 이메일로 회원 조회, 존재하지 않으면 NOT_FOUND_MEMBER 예외 발생
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER, messageSource));

        // 가계부 ID로 가계부 조회, 존재하지 않으면 NOT_FOUND_BUDGETBOOK 예외 발생
        BudgetbookEntity budgetbook = budgetbookRepository.findById(budgetbookId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK, messageSource));

        // 가계부 소유자와 ID가 일치하는지 확인, 불일치 시 예외 발생
        if (!budgetbook.getMember().getEmail().equals(email) || !budgetbook.getBudgetbookId().equals(budgetbookId)) {
            log.warn("가계부 소유자 또는 ID 불일치: budgetbookId={}, email={}", budgetbookId, email);
            throw new CustomException(ErrorCode.NOT_FOUND_BUDGETBOOK, messageSource);
        }

        // 조회 성공 로그 기록
        log.info("가계부 조회 성공: budgetbookId={}", budgetbookId);
        // 이미 조회된 budgetbook 객체를 사용하므로 추가적인 findById 호출은 불필요
        // budgetbookRepository.findById(budgetbookId); // 이 줄은 중복이므로 제거 가능

        // 성공 메시지와 조회된 가계부 데이터를 포함한 응답 반환
        return ResponseEntity.ok(new ResultDTO<>("가계부 조회에 성공했습니다", budgetbook));
    }
}