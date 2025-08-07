package com.mokuroku.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "회원 정보 DTO")
public class MemberDTO {

    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "회원 이메일", example = "user@example.com")
    private String email;

    @Schema(description = "비밀번호 (암호화된 상태)", example = "$2a$10$...")
    private String password;

    @Schema(description = "회원 닉네임", example = "mokuroku")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImage;

    @Schema(description = "소셜 로그인 여부 (true=소셜 로그인)", example = "false")
    private boolean socialLogin;

    @Schema(description = "가입일시", example = "2025-08-07T12:34:56")
    private LocalDateTime regDate;

    @Schema(description = "탈퇴일시 (null이면 탈퇴하지 않음)", example = "2025-09-01T15:00:00")
    private LocalDateTime withdrawalDate;

    @Schema(description = "회원 권한 (예: user, admin)", example = "user")
    private String role;

    @Schema(description = "계정 활성 상태 (true=사용 가능, false=정지/탈퇴)", example = "true")
    private boolean status;
}

// ✅ 필드 설명 요약 (Swagger UI에서 표시됨)
// 필드	설명	예시
// id	회원 고유 식별자	1
// email	이메일 주소	user@example.com
// password	암호화된 비밀번호	$2a$10$...
// nickname	닉네임	mokuroku
// profileImage	프로필 이미지 URL	https://example.com/img.jpg
// socialLogin	소셜 로그인 여부	false
// regDate	가입 일시	2025-08-07T12:34:56
// withdrawalDate	탈퇴 일시 (null이면 미탈퇴)	2025-09-01T15:00:00
// role	사용자 권한	user, admin
// status	계정 상태 (true=활성, false=비활성)	true

//        ## 🔍 1. Entity (`Member`)와 분리해서 사용하는 이유
//
// 이유                    | 설명                                                                                                                    |
//        | --------------------- | --------------------------------------------------------------------------------------------------------------------- |
//        | 🔐 **보안성**            | `Member` 엔티티에는 password, status, internal ID 등 민감한 정보가 포함될 수 있는데, 이를 그대로 노출하면 보안상 위험합니다. DTO를 사용하면 노출 필드를 제어할 수 있습니다. |
//        | ⚙️ **역직렬화/요청 유효성 검사** | 클라이언트로부터 받는 요청에 대해 Entity를 직접 매핑하면 검증하기 어렵고, DTO를 통해 검증이 가능합니다.                                                       |
//        | 🔄 **응답 구조 유연성**      | 엔티티 구조는 DB 모델에 맞춰져 있지만, 응답은 사용자 관점으로 구성해야 하므로 DTO는 응답을 더 자유롭게 설계할 수 있습니다.                                             |
//        | 📚 **계층 간 책임 분리**     | Controller ↔ Service ↔ Repository 간 데이터 흐름에서 DTO는 명확한 데이터 구조 전달 수단입니다.                                                |
//
//        ---
//
//        ## 📌 예시로 이해하기
//
//### 🔸 Member Entity (DB 구조 기반)
//
//```java
//@Entity
//public class Member {
//    private Long id;
//    private String email;
//    private String password;
//    private String nickname;
//    private LocalDateTime regDate;
//    ...
//}
//```
//
//        ### 🔸 MemberDTO (서비스/응답 전용)
//
//```java
//public class MemberDTO {
//    private Long id;
//    private String email;
//    private String nickname;
//    private boolean status;
//}
//```
//
//        * 👉 `MemberDTO`는 `password`나 `withdrawalDate` 등을 포함하지 않아 **안전한 응답**을 보장합니다.
//        * 👉 필요에 따라 `MemberDTO`는 관리자, 일반 사용자용 등으로 변형된 버전을 만들 수도 있습니다.
//
//---
//
//        ## 🧩 실제 용도 예시
//
//| 사용 위치                  | 예시                                  |
//        | ---------------------- | ----------------------------------- |
//        | ✅ Controller → Service | 클라이언트 요청 값을 DTO로 받아 서비스 계층에 전달      |
//        | ✅ Service → Controller | 응답 시 Entity → DTO로 변환하여 필요한 데이터만 전달 |
//        | ✅ 테스트 / 로깅             | DTO를 활용해 응답 포맷 구조화 및 테스트 자동화 가능     |
//
//        ---
//
//        ## 🚫 만약 DTO 없이 Entity를 직접 노출하면?
//
//        * 민감한 데이터(`password`, `status`, `role`)가 Swagger 또는 API 응답에서 노출될 수 있음
//* API 스펙이 Entity 구조에 종속 → Entity 변경 시 API도 함께 깨짐
//* 테스트나 문서화가 어려워짐
//
//---
//
//        ## ✅ 결론
//
//> `MemberDTO`는 **Entity와 API 간의 명확한 경계**를 유지하며,
//> **보안**, **유지보수성**, **역할 분리**를 위한 핵심 구조입니다.
//
//        **비유하자면**:
//
//        * Entity는 DB와 직접 연결된 “원시 데이터”
//        * DTO는 외부에 노출되는 “가공된 상품”
//
//        ---
//
//원하신다면:
//
//        * `MemberDTO`의 역할을 더 잘 분리하기 위한 구조 (예: `MemberSummaryDTO`, `MemberDetailDTO`)
//* DTO ↔ Entity 변환을 도와주는 Mapper 또는 ModelMapper 적용
//
//등도 함께 설명해드릴 수 있어요.
