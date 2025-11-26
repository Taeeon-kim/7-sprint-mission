package com.sprint.mission.discodeit.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.entity.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Nested
    @DisplayName("signup")
    class UserSignup {

        @Test
        void signup_then_success() throws Exception {
            // given
            final String uri = "/api/users";
            User user = User.create("test22", "emaile@edsd.com", "password", null);

            // dto
            UserSignupRequestDto request = UserSignupRequestDto.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();
            String requestJson = objectMapper.writeValueAsString(request);

            MockMultipartFile multipartFile = new MockMultipartFile(
                    "userCreateRequest",
                    "userCreateRequest.json",
                    MediaType.APPLICATION_JSON_VALUE,
                    requestJson.getBytes(StandardCharsets.UTF_8)
            );

            // when
            ResultActions actions = mockMvc.perform(
                    multipart(uri)
                            .file(multipartFile)
                            .characterEncoding("UTF-8")
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .accept(MediaType.APPLICATION_JSON)

            );


            actions.andExpect(status().isCreated())
                    .andExpect(result -> {
                        String uuid = result.getResponse().getContentAsString();
                        assertNotNull(uuid);
                    });
        }


    }


}
