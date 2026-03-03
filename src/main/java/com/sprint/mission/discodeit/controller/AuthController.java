package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserLoginDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

  private final AuthService authService;

  // 로그인
  @Operation(summary = "로그인", description = "사용자 인증 수행")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class),
              examples = @ExampleObject(value = """
                  {
                    "userId": "550e8400-e29b-41d4-a716-446655440000",
                                        "createdAt": "2026-01-01T00:00:00Z",
                                        "updatedAt": "2026-02-20T14:00:00Z",
                                        "newUsername": "달선",
                                        "status": "ONLINE",
                                        "email": "dalsun@naver.com",
                                        "profileId": "3f3fb215-32aa-4281-904c-45b9dd8b96fb",
                                        "online": true
                     }
                  """))),
      @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않음"),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.")

  })

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public UserDto login(@RequestBody UserLoginDto dto) {
    System.out.println("newUsername = " + dto.username());
    System.out.println("password = " + dto.password());
    return authService.login(dto);
  }
}
