package com.mokuroku.backend.member.service.Impl;

import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.dto.RegisterRequestDTO;
import com.mokuroku.backend.member.dto.RegisterResponseDTO;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO requestDTO) {
        // 중복 이메일 검사
        if (memberRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 중복 닉네임 검사
        if (memberRepository.existsByNickname(requestDTO.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        // Member 엔티티 생성
        Member member = Member.builder()
                .email(requestDTO.getEmail())
                .password(encodedPassword)
                .nickname(requestDTO.getNickname())
                .profileImage(requestDTO.getProfileImage())
                .socialLoginCheck(requestDTO.isSocialLoginCheck() ? 'Y' : 'N')
                .regDate(LocalDateTime.now())
                .role(Member.Role.USER)
                .status('1') // usable 상태
                .build();

        // 저장
        Member saved = memberRepository.save(member);

        // 응답 DTO 반환
        return RegisterResponseDTO.builder()
                .memberId(saved.getId())
                .email(saved.getEmail())
                .nickname(saved.getNickname())
                .profileImage(saved.getProfileImage())
                .build();
    }
}
