package com.sprint.mission.discodeit.unit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelInvalidParticipantsException;
import com.sprint.mission.discodeit.exception.channel.ChannelMinimumMembersNotMetException;
import com.sprint.mission.discodeit.exception.channel.ChannelModificationNotAllowedException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.factory.ChannelFactory;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceUnitTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserReader userReader;

    @Mock
    private ChannelReader channelReader;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private ChannelMapper channelMapper;

    @Mock
    private ChannelFactory channelFactory;

    @InjectMocks
    private BasicChannelService channelService;

    @Nested
    @DisplayName("채널 생성 - public, private")
    class CreateChannel {

        @Test
        @DisplayName("[Behavior + Branch][Positive] PUBLIC 채널 생성 - 정상적으로 channelFactory, channerRepository, channelMapper 위임")
        void createPublicChannel_shouldDelegateToFactorRepoMapper() {

            // given
            ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto("채널 이름", "채널 설명입니다.", null);
            ChannelCreateCommand command = ChannelCreateCommand.from(channelCreateRequestDto, ChannelType.PUBLIC);
            Channel publicChannel = mock(Channel.class);
            ChannelResponseDto responseDto = mock(ChannelResponseDto.class);

            given(channelFactory.create(command)).willReturn(publicChannel);
            given(channelRepository.save(publicChannel)).willReturn(publicChannel);
            given(channelMapper.toDto(publicChannel)).willReturn(responseDto);

            // when
            ChannelResponseDto channelResponseDto = channelService.createChannel(command);

            // then
            assertSame(responseDto, channelResponseDto); // 결과검증

            // 행위검증
            InOrder inOrder = inOrder(channelFactory, channelRepository, channelMapper);
            inOrder.verify(channelFactory).create(command);
            inOrder.verify(channelRepository).save(publicChannel);
            inOrder.verify(channelMapper).toDto(publicChannel);

            then(channelFactory).shouldHaveNoMoreInteractions();
            then(channelRepository).shouldHaveNoMoreInteractions();
            then(channelMapper).shouldHaveNoMoreInteractions();

            then(userReader).should(never()).findUsersByIds(any());
        }

        @Test
        @DisplayName("[Behavior + Branch][Positive] PRIVATE 채널 생성 - userReader -> factory -> repo -> mapper 순서로 위임")
        void createPrivateChannel_shouldDelegateAndCallUserReader() {
            // given
            List<UUID> memberIds = List.of(UUID.randomUUID(), UUID.randomUUID());
            ChannelCreateRequestDto dto = new ChannelCreateRequestDto(null, null, memberIds);
            ChannelCreateCommand command = ChannelCreateCommand.from(dto, ChannelType.PRIVATE);

            List<User> users = List.of(mock(User.class), mock(User.class));
            Channel privateChannel = mock(Channel.class);
            ChannelResponseDto responseDto = mock(ChannelResponseDto.class);

            given(privateChannel.getType()).willReturn(ChannelType.PRIVATE);
            given(userReader.findUsersByIds(command.memberIds())).willReturn(users);
            given(channelFactory.create(command)).willReturn(privateChannel);
            given(channelRepository.save(privateChannel)).willReturn(privateChannel);
            given(channelMapper.toDto(privateChannel)).willReturn(responseDto);

            // when
            ChannelResponseDto result = channelService.createChannel(command);

            // then
            assertSame(responseDto, result);

            InOrder inOrder = inOrder(channelFactory, channelRepository, userReader, readStatusRepository, channelMapper);
            inOrder.verify(channelFactory).create(command);
            inOrder.verify(channelRepository).save(privateChannel);
            inOrder.verify(userReader).findUsersByIds(command.memberIds());
            inOrder.verify(readStatusRepository).saveAll(anyList());
            inOrder.verify(channelMapper).toDto(privateChannel);

            // 추가 호출 금지, 요구한 협력자 호출 외엔 전부 실패
            then(userReader).shouldHaveNoMoreInteractions();
            then(channelFactory).shouldHaveNoMoreInteractions();
            then(channelRepository).shouldHaveNoMoreInteractions();
            then(channelMapper).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("[Behavior + Branch][Negative] PRIVATE 채널 생성 - 멤버가 없으면 ChannelMinimumMembersNotMetException 예외 + readStatus, channel mapper 미호출")
        void createPrivateChannel_shouldThrow_whenMemberIdsMissing() {

            // given
            ChannelCreateRequestDto dto = new ChannelCreateRequestDto("비공개 채널", "설명", List.of());
            ChannelCreateCommand command = ChannelCreateCommand.from(dto, ChannelType.PRIVATE);
            Channel privateChannel = mock(Channel.class);

            given(channelFactory.create(command)).willReturn(privateChannel);
            given(channelRepository.save(privateChannel)).willReturn(privateChannel);
            given(privateChannel.getType()).willReturn(ChannelType.PRIVATE);
            given(userReader.findUsersByIds(command.memberIds())).willReturn(List.of());

            // when + then
            assertThrows(ChannelMinimumMembersNotMetException.class, () -> channelService.createChannel(command));

            then(channelFactory).should().create(command);
            then(channelRepository).should().save(privateChannel);
            then(userReader).should().findUsersByIds(command.memberIds());

            then(channelMapper).shouldHaveNoInteractions();
            then(readStatusRepository).shouldHaveNoInteractions();
        }

        @DisplayName("[Negative] 채널 참가 인원에 잘못된 인원이있어 요청 인원보다 작을시 ChannelInvalidParticipantsException 예외발생")
        @Test
        void createPrivateChannel_shouldThrow_whenUserIdsLessThenRequest() {
            // given
            List<UUID> memberIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
            ChannelCreateRequestDto dto = new ChannelCreateRequestDto(null, null, memberIds);
            ChannelCreateCommand command = ChannelCreateCommand.from(dto, ChannelType.PRIVATE);

            List<User> users = List.of(mock(User.class), mock(User.class));
            Channel privateChannel = mock(Channel.class);

            given(channelFactory.create(command)).willReturn(privateChannel);
            given(channelRepository.save(privateChannel)).willReturn(privateChannel);
            given(privateChannel.getType()).willReturn(ChannelType.PRIVATE);
            given(userReader.findUsersByIds(command.memberIds())).willReturn(users);

            //when + then
            assertThrows(ChannelInvalidParticipantsException.class, () -> channelService.createChannel(command));

            then(channelFactory).should().create(command);
            then(channelRepository).should().save(privateChannel);
            then(userReader).should().findUsersByIds(command.memberIds());

            then(readStatusRepository).shouldHaveNoInteractions();
            then(channelMapper).shouldHaveNoInteractions();

        }

    }

    @Nested
    @DisplayName("채널 수정 - public, private")
    class updateChannel {
        @Test
        @DisplayName("[Behavior][Positive] 채널 수정 - PUBLIC 채널 update, channelRepository.save() 정상 위임")
        void updateChannel_shouldUpdateAndSave_whenPublicChannel() {

            // given
            UUID channelId = UUID.randomUUID();
            ChannelUpdateRequestDto request = new ChannelUpdateRequestDto("newTitme", "newDesc");

            Channel channel = mock(Channel.class);
            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
            given(channel.getType()).willReturn(ChannelType.PUBLIC);

            // when
            channelService.updateChannel(channelId, request);

            // then
            then(channel).should().update(any(ChannelUpdateParams.class));

            then(channelRepository).should().save(channel);
        }

        @Test
        @DisplayName("[Behavior + Branch][Negative] 채널 수정 - PRIVATE 채널이면 수정 불가 ChannelModificationNotAllowedException 예외발생 update 미호출, save 미위임")
        void updateChannel_shouldThrow_whenPrivateChannel() {
            // given
            UUID channelId = UUID.randomUUID();
            ChannelUpdateRequestDto request = new ChannelUpdateRequestDto("newTitme", "newDesc");
            Channel channel = mock(Channel.class);
            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
            given(channel.getType()).willReturn(ChannelType.PRIVATE);

            // when & then
            assertThrows(ChannelModificationNotAllowedException.class, () -> channelService.updateChannel(channelId, request));
            then(channel).should(never()).update(any(ChannelUpdateParams.class));
            then(channelRepository).should(never()).save(any(Channel.class));

        }

        @Test
        @DisplayName("[Branch + Exception][Negative] 채널 수정 - channelId이 null 이면 INVALID_INPUT 예외 전파")
        void updateChannel_shouldThrow_whenChannelIdIsNull() {
            // given
            UUID channelId = null;
            ChannelUpdateRequestDto request = new ChannelUpdateRequestDto("newTitme", "newDesc");

            // when & then
            DiscodeitException ex = assertThrows(DiscodeitException.class, () -> channelService.updateChannel(channelId, request));

            assertEquals(ErrorCode.INVALID_INPUT, ex.getErrorCode());

            then(channelRepository).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("채널 삭제")
    class deleteChannel{
        @Test
        @DisplayName("[Behavior][Positive] 채널 삭제 - message, readStats, channel 삭제 위임")
        void deleteChannel_shouldDeleteChannelAndReadStatusAndMessage(){
            // given
            UUID channelId = UUID.randomUUID();
            Channel channel = mock(Channel.class);
            given(channelReader.findChannelOrThrow(channelId)).willReturn(channel);
            given(channel.getId()).willReturn(channelId);

            // when
            channelService.deleteChannel(channelId);

            // then
            then(messageRepository).should().deleteByChannelId(channel.getId());
            then(readStatusRepository).should().deleteByChannelId(channel.getId());
            then(channelRepository).should().deleteById(channel.getId());
        }

        @Test
        @DisplayName("[Branch][Negative] 채널 삭제 - channelId가 null일시 INVALID_INPUT 전파")
        void deleteChannel_shouldThrow_whenChannelIdIsNull(){

            // when & then
            DiscodeitException ex = assertThrows(DiscodeitException.class, () -> channelService.deleteChannel(null));
            assertEquals(ErrorCode.INVALID_INPUT, ex.getErrorCode());
        }
    }

}
