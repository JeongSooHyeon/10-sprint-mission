package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        System.out.println("========== 디스코드 서비스 테스트 시작 ==========\n");

        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService(userService);
        MessageService messageService = new JCFMessageService(userService, channelService);

//        // User 테스트
//        System.out.println("유저 기능 테스트");
//
//        // 유저 등록
//        User createdUser = userService.createUser("임지호", "1234", "jiho@codeit.com");
//        UUID createdUserId = createdUser.getId();
//
//        User createdUser2 = userService.createUser("홍길동", "gildong", "gildong@codeit.com");
//        UUID createdUserId2 = createdUser2.getId();
//
//        // 단건 조회
//        System.out.println("\n단건 조회 시도:");
//        User foundUser = userService.findUserById(createdUserId);
//        System.out.println("조회된 유저: " + foundUser.getUsername());
//
//        // 다건 조회
//        System.out.println("\n전체 조회 시도:");
//        List<User> allUsers = userService.findAllUsers();
//        System.out.println("총 유저 수: " + allUsers.size() + "명");
//
//        // 수정
//        System.out.println("\n>> 유저 정보 수정 시도:");
//
//        User updatedUser = userService.updateUserInfo(createdUserId, "아임지호", "newjiho@codeit.com");
//        System.out.println("이름 변경 확인: " + updatedUser.getUsername());
//        updatedUser = userService.changePassword(createdUserId, "234234");
//        System.out.println("비밀번호 변경 확인: " + updatedUser.getPassword());
//
//        System.out.println("\n------------------------------------------\n");
//
//        User updatedUser2 = userService.updateUserInfo(createdUserId2, null, "newgildong@naver.com");
//        System.out.println(updatedUser2);
//        updatedUser2 = userService.updateUserInfo(createdUserId2, "홍 길동", "new@codeit.kr");
//        System.out.println(updatedUser2);
//        updatedUser2 = userService.updateUserInfo(createdUserId2, null, null);
//        System.out.println(updatedUser2);
//        updatedUser2 = userService.updateUserInfo(createdUserId2, "뉴길동", null);
//        System.out.println(updatedUser2);
//
//        System.out.println("최종 상태: " + updatedUser);
//
//        System.out.println("\n------------------------------------------\n");
//
//        // Channel 테스트
//        System.out.println("채널 기능 테스트");
//
//        // 채널 등록
//        Channel newChannel = channelService.createChannel("본방");
//        UUID createdChannelId = newChannel.getId();
//
//        // 조회
//        Channel foundChannel = channelService.findChannelById(createdChannelId);
//        System.out.println("생성된 채널명: " + foundChannel.getChannelName());
//
//        // 수정
//        Channel updatedChannel = channelService.updateChannel(createdChannelId, "공지방");
//
//        // 수정 확인
//        System.out.println("변경된 채널명: " + updatedChannel.getChannelName());
//
//
//        System.out.println("\n------------------------------------------\n");
//
//        // 메시지 기능 테스트
//        System.out.println("\n메시지 기능 테스트");
//
//        // 생성된 유저가 채널에 글 작성
//        Message msg1 = messageService.createMessage("안녕하세요", createdChannelId, createdUserId);
//        Message msg2 = messageService.createMessage("반갑습니다", createdChannelId, createdUserId);
//        UUID msg1Id = msg1.getId();
//        UUID msg2Id = msg2.getId();
//
//        // 특정 채널의 메시지 목록 조회
//        System.out.println(">> [" + foundChannel.getChannelName() + "] 채널의 메시지 목록:");
//        List<Message> channelMessages = messageService.findMessagesByChannelId(createdChannelId);
//
//        for (Message msg : channelMessages) {
//            System.out.println("- 내용: " + msg.getContent());
//        }
//
//        // 메시지 수정
//        System.out.println("\n");
//        msg1 = messageService.updateMessage(msg1Id, "자바 실습 중");
////        messageService.updateMessage(msg1.getId(), "안녕하세요! (수정됨)");
//        System.out.println("수정 후 내용: " + msg1.getContent());
//
//        // 메시지 삭제
//        System.out.println("\n>> 메시지 삭제 시도:");
//        messageService.deleteMessage(msg2.getId());
//
//        // 유저 삭제
//        System.out.println("\n>> 유저 삭제 확인:");
//        userService.deleteUser(createdUserId);
//        try {
//            userService.findUserById(createdUserId);
//        } catch (IllegalArgumentException e) {
//            System.out.println("에러 발생: " + e.getMessage());
//        }
//
//        // 채널 삭제
//        channelService.deleteChannel(createdChannelId);
//        System.out.println("\n>> 채널 삭제 확인:");
//        try {
//            channelService.findChannelById(createdChannelId);
//        } catch (IllegalArgumentException e) {
//            System.out.println("에러 발생: " + e.getMessage());
//        }
//
//        System.out.println("\n========== 테스트 종료 ==========");

        // 데이터 준비 (유저 2명, 채널 1개)
        System.out.println(">> 유저 및 채널 생성 중");
        User user1 = userService.createUser("임지호", "1234", "jiho@codeit.com");
        User user2 = userService.createUser("홍길동", "5678", "hong@codeit.com");
        Channel channel = channelService.createChannel("수다방");

        UUID user1Id = user1.getId();
        UUID user2Id = user2.getId();
        UUID channelId = channel.getId();


        System.out.println("\n------------------------------------------\n");


        // 채널 입장 테스트 (joinChannel)
        System.out.println(">> [테스트 1] 채널 입장 (joinChannel)");
        try {
            channelService.joinChannel(user1Id, channelId);
            channelService.joinChannel(user2Id, channelId);

            // 중복 입장 테스트
            System.out.println("\n(중복 입장 시도 -> 에러 예상)");
            channelService.joinChannel(user1Id, channelId);
        } catch (IllegalArgumentException e) {
            System.out.println("중복 방지 성공: " + e.getMessage());
        }


        System.out.println("\n------------------------------------------\n");


        // 채널 참가자 조회(findParticipantsByChannelId)
        System.out.println(">>채널 참가자 리스트 조회");
        List<User> participants = channelService.findParticipants(channelId);

        System.out.println("[" + channel.getChannelName() + "] 현재 참가자 수: " + participants.size() + "명");
        for (User p : participants) {
            System.out.println("- 참가자: " + p.getUsername() + " (" + p.getEmail() + ")");
        }


        System.out.println("\n------------------------------------------\n");


        // 메시지 작성 (데이터 쌓기)
        System.out.println(">> 메시지 작성 중");
        messageService.createMessage("안녕하세요! 지호입니다.", channelId, user1Id);
        messageService.createMessage("반갑습니다~ 길동이에요.", channelId, user2Id);
        messageService.createMessage("오늘 점심 뭐 드실래요?", channelId, user2Id);


        // 특정 유저의 메시지 조회 (findMessagesByUserId)
        System.out.println("\n>> [테스트 3] '홍길동'이 작성한 메시지 내역 조회");

        // 홍길동(user2)이 쓴 글만 가져오기
        List<Message> gildongMessages = messageService.findMessagesByUserId(user2Id);

        System.out.println("홍길동 작성글 개수: " + gildongMessages.size() + "개");
        for (Message msg : gildongMessages) {
            System.out.println("📝 내용: " + msg.getContent() + " | 채널: " + msg.getChannel().getChannelName());
        }


        System.out.println("\n========== 모든 테스트 종료 ==========");
    }
}
