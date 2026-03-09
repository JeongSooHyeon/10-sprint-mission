//package com.sprint.mission.discodeit.repository.jcf;
//
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Repository
//@ConditionalOnProperty(username = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
//public class JCFMessageRepository implements MessageRepository {
//
//  private final Map<UUID, Message> data;
//
//  public JCFMessageRepository() {
//    this.data = new HashMap<>();
//  }
//
//  @Override
//  public Message save(Message message) {
//    data.put(message.getId(), message);
//    return message;
//  }
//
//  @Override
//  public Optional<Message> findById(UUID id) {
//    return Optional.ofNullable(data.get(id));
//  }
//
//  @Override
//  public List<Message> findAllByUserId(UUID userId) {
//    return data.values().stream()
//        .filter(m -> m.getAuthor().getId().equals(userId))
//        .toList();
//  }
//
//  @Override
//  public List<Message> findAllByChannelId(UUID channelId) {
//    return data.values().stream()
//        .filter(m -> m.getChannel().getId().equals(channelId))
//        .sorted(Comparator.comparing(Message::getCreatedAt))
//        .toList();
//  }
//
//  @Override
//  public void deleteByChannelId(UUID channelId) {
//    data.values().removeIf(m -> m.getChannel().getId().equals(channelId));
//  }
//
//  @Override
//  public void deleteByUserId(UUID userId) {
//    data.values().removeIf(m -> m.getAuthor().getId().equals(userId));
//  }
//
//  @Override
//  public void delete(UUID id) {
//    data.remove(id);
//  }
//
//  @Override
//  public void clear() {
//    this.data.clear();
//  }
//}
