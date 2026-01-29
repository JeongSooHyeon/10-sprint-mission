package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Getter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private List<Message> messages;
    private List<Channel> channels;
    private UUID profileId;

    public User(String name, String email, UUID profileId) {
        super(UUID.randomUUID(), Instant.now());
        this.name = name;
        this.email = email;
        this.messages = new ArrayList<Message>();
        this.channels = new ArrayList<Channel>();
        this.profileId = profileId;
    }

    public void addMessages(Message message) {
        this.messages.add(message);
        if (message.getSender() != this) {
            message.addSender(this);
        }
    }

    public void addChannel(Channel channel) {
        if (!this.channels.contains(channel)) {
            this.channels.add(channel);
        }

        // 채널장이 비어있으면 방장 등록
        if (channel.getOwner() == null) {
            channel.addOwner(this);
        }

        // 채널에 멤버로 추가
        if (!channel.getUsers().contains(this)) {
            channel.addUser(this);
        }
    }

    public void updateProfileId(UUID id) {
        this.profileId = id;
    }
    public void updateName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "유저명 : " + name;
    }


}
