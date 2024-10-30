package com.springboot.blog.repository;

import com.springboot.blog.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @DisplayName("test to save a User")
    @Test
    public void givenUserObject_whenSave_thenReturnSavedObject() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@gmail.com");

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0L);
    }

    @DisplayName("test to find User by username")
    @Test
    public void givenUserObject_whenFindByUsername_thenReturnUserObject() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@gmail.com");

        userRepository.save(user);

        assertThat(userRepository.findByUsername("test").get()).isNotNull();
    }

    @DisplayName("test to find User by username or email")
    @Test
    public void givenUserObject_whenFindByUsernameOrEmail_thenReturnUserObject() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@gmail.com");

        userRepository.save(user);

        assertThat(userRepository.findByUsernameOrEmail("test","test@gmail.com").get()).isNotNull();
    }

    @DisplayName("test to check existence of a User by username")
    @Test
    public void givenUserObject_whenExistsByUsername_thenReturnUserObject() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@gmail.com");

        userRepository.save(user);

        assertThat(userRepository.existsByUsername("test")).isEqualTo(true);
    }

    @DisplayName("test to check existence of a User by email")
    @Test
    public void givenUserObject_whenExistsByEmail_thenReturnUserObject() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@gmail.com");

        userRepository.save(user);

        assertThat(userRepository.existsByEmail("test@gmail.com")).isEqualTo(true);
    }
}
