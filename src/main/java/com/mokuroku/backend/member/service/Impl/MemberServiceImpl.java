package com.mokuroku.backend.member.service.Impl;

import com.mokuroku.backend.common.ResultDTO;
import com.mokuroku.backend.common.component.MailComponents;
import com.mokuroku.backend.common.component.S3Uploader;
import com.mokuroku.backend.exception.ErrorCode;
import com.mokuroku.backend.exception.impl.CustomException;
import com.mokuroku.backend.member.dto.*;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.member.repository.MemberRepository;
import com.mokuroku.backend.member.service.MemberService;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private static final Long MAIL_EXPIRES_IN = 300000L; // 5분
  private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif"); // 허용 가능한 이미지 파일 형식

  private final MemberRepository memberRepository;
  private final S3Uploader s3Uploader;
  private final RedisTemplate<String, String> redisTemplate;
  private final MailComponents mailComponents;

  @Override
  @Transactional
  public ResponseEntity<ResultDTO> register(RegisterRequestDTO requestDTO, MultipartFile file) {

    // 중복 이메일 검사
    if (memberRepository.existsById(requestDTO.getEmail())) {
      throw new CustomException(ErrorCode.DUPLICATE_MEMBER);
    }

    // 중복 닉네임 검사
    if (memberRepository.existsByNickname(requestDTO.getNickname())) {
      throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
    }

    // 프로필 이미지 파일 있는지 확인
    String imageUrl = null;

    if (file != null && !file.isEmpty()) { // 있을 경우 이미지 파일 검사 후 회원정보 저장
      // 파일 이름에서 확장자 추출
      String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

      // 확장자가 이미지 파일인지 확인
      if (fileExtension != null && ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
        try {
          imageUrl = s3Uploader.upload(file, "profile-images");
        } catch (Exception e) {
          throw new CustomException(ErrorCode.FAILED_IMAGE_SAVE);
        }
      } else { // 이미지 파일이 아닌 경우에 대한 처리
        throw new CustomException(ErrorCode.UN_SUPPORTED_IMAGE_TYPE);
      }
    }

    Member member = RegisterRequestDTO.joinMember(requestDTO, imageUrl);
    memberRepository.save(member);

    // 회원정보 저장 후 가입한 이메일로 본인인증 메일 전송 및 레디스에 토큰값 저장
    String code = generateRandomUUID();

    try {
      sendVerificationEmail(requestDTO.getEmail(), code, "MOKUROKU 회원가입 인증메일", "join: ");
      return ResponseEntity.ok(new ResultDTO<>("회원가입에 성공했습니다. 인증을 위해 가입한 이메일의 메일을 확인해주세요.", null));
    } catch (Exception e) {
      throw new CustomException(ErrorCode.REDIS_CONNECTION_FAILED);
    }
  }

  public static String generateRandomUUID() {
    SecureRandom random = new SecureRandom();
    int code = random.nextInt(900000) + 100000; // 6자리 숫자
    return String.valueOf(code);
  }

  private void sendVerificationEmail(String email, String code, String title,
      String redisKeyPrefix) {
    String message = "<h3>5분안에 인증번호를 입력해주세요</h3> <br>" +
        "<h1>" + code + "</h1>";

    // 기존 코드가 있다면 삭제
    if (redisTemplate.opsForValue().get(redisKeyPrefix + email) != null) {
      redisTemplate.delete(redisKeyPrefix + email);
    }

    // 메일 전송
    mailComponents.sendMail(email, title, message);

    // Redis에 저장
    redisTemplate.opsForValue()
        .set(redisKeyPrefix + email, code, MAIL_EXPIRES_IN, TimeUnit.MILLISECONDS);
  }
}