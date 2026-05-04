package cz.osu.swidemo;

import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.UserRepository;
import cz.osu.swidemo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterNewUser_passwordIsHashed() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("plainPassword");

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User saved = userService.registerNewUser(user);

        assertNotEquals("plainPassword", saved.getPassword());
        assertTrue(saved.getPassword().startsWith("$2a$"));
    }

    @Test
    void testAuthenticate_wrongPassword_returnsEmpty() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("$2a$10$abcdefghijklmnopqrstuuVGOiA736MXPyN7EUGMmVoJpnNnPRpEi");

        when(userRepository.findFirstByUsername("testuser")).thenReturn(Optional.of(user));
        Optional<User> result = userService.authenticate("testuser", "wrongpassword");

        assertTrue(result.isEmpty());
    }

    @Test
    void testAuthenticate_userNotFound_returnsEmpty() {
        when(userRepository.findFirstByUsername("neexistuje")).thenReturn(Optional.empty());

        Optional<User> result = userService.authenticate("neexistuje", "heslo");

        assertTrue(result.isEmpty());
    }
}