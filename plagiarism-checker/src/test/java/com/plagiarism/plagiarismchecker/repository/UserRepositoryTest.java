
package com.plagiarism.plagiarismchecker.repository;

import com.plagiarism.plagiarismchecker.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {

        User user = new User();
        user.setName("Sneha");
        user.setEmail("sneha@test.com");
        user.setPassword("1234");
        user.setRole("STUDENT");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Sneha", savedUser.getName());
    }
}