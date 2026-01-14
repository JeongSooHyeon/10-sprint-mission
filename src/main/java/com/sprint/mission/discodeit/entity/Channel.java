package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends BaseEntity{
    private String channelName;
    private List<User> participants = new ArrayList<>();

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelInfo(String newChannelName){
        this.channelName = newChannelName;
        super.setUpdatedAt(System.currentTimeMillis());
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void addParticipant(User user){
       this.participants.add(user);
    }

    public Channel(String channelName) {
        this.channelName = channelName;
    }
}
