package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    UserRepository userRepository;


    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String name, UserStatus status) {
        List<User> data = userRepository.readAll();
        boolean isDuplicate = data.stream()
                .anyMatch(user -> user.getName().equals(name));
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        User user = new User(name, status);
        return userRepository.save(user);
    }

    @Override
    public User findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException("실패 : 존재하지 않는 사용자 ID입니다."));
        return user;
    }

    @Override
    public List<User> readAll() {
        return userRepository.readAll();
    }

    @Override
    public User update(UUID id, String newName, UserStatus newStatus) {
        User user = findById(id);   // 예외 검사

        user.updateName(newName);
        user.updateStatus(newStatus);

        userRepository.update(user);
        return user;
    }

    @Override
    public void printUserMessages(UUID id) {
        User user = findById(id);
        if (!user.getMessages().isEmpty()) {
            String allMessages = user.getMessages().stream()
                    .map(msg -> String.format("- [%s] %s",
                            msg.getChannel().getName(),
                            msg.getContent()))
                    .collect(Collectors.joining("\n"));
            System.out.println("[" + user.getName() + "님이 보낸 메시지 내역]\n" + allMessages);
        } else {
            System.out.println(user.getName() + "님이 보낸 메시지가 없습니다.");
        }
    }

    @Override
    public void printUserChannels(UUID id) {
        User user = findById(id);
        String result = user.getChannels().stream()
                .map(Channel::getName)
                .collect(Collectors.joining("\n"));
        System.out.println("[" + user.getName() + "님의 채널들]");
        if (result.isEmpty()) {
            System.out.println("보유 채널이 없습니다.");
        } else {
            System.out.println(result);
        }
    }

    @Override
    public void delete(UUID id) {
        User user = findById(id);   // 예외 검사

        userRepository.delete(id);
    }

    private void validateExistence(Map<UUID, User> data, UUID id) {
        if (!data.containsKey(id)) {
            throw new NoSuchElementException("실패 : 존재하지 않는 사용자 ID입니다.");
        }
    }
}
