package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {

    public FileChannelRepository(@Value("${discodeit.repository.file-directory:.discodeit}")String directoryPath) {
        super(directoryPath + File.separator + "Channel.ser");
    }

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> data = load();
        data.put(channel.getId(), channel);
        writeToFile(data);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Map<UUID, Channel> data = load();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        Map<UUID, Channel> data = load();
        return data.values().stream()
                .filter(ch -> ch.getUserIds().stream()
                        .anyMatch(uId -> uId.equals(userId)))
                .toList();
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> data = load();
        return List.copyOf(data.values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> data = load();
        data.remove(id);
        writeToFile(data);
    }

    @Override
    public void deleteMessageByMessageId(UUID channelId, UUID messageId) {
        Map<UUID, Channel> data = load();
        Channel channel = data.get(channelId);
        if (channel != null) {
            if (channel.getMessageIds().removeIf(id -> id.equals(messageId))) {
                writeToFile(data);
            }
        }
    }

    @Override
    public void clear() {
        writeToFile(new HashMap<UUID, Channel>());
    }
}
