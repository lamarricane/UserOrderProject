package com.example.service;

import com.example.UserOrderApplication;
import com.example.controller.UserController;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {UserOrderApplication.class, UserService.class})
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() {
        User user1 = new User(UUID.randomUUID(), "John Doe", "john@example.com");
        User user2 = new User(UUID.randomUUID(), "Jane Doe", "jane@example.com");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "John Doe", "john@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void createUser() {
        User user = new User(UUID.randomUUID(), "John Doe", "john@example.com");

        userService.createUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser() {
        UUID id = UUID.randomUUID();
        User existingUser = new User(id, "John Doe", "john@example.com");
        User updatedUser = new User(id, "John Smith", "john.smith@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        userService.updateUser(id, updatedUser);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(existingUser);
        assertEquals("John Smith", existingUser.getName());
        assertEquals("john.smith@example.com", existingUser.getEmail());
    }

    @Test
    void updateUser_NotFound() {
        UUID id = UUID.randomUUID();
        User updatedUser = new User(id, "John Smith", "john.smith@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(id, updatedUser));
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "John Doe", "john@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.deleteUser(id);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteUser_NotFound() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(id));
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, never()).deleteById(any());
    }
}