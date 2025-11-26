package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    List<ReadStatus> findByChannelId(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);

    boolean existsByUserAndChannel(User user, Channel channel);

    Optional<ReadStatus> findByUserAndChannel(User user, Channel channel);

    @Query("""
            SELECT rs.user.id
            FROM ReadStatus rs
            WHERE rs.channel.id = :channelId
            """)
    List<UUID> findUserIdsByChannelId(UUID channelId
    );

    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    void deleteByChannelId(UUID channelId);
}
