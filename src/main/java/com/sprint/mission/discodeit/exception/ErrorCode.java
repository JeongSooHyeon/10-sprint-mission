package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
  USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 상태 정보가 존재하지 않습니다."),
  USER_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자 상태입니다."),

  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),

  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 파일입니다."),

  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채널입니다."),
  PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "PRIVATE 채널은 수정할 수 없습니다."),

  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메시지입니다."),

  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 ReadStatus입니다."),

  STORAGE_INIT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "저장소를 초기화할 수 없습니다."),
  FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다."),
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;


}
