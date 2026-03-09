package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  @Query("SELECT m FROM Message m JOIN FETCH m.author JOIN FETCH m.channel LEFT JOIN FETCH m.attachments WHERE m.author.id = :userId")
  Slice<Message> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

  @Query("SELECT m FROM Message m JOIN FETCH m.author JOIN FETCH m.channel LEFT JOIN FETCH m.attachments WHERE m.channel.id = :channelId")
  Slice<Message> findAllByChannelId(@Param("channelId") UUID channelId, Pageable pageable);

  void deleteByChannelId(UUID channelId);

  void deleteByAuthorId(UUID authorId);

  Optional<Message> findFirstByChannelIdOrderByCreatedAtDesc(UUID channelId);

  Slice<Message> findAllByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);

  Slice<Message> findByChannelIdAndContentContaining(
      UUID channelId,
      String keyword,
      Pageable pageable);

}
