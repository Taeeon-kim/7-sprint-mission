package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {


    // userId + channelId 복합 Unique 제약으로 인해 DISTINCT 불필요
    @Query("""
                SELECT c
                FROM Channel c
                LEFT JOIN ReadStatus rs ON rs.channel = c
                WHERE c.type = 'PUBLIC'
                   OR rs.user.id = :userId
            """)
    List<Channel> findAllVisibleByUserId(UUID userId);
}
