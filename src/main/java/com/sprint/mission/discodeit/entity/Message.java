package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements  Serializable {
    private static final long serialVersionUID = 1L;

    private User sender;
    private Channel channel;
    private String content;
    private List<UUID> attachmentIds;   // 첨부파일

    public Message(User user, Channel channel, String content) {
        super(UUID.randomUUID(), Instant.now());
        this.channel = channel;
        this.content = content;
        this.addSender(user);
        this.attachmentIds = new ArrayList<>();
    }

    public void addSender(User user) {
        this.sender = user;
        if (!user.getMessages().contains(this)) {
            user.addMessages(this);
        }
    }

    public void setChannel(Channel channel) {
        this.channel = channel;

        if (!channel.getMessages().contains(this)) {
            channel.addMessage(this);
        }
    }

    public UUID getChannelId() {
        return channel.getId();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "보낸 사람 : " + sender.getName() + ", 내용 : " + content + ", 수정 시간 : " + updatedAt;
    }
}
