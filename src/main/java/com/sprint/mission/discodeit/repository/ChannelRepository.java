package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {


    // TODO: DISTINCT 관련해서 더 공부할것 내부적으로 어느시점에 필요한지 JOIN, WHERE
    @Query("""
                SELECT DISTINCT c
                FROM Channel c
                LEFT JOIN ReadStatus rs ON rs.channel = c
                WHERE c.type = 'PUBLIC'
                   OR rs.user.id = :userId
            """)
    List<Channel> findAllVisibleByUserId(UUID userId);
}
