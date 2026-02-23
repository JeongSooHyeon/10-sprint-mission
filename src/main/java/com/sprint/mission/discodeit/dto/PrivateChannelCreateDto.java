package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateDto(
    List<UUID> participantIds // 방장 포함 채널 참여자
) {

}
