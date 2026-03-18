//package com.sprint.mission.discodeit.repository.file;
//
//import com.sprint.mission.discodeit.entity.ReadStatus;
//import com.sprint.mission.discodeit.repository.ReadStatusRepository;
//import javax.swing.text.html.Option;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Repository;
//
//import java.io.File;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@ConditionalOnProperty(username = "discodeit.repository.type", havingValue = "file")
//public class FileReadStatusRepository extends AbstractFileRepository<ReadStatus> implements
//    ReadStatusRepository {
//
//  public FileReadStatusRepository(
//      @Value("${discodeit.repository.file-directory:.discodeit}") String directoryPath) {
//    super(directoryPath + File.separator + "ReadStatus.ser");
//  }
//
//  @Override
//  public void deleteByChannelId(UUID channelId) {
//    Map<UUID, ReadStatus> data = load();
//    if (data.values().removeIf(rs -> rs.getChannel().getId().equals(channelId))) {
//      writeToFile(data);
//    }
//  }
//
//  @Override
//  public void deleteByUserId(UUID userId) {
//    Map<UUID, ReadStatus> data = load();
//    if (data.values().removeIf(rs -> rs.getUser().getId().equals(userId))) {
//      writeToFile(data);
//    }
//  }
//
//  @Override
//  public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
//    Map<UUID, ReadStatus> data = load();
//    return data.values().stream()
//        .filter(rs ->
//            rs.getUser().getId().equals(userId)
//                && rs.getChannel().getId().equals(channelId))
//        .findFirst();
//  }
//
//  @Override
//  public List<ReadStatus> findAllByChannelId(UUID channelId) {
//    Map<UUID, ReadStatus> data = load();
//    return data.values().stream()
//        .filter(rs ->
//            rs.getChannel().getId().equals(channelId))
//        .toList();
//
//  }
//
//
//  @Override
//  public List<ReadStatus> findAllByUserId(UUID userId) {
//    Map<UUID, ReadStatus> data = load();
//    return data.values().stream()
//        .filter(rs -> rs.getUser().getId().equals(userId))
//        .toList();
//  }
//
//}
