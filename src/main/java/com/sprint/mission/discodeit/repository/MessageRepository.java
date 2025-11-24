package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("""
             SELECT m FROM Message m
             WHERE m.channel.id = :channelId
             ORDER BY m.createdAt DESC
            """)
    List<Message> findLatestByChannelId(UUID channelId, Pageable pageable);

    @Query("""
                SELECT m.id FROM Message m where m.channel.id = :channelId
            """)
    List<UUID> findIdsByChannelId(UUID channelId);

    List<Message> findAllByChannelId(UUID id);
}
