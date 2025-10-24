package com.sprint.mission.discodeit.store;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryStore {
    // NOTE: 원래 OOP 준수를 위해 캡슐화 getter, setter해야하지만 학습, 테스트 임시용을 위해 public으로 사용
    public final HashMap<UUID, User> users = new HashMap<>();
    public final HashMap<UUID, Channel> channels = new HashMap<>();
    public final HashMap<UUID, Message> messages = new HashMap<>();

    public final HashMap<UUID, BinaryContent> binaryContents = new HashMap<>();
}
