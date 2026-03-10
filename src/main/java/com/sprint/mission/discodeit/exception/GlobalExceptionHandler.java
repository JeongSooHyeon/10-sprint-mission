package com.sprint.mission.discodeit.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Map<String, Object>> handleNoSuchElement(NoSuchElementException e,
      HttpServletRequest request) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", HttpStatus.NOT_FOUND.value());
    errorResponse.put("error", "해당 리소스를 찾을 수 없습니다.");
    errorResponse.put("message", e.getMessage());
    errorResponse.put("path", request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }
    
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e,
      HttpServletRequest request) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("error", "잘못된 요청입니다.");
    errorResponse.put("message", e.getMessage());
    errorResponse.put("path", request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException e,
      HttpServletRequest request) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorResponse.put("error", "서버 내부 오류입니다.");
    errorResponse.put("message", e.getMessage());
    errorResponse.put("path", request.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

}
