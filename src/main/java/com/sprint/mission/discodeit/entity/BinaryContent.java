package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor
public class BinaryContent extends BaseEntity {

  @Column(nullable = false)
  private String contentType;

  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private Long size;

  public BinaryContent(String contentType, Long size, String fileName) {
    this.contentType = contentType;
    this.fileName = fileName;
    this.size = size;
  }
}