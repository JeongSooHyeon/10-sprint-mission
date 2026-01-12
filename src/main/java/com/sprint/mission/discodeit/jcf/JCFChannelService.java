package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.IsPrivate;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(String name, IsPrivate isPrivate){
        Channel channel = new Channel(name, isPrivate);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        validateExistence(data, id, "조회");
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(Channel channel) {
        validateExistence(data, channel.getId(), "수정");
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public void delete(UUID id) {
        validateExistence(data, id, "삭제");
        data.remove(id);
    }

    private void validateExistence(Map<UUID, Channel> data, UUID id, String action){
        if (!data.containsKey(id)) {
            throw new NoSuchElementException(action + " 실패 : 존재하지 않는 채널 ID입니다.");
        }
    }
}
