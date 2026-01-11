package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.status.UserActiveStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;

@Mapper( // NOTE: 스프링빈 등록, @Component 불필요
        componentModel = "spring",
        uses = { BinaryContentMapper.class}
)
public interface UserMapper {

    @Mapping(target = "profile", source = "profile")
    @Mapping(target = "online", source = "userStatus", qualifiedByName = "mapUserActiveStatus")
    UserResponseDto toDto(User user);




    @Named("mapUserActiveStatus")
    default boolean mapUserActiveStatus(UserStatus userStatus) {
        UserActiveStatus activeStatus = Optional.ofNullable(userStatus)
                .map(UserStatus::getUserStatus)
                .orElse(UserActiveStatus.OFFLINE);

        return activeStatus == UserActiveStatus.ONLINE;
    }
}
