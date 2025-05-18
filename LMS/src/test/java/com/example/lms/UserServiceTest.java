package com.example.lms;

import com.example.lms.models.AuthentictationResponse;
import com.example.lms.models.Profile;
import com.example.lms.models.User;
import com.example.lms.repositories.UserRepository;
import com.example.lms.repositories.ProfileRepository;
import com.example.lms.services.UserService;
import com.example.lms.springsecurity.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
 class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    // -------------------- Register Tests --------------------

    @Test
     void testSuccessfulRegister() {
        User user = new User();
        user.setName("dina");
        user.setEmail("dina@gmail.com");
        user.setPassword("password");
        user.setRole("ADMIN");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("mockJwtToken");

        AuthentictationResponse response = userService.register(user);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
    }

    @Test
    void testRegister_duplicateEmail() {
        User user = new User();
        user.setName("dina");
        user.setEmail("dina@gmail.com");
        user.setPassword("password");
        user.setRole("ADMIN");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));
    }

    @Test
    void testRegister_EmptyEmail() {
        User user = new User();
        user.setName("dina");
        user.setEmail("");
        user.setPassword("password");
        user.setRole("ADMIN");

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));
    }

    // -------------------- Login Tests --------------------

    @Test
     void testLogin_SuccessfulLogin() {
        User user = new User();
        user.setEmail("dina@gmail.com");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("mockJwtToken");

        AuthentictationResponse response = userService.login(user);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
    }

    @Test
     void testLogin_FailedLogin_UserNotFound() {
        User user = new User();
        user.setEmail("nonexistent@gmail.com");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> userService.login(user));
    }

    @Test
     void testLogin_FailedLogin_IncorrectPassword() {
        User user = new User();
        user.setEmail("dina@gmail.com");
        user.setPassword("wrongpassword");

        User existingUser = new User();
        existingUser.setEmail("dina@gmail.com");
        existingUser.setPassword("correctpassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(user.getPassword(), existingUser.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.login(user));
    }

    // -------------------- Profile Tests --------------------

    @Test
     void testEditProfile_ProfileNotFound() {
        User user = new User();
        user.setId(1);
        Profile profile = new Profile(user);

        when(profileRepository.getProfileById(user.getId())).thenReturn(null);

        ResponseEntity<Object> response = userService.editProfile(profile);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Profile not found for user ID: 1", response.getBody());
    }

    @Test
     void testEditProfile_SuccessfulEdit() {


        //existing profile
        User user1 = new User();
        user1.setId(1);
        user1.setName("JohnDoe");
        user1.setEmail("john.doe@example.com");

        Profile profile1 = new Profile();
        profile1.setId(1);
        profile1.setUser(user1);
        profile1.setAddress("123 Main St");
        profile1.setPhoneNumber("1234567890");

        // New profile  updated
        Profile updatedProfile = new Profile();
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName("JohnUpdated");
        updatedUser.setEmail("john.updated@example.com");
        updatedProfile.setUser(updatedUser);
        updatedProfile.setAddress("456 Updated Ave");
        updatedProfile.setPhoneNumber("9876543210");

        when(profileRepository.getProfileById(1)).thenReturn(profile1); // Existing profile
        when(userRepository.findByNameAndEmail("JohnUpdated", "john.updated@example.com"))
                .thenReturn(Optional.empty()); // No conflicting user

        ResponseEntity<Object> response = userService.editProfile(updatedProfile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Profile savedProfile = (Profile) response.getBody();
        assertNotNull(savedProfile);
        assertEquals("JohnUpdated", savedProfile.getUser().getName());
        assertEquals("john.updated@example.com", savedProfile.getUser().getEmail());
        assertEquals("456 Updated Ave", savedProfile.getAddress());
        assertEquals("9876543210", savedProfile.getPhoneNumber());

    }

    @Test
    void testEditProfile_DuplicateNameAndEmail() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("JohnDoe");
        user1.setEmail("john.doe@example.com");

        Profile profile1 = new Profile();
        profile1.setId(1);
        profile1.setUser(user1);


        User user2 = new User();
        user2.setId(2);
        user2.setName("JaneDoe");
        user2.setEmail("jane.doe@example.com");

        Profile updatedProfile = new Profile();
        User updatedUser = new User();
        updatedUser.setId(1); // Same as User1
        updatedUser.setName("JaneDoe");
        updatedUser.setEmail("jane.doe@example.com"); // Conflicting email
        updatedProfile.setUser(updatedUser);

        when(profileRepository.getProfileById(1)).thenReturn(profile1); // Existing profile
        when(userRepository.findByNameAndEmail("JaneDoe", "jane.doe@example.com"))
                .thenReturn(Optional.of(user2)); // Conflicting user


        ResponseEntity<Object> response = userService.editProfile(updatedProfile);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "The name 'JaneDoe' and email 'jane.doe@example.com' are already in use by another user. Please choose a different name or email.",
                response.getBody()
        );

    }

}