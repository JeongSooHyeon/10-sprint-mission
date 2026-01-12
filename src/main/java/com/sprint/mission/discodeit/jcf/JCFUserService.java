package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User create(String name, UserStatus status) {
        boolean isDuplicate = data.values().stream()
                .anyMatch(user -> user.getName().equals(name));
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        User user = new User(name, status);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(UUID id) {
        validateExistence(data, id, "조회");
        return data.get(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(User user) {
        validateExistence(data, user.getId(), "수정");
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(UUID id) {
        validateExistence(data, id, "삭제");
        data.remove(id);
    }

    private void validateExistence(Map<UUID, User> data, UUID id, String action){
        if (!data.containsKey(id)) {
            throw new NoSuchElementException(action + " 실패 : 존재하지 않는 사용자 ID입니다.");
        }
    }
}
