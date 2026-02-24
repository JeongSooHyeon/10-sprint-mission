package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class User extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private String email;
  private String password;
  private List<UUID> messageIds;
  private List<UUID> channelIds;
  private UUID profileId;

  public User(String name, String email, String password, UUID profileId) {
    super(UUID.randomUUID(), Instant.now());
    this.name = name;
    this.email = email;
    this.password = password;
    this.messageIds = new ArrayList<>();
    this.channelIds = new ArrayList<>();
    this.profileId = profileId;
  }

  public User update(UserUpdateRequest dto) {
    boolean anyValueUpdated = false;
    if (dto.newUsername() != null && !dto.newUsername().equals(this.name)) {
      this.name = dto.newUsername();
      anyValueUpdated = true;
    }

    if (dto.newProfileId() != null && !dto.newProfileId().equals(this.profileId)) {
      this.profileId = dto.newProfileId();
      anyValueUpdated = true;
    }

    if (dto.newEmail() != null && !dto.newEmail().equals(this.email)) {
      this.email = dto.newEmail();
      anyValueUpdated = true;
    }

    if (dto.newPassword() != null && !dto.newPassword().equals(this.password)) {
      this.password = dto.newPassword();
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }

    return this;
  }

  public void addMessages(UUID messageId) {
    this.messageIds.add(messageId);
  }

  public void addChannel(UUID channelId) {
    if (!this.channelIds.contains(channelId)) {
      this.channelIds.add(channelId);
    }
  }

  public void updateProfileId(UUID id) {
    this.profileId = id;
    this.onUpdate();
  }

  public void updateName(String name) {
    this.name = name;
    this.onUpdate();
  }

  @Override
  public String toString() {
    return "유저명 : " + name;
  }


}
