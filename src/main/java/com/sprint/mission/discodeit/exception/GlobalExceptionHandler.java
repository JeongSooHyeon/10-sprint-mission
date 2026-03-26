package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(ErrorResponse.of(e));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    Map<String, Object> details = new HashMap<>();
    e.getBindingResult().getFieldErrors()
        .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(
            Instant.now(),
            "VALIDATION_FAILED",
            "유효성 검증에 실패했습니다.",
            details,
            e.getClass().getSimpleName(),
            HttpStatus.BAD_REQUEST.value()
        ));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(
            Instant.now(),
            "INTERNAL_SERVER_ERROR",
            e.getMessage(),
            null,
            e.getClass().getSimpleName(),
            500));
  }
}
