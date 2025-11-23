package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    @Query("""
            SELECT rs.channel
            FROM ReadStatus rs
            WHERE rs.user.id = :userId
            """)
    List<Channel> findAllByUserId(UUID userId);
}
