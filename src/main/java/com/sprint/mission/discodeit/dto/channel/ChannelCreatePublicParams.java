package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

@Builder
public record ChannelCreatePublicParams(
        String title,
        String description
) {

    public static ChannelCreatePublicParams from(ChannelCreateCommand command) {
        // NOTE: 정규화(trim, null 등), 불변식, 입력 가드등 처리 가능 이점있음
        return ChannelCreatePublicParams.builder()
                .title(command.title())
                .description(command.description())
                .build();
    }

}
