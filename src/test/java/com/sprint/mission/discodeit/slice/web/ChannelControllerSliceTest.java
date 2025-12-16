package com.sprint.mission.discodeit.slice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChannelController.class)
public class ChannelControllerSliceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ChannelService channelService;

    @Nested
    class CreateChannel {

        @Test
        void create_public_channel_returns_201() throws Exception {
            // given
            Map<String, Object> body = Map.of(
                    "name", "public-channel",
                    "description", "desc"
            );

            UUID channelId = UUID.randomUUID();

            when(channelService.createChannel(any(ChannelCreateCommand.class)))
                    .thenReturn(new ChannelResponseDto(
                            channelId,
                            "public-channel",
                            "desc",
                            null,
                            ChannelType.PUBLIC,
                            Instant.now()
                    ));

            // when & then
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andDo(print())
                    .andExpect(status().isCreated());

            // 타입까지 검증 (중요)
            verify(channelService).createChannel(argThat(cmd ->
                    cmd.type() == ChannelType.PUBLIC
            ));
        }

        @Test
        void create_public_channel_returns_400_when_name_blank() throws Exception {
            // given
            Map<String, Object> body = Map.of(
                    "name", "   ",          // blank
                    "description", "desc"
            );

            // when & then
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(channelService, never()).createChannel(any());
        }

        @Test
        void create_public_channel_returns_400_when_description_blank() throws Exception {
            // given
            Map<String, Object> body = Map.of(
                    "name", "public-channel",
                    "description", " "      // blank
            );

            // when & then
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(channelService, never()).createChannel(any());
        }

        @Test
        void create_private_channel_returns_201() throws Exception {
            // given
            Map<String, Object> body = Map.of(
                    "memberIds" , List.of(UUID.randomUUID(), UUID.randomUUID())
            );

            UUID channelId = UUID.randomUUID();


            UserResponseDto user1 = new UserResponseDto(
                    UUID.randomUUID(),
                    "user1",
                    "user1@test.com",
                    null,
                    true,
                    Instant.now(),
                    Instant.now()
            );

            UserResponseDto user2 = new UserResponseDto(
                    UUID.randomUUID(),
                    "user2",
                    "user2@test.com",
                    null,
                    true,
                    Instant.now(),
                    Instant.now()
            );
            List<UserResponseDto> participants = List.of(user1, user2);
            when(channelService.createChannel(any(ChannelCreateCommand.class)))
                    .thenReturn(new ChannelResponseDto(
                            channelId,
                            null,
                            null,
                            participants,
                            ChannelType.PRIVATE,
                            Instant.now()
                    ));

            // when & then
            mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andDo(print())
                    .andExpect(status().isCreated());

            verify(channelService).createChannel(argThat(cmd ->
                    cmd.type() == ChannelType.PRIVATE
            ));
        }

        @Test
        void create_channel_returns_400_when_name_blank() throws Exception {
            // given
            Map<String, Object> body = Map.of(
                    "name", " ",
                    "description", "desc"
            );

            // when & then
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(channelService, never()).createChannel(any());
        }
    }

}
