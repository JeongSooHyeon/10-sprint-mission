package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  UserStatusRepository userStatusRepository;

  @Test
  @DisplayName("전체 사용자 조회 성공 - status, profile fetch join")
  void findAll_success() {
    // given
    User user1 = new User("달선", "dalsun@naver.com", "ekftjs123", null);
    User user2 = new User("달룡", "dalyong@naver.com", "ekffyd123", null);
    userRepository.save(user1);
    userRepository.save(user2);
    userStatusRepository.save(new UserStatus(user1, Instant.now()));
    userStatusRepository.save(new UserStatus(user2, Instant.now()));
    // when
    List<User> result = userRepository.findAll();

    // then
    assertThat(result).hasSize(2);
    assertThat(result).extracting("username")
        .containsExactlyInAnyOrder("달선", "달룡");
  }

  @Test
  @DisplayName("전체 사용자 조회 성공 - 사용자 없음")
  void findAll_empty() {
    // when
    List<User> result = userRepository.findAll();

    // then
    assertThat(result).isEmpty();
  }
}