package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository {
    void save(ReadStatus readStatus);

}
