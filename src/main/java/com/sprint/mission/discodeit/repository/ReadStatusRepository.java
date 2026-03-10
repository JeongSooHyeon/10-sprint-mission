package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  void deleteByChannelId(UUID channelId);

  void deleteByUserId(UUID userId);

  Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  @Query("SELECT rs FROM ReadStatus rs JOIN FETCH rs.user JOIN FETCH rs.channel WHERE rs.user.id = :userId")
  List<ReadStatus> findAllByUserId(UUID userId);

  // 채널 ID 목록으로 ReadStatus 한번에 조회
  @Query("SELECT r FROM ReadStatus r JOIN FETCH r.user WHERE r.channel.id IN :channelIds")
  List<ReadStatus> findAllByChannelIdIn(@Param("channelIds") List<UUID> channelIds);

}
