package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.converters.ChatConverter;
import org.demo.chatweb.converters.SectionDTOConverter;
import org.demo.chatweb.dto.AvatarInstDTO;
import org.demo.chatweb.dto.SectionDTO;
import org.demo.chatweb.dto.UpdateSectionDTO;
import org.demo.chatweb.dto.UserMinDTO;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Section;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.ChatsService;
import org.demo.chatweb.services.SectionsService;
import org.demo.chatweb.services.StorageService;
import org.demo.chatweb.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SectionControllerTests {

    @Mock
    private SectionsService sectionsService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UsersService usersService;

    @Mock
    private ChatsService chatsService;

    @Mock
    private SectionDTOConverter sectionDTOConverter;

    @Mock
    private ChatConverter chatConverter;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SectionController sectionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUserSections() throws ServletException {
        String authHeader = "Bearer mockToken";
        User mockUser = new User();
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn("mockUsername");
        when(usersService.getByUsername("mockUsername")).thenReturn(mockUser);
        List<Section> mockSections = new ArrayList<>();
        when(chatsService.findUserSections(mockUser)).thenReturn(mockSections);
        when(sectionDTOConverter.convertToDTOList(mockSections, mockUser)).thenReturn(new ArrayList<>());

        ResponseEntity<List<SectionDTO>> result = sectionController.getUserSections(authHeader);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(jwtUtil, times(1)).filterAndRetrieveClaimSpecificToken(authHeader);
        verify(usersService, times(1)).getByUsername("mockUsername");
        verify(chatsService, times(1)).findUserSections(mockUser);
        verify(sectionDTOConverter, times(1)).convertToDTOList(mockSections, mockUser);
    }

    @Test
    void testGetUsersWithoutSection() throws ServletException {
        String authHeader = "Bearer mockToken";
        User mockUser = new User();
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn("mockUsername");
        when(usersService.getByUsername("mockUsername")).thenReturn(mockUser);
        List<Chat> mockChats = new ArrayList<>();
        when(chatsService.findUserChatsWithNoSection(mockUser)).thenReturn(mockChats);
        List<UserMinDTO> mockUsers = new ArrayList<>();
        when(chatConverter.convertToMinDTOList(mockChats)).thenReturn(mockUsers);

        ResponseEntity<List<UserMinDTO>> result = sectionController.getUsersWithoutSection(authHeader);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(jwtUtil, times(1)).filterAndRetrieveClaimSpecificToken(authHeader);
        verify(usersService, times(1)).getByUsername("mockUsername");
        verify(chatsService, times(1)).findUserChatsWithNoSection(mockUser);
        verify(chatConverter, times(1)).convertToMinDTOList(mockChats);
    }

    @Test
    void testGetUsersWithoutSectionAvatars() throws ServletException, IOException {
        String authHeader = "Bearer mockToken";
        User mockUser = new User();
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn("mockUsername");
        when(usersService.getByUsername("mockUsername")).thenReturn(mockUser);
        List<Chat> mockChats = new ArrayList<>();
        when(chatsService.findUserChatsWithNoSection(mockUser)).thenReturn(mockChats);
        List<User> mockUsers = new ArrayList<>();
        when(chatConverter.convertToChatListUser(mockUser, mockChats)).thenReturn(mockUsers);
        when(storageService.downloadFile(any())).thenReturn(new byte[0]);

        ResponseEntity<List<AvatarInstDTO>> result = sectionController.getUsersWithoutSectionAvatars(authHeader);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(jwtUtil, times(1)).filterAndRetrieveClaimSpecificToken(authHeader);
        verify(usersService, times(1)).getByUsername("mockUsername");
        verify(chatsService, times(1)).findUserChatsWithNoSection(mockUser);
        verify(chatConverter, times(1)).convertToChatListUser(mockUser, mockChats);
        verify(storageService, times(mockUsers.size())).downloadFile(any());
    }
    @Test
    void testCreateSection() throws ServletException {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setTitle("Test Section");
        String authHeader = "Bearer mockToken";
        User mockUser = new User();
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn("mockUsername");
        when(usersService.getByUsername("mockUsername")).thenReturn(mockUser);
        when(usersService.getByUsernames(any())).thenReturn(new ArrayList<>());
        when(chatsService.findExistingChat(any(), any())).thenReturn(Collections.singletonList(new Chat()));

        ResponseEntity<HttpStatus> responseEntity = sectionController.createSection(sectionDTO, authHeader);

        verify(sectionsService, times(1)).save((Section) any());
        verify(chatsService, times(1)).setSection(any(), any());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateSection() throws ServletException {
        UpdateSectionDTO updateSectionDTO = new UpdateSectionDTO();
        updateSectionDTO.setPrevTitle("Old Title");
        updateSectionDTO.setNewTitle("New Title");
        updateSectionDTO.setUsersToAdd(Collections.singletonList("user1"));
        updateSectionDTO.setUsersToDelete(Collections.singletonList("user2"));
        String authHeader = "Bearer mockToken";
        User mockUser = new User();
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn("mockUsername");
        when(usersService.getByUsername("mockUsername")).thenReturn(mockUser);
        when(sectionsService.getByTitle("Old Title")).thenReturn(new Section());
        when(usersService.getByUsernames(any())).thenReturn(new ArrayList<>());
        when(chatsService.findExistingChat(any(), any())).thenReturn(Collections.singletonList(new Chat()));

        ResponseEntity<HttpStatus> responseEntity = sectionController.updateSection(updateSectionDTO, authHeader);

        verify(sectionsService, times(1)).getByTitle(any());
        verify(chatsService, times(1)).setSection(any(), any());
        verify(chatsService, times(1)).removeSection(any());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
