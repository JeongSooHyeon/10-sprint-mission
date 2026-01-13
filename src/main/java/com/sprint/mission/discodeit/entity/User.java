package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private UserStatus status;
    private List<Message> messages;
    private List<Channel> channels;

    public User(String name, UserStatus status) {
        super(UUID.randomUUID(), System.currentTimeMillis());
        this.name = name;
        this.status = status;
        this.messages = new ArrayList<Message>();
        this.channels = new ArrayList<Channel>();
    }

    public void addMessages(Message message) {
        this.messages.add(message);
        if (message.getSender() != this) {
            message.addUser(this);
        }
    }

    public void addChannel(Channel channel) {
        this.channels.add(channel);
        if (channel.getOwner() != this) {
            channel.addOwner(this);
        }
    }

    public String getName() {
        return name;
    }

    public UserStatus getStatus() {
        return status;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "유저명 : " + name + ", 상태 : " + status + ", 보유 채널들 : " + getChannels();
    }


}
