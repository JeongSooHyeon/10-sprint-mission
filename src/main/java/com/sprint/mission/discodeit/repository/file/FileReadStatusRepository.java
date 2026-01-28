package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public class FileReadStatusRepository extends AbstractFileRepository<ReadStatus> implements ReadStatusRepository {

    public FileReadStatusRepository() {
        super("ReadStatus.ser");
    }

}
