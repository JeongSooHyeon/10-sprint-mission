package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;

    public JCFChannelRepository(){
        data = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(ch -> ch.getUserIds().stream()
                        .anyMatch(uid -> uid.equals(userId)))
                .toList();
    }

    @Override
    public List<Channel> findAll() {
        return List.copyOf(data.values());

    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteMessageByMessageId(UUID channelId, UUID messageId) {
        Channel channel = data.get(channelId);
        if (channel != null) {
            channel.getMessageIds().removeIf(id -> id.equals(messageId));
        }
    }

    @Override
    public void clear(){
        this.data.clear();
    }
}
