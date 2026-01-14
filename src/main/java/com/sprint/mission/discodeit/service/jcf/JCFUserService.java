package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> userList;
    public JCFUserService(){
        this.userList = new ArrayList<>();
    }

    @Override
    public User createUser(String username, String password, String email) {
        User newUser = new User(username, password, email);
        userList.add(newUser);
        System.out.println(username + "님 회원가입 완료되었습니다.");
        return newUser;
    }

    @Override
    public User findUserById(UUID id){
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 없음"));
    }

    @Override
    public List<User> findAllUsers(){
        return userList;
    }

    @Override
    public User updateUserInfo(UUID id, String newUsername, String newEmail){
        User targetUser = findUserById(id);
        try{
            validateUpdateInfo(newUsername, newEmail);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return targetUser;
        }

        if (newUsername != null){
            targetUser.updateUsername(newUsername);
            System.out.println("이름이 변경되었습니다: " + targetUser.getUsername());
        }
        if (newEmail != null){
            targetUser.updateEmail(newEmail);
            System.out.println("이메일이 변경되었습니다: " + targetUser.getEmail());
        }
        return targetUser;
    }

    public void validateUpdateInfo(String name, String email){
        if (name == null && email == null){
            throw new IllegalArgumentException("둘 중 하나는 입력해야 합니다.");
        }
        if (name != null && name.contains(" ")){
            throw new IllegalArgumentException("띄어쓰기는 포함할 수 없습니다.");
        }
        if (email != null && !email.contains("@")){
            throw new IllegalArgumentException("이메일 형식이 잘못되었습니다.");
        }
        if (email != null && email.contains(" ")){
            throw new IllegalArgumentException("띄어쓰기는 포함할 수 없습니다.");
        }
    }

    @Override
    public void deleteUser(UUID id){
        User targetUser = findUserById(id);
        userList.remove(targetUser);
        System.out.println(targetUser.getUsername() + "님 삭제 완료되었습니다");
    }

    @Override
    public User changePassword(UUID id, String newPassword) {
        User targetUser = findUserById(id);
        targetUser.updatePassword(newPassword);
        return targetUser;
    }

}
