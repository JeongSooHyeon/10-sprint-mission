package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.io.IOException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {


}
