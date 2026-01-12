package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private UserService userService;
    private ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(User user, Channel channel, String content) {
        Message message = new Message(user, channel, content);
        try {
            userService.findById(message.getSender().getId());
            channelService.findById(message.getChannelId());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("메시지 생성 실패 - " + e.getMessage());
        }
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        validateExistence(data, id, "조회");
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(Message message) {
        validateExistence(data, message.getId(), "수정");
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public void delete(UUID id) {
        validateExistence(data, id, "삭제");
        data.remove(id);
    }

    private void validateExistence(Map<UUID, Message> data, UUID id, String action){
        if (!data.containsKey(id)) {
            throw new NoSuchElementException(action + " 실패 : 존재하지 않는 메시지 ID입니다.");
        }
    }
}
