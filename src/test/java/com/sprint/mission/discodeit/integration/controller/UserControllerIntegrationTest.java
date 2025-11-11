package com.sprint.mission.discodeit.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.RoleType;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private UserController userController;
    private UserService userService;
    private UserReader userReader;
    private UserRepository userRepository;
    private UserStatusService userStatusService;
    private UserStatusRepository userStatusRepository;
    private BinaryContentRepository binaryContentRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userReader = new UserReader(userRepository);
        userStatusRepository = new JCFUserStatusRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        userStatusService = new BasicUserStatusService(userReader, userStatusRepository);
        userService = new BasicUserService(
                userRepository,
                userReader,
                userStatusService,
                userStatusRepository,
                binaryContentRepository
        );

    }


    @Nested
    @DisplayName("signup")
    class UserSignup {

        @Test
        void signup_then_success() throws Exception {
            // given
            final String uri ="/api/users";
            User user = User.create("test", "emaile@edsd.com", "password", RoleType.USER, "010-1111-1111", null);

            // dto
            UserSignupRequestDto request = UserSignupRequestDto.builder()
                    .username(user.getNickname())
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
                    } );
        }


    }


}
