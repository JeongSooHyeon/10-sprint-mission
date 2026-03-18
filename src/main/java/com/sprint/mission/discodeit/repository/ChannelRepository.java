package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {


  // PUBLIC 채널이거나 내 ReadStatus가 존재하는 채널
  @Query("SELECT c FROM Channel c WHERE c.type = 'PUBLIC' " +
      "OR EXISTS (SELECT r FROM ReadStatus r WHERE r.channel = c AND r.user.id = :userId)")
  List<Channel> findAllByUserId(@Param("userId") UUID userId);

}
