package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void setUserStatus(UserStatus status) {
    this.status = status;
    if (status.getUser() != this) {
      status.setUser(this);
    }
  }

  public void setProfile(BinaryContent profile) {
    this.profile = profile;
  }

  public User update(String newUserName, BinaryContent newProfile, String newEmail,
      String newPassword) {
    boolean anyValueUpdated = false;
    if (newUserName != null && !newUserName.equals(this.username)) {
      this.username = newUserName;
      anyValueUpdated = true;
    }

    if (newProfile != null && !newProfile.getId().equals(this.profile.getId())) {
      this.profile = newProfile;
      anyValueUpdated = true;
    }

    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
      anyValueUpdated = true;
    }

    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }

    return this;
  }

  @Override
  public String toString() {
    return "유저명 : " + username;
  }

}
