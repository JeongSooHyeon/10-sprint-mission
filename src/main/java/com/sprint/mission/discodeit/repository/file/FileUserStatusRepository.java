package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserStatusRepository extends AbstractFileRepository<UserStatus> implements UserStatusRepository {

    public FileUserStatusRepository() {
        super("UserStatus.ser");
    }

    @Override
    public void save(UserStatus userStatus) {
        Map<UUID, UserStatus> data = load();
        data.put(userStatus.getId(), userStatus);
        writeToFile(data);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Map<UUID, UserStatus> data = load();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        Map<UUID, UserStatus> data = load();
        Optional<UserStatus> userStatus = data.values().stream()
                .filter(us -> us.getUserId().equals(userId))
                .findAny();
        return userStatus;
    }
}
