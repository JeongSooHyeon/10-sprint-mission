package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final File file;    // 클래스가 사용할 파일 저장소 객체 - 경로를 생성자에서 주입해 저장/불러오기 사용

    public FileUserRepository(String path) {
        this.file = new File(path);
    }

    public User save(User user) {
        Map<UUID, User> data = load();
        data.put(user.getId(), user);
        writeToFile(data);
        return user;
    }

    @SuppressWarnings("unchecked")
    private <T> Map<UUID, T> load() {
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToFile(Map<UUID, ?> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(UUID id) {
        Map<UUID, User> data = load();
        data.remove(id);
        writeToFile(data);
    }

    public Optional<User> findById(UUID id) {
        Map<UUID, User> data = load();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> findByName(String name) {
        Map<UUID, User> data = load();
        Optional<User> user = data.values().stream()
                .filter(u -> u.getName().equals(name))
                .findFirst();
        return user;
    }

    public List<User> readAll() {
        Map<UUID, User> data = load();
        return List.copyOf(data.values());
    }

    public void clear() {
        writeToFile(new HashMap<UUID, User>());
    }

}
