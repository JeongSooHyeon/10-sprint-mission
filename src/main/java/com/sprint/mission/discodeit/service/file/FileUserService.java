package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ClearMemory;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class FileUserService implements UserService, ClearMemory {
    UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String name, UserStatus status) {
        userRepository.findByName(name)
                .ifPresent(u -> { throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");});
        User user = new User(name, status);
        return userRepository.save(user);
    }

    @Override
    public User findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("실패 : 존재하지 않는 사용자 ID입니다."));
        return user;
    }

    @Override
    public List<User> readAll() {
        return userRepository.readAll();
    }

    @Override
    public User update(UUID id, String newName, UserStatus newStatus) {
        User user = findById(id);
        user.updateName(newName);
        user.updateStatus(newStatus);
        return userRepository.save(user);
    }

    @Override
    public List<Message> getUserMessages(UUID id) {
        User user = findById(id);
        return user.getMessages();

    }

    @Override
    public void delete(UUID id) {
        findById(id);
        userRepository.delete(id);
    }


    @Override
    public  List<Channel> getUserChannels(UUID id) {
        User user = findById(id);
        return user.getChannels();
    }

    @Override
    public void clear() {
        userRepository.clear();
    }
}
