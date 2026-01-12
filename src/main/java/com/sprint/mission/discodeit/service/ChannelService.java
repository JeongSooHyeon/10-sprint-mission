package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.IsPrivate;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // Create
    public Channel create(String name, IsPrivate isPrivate);

    // Read
    Channel findById(UUID id);

    // ReadAll
    List<Channel> readAll();

    // Update
    Channel update(Channel channel);

    // Delete
    void delete(UUID id);


}
