package com.example.controller;

import com.example.model.User;
import com.example.server.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllUsers() throws Exception {
        User user1 = new User(UUID.randomUUID(), "John Doe", "john@example.com");
        User user2 = new User(UUID.randomUUID(), "Jane Doe", "jane@example.com");
        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User(id, "John Doe", "john@example.com");

        when(userService.getUserById(id)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).getUserById(id);
    }

    @Test
    void getUserById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(userService.getUserById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(id);
    }

    @Test
    void createUser() throws Exception {
        User user = new User(UUID.randomUUID(), "John Doe", "john@example.com");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User has successfully created!"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void updateUser() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User(id, "John Doe", "john@example.com");

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User has successfully updated!"));

        verify(userService, times(1)).updateUser(eq(id), any(User.class));
    }

    @Test
    void deleteUser() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("User has successfully deleted!"));

        verify(userService, times(1)).deleteUser(id);
    }
}