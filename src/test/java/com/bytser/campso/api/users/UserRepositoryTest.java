package com.bytser.campso.api.users;

import static org.assertj.core.api.Assertions.assertThat;

import com.bytser.campso.api.support.TestDataFactory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByEmail should return the user with the requested email")
    void shouldFindUserByEmail() {
        User storedUser = persistUser("100");

        Optional<User> result = userRepository.findByEmail("user100@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(storedUser.getId());
        assertThat(result.get().getEmail()).isEqualTo("user100@example.com");
    }

    @Test
    @DisplayName("findByEmail should return an empty Optional when the email is unknown")
    void shouldReturnEmptyOptionalWhenEmailUnknown() {
        persistUser("200");

        Optional<User> result = userRepository.findByEmail("missing@example.com");

        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("findByUserName should resolve the correct user")
    void shouldFindUserByUserName() {
        User storedUser = persistUser("300");

        Optional<User> result = userRepository.findByUserName("user300");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(storedUser.getId());
        assertThat(result.get().getUserName()).isEqualTo("user300");
    }

    @Test
    @DisplayName("findByFirstNameAndLastName should return all matching users")
    void shouldFindUsersByFirstAndLastName() {
        User firstMatch = persistUser("400");
        firstMatch.setFirstName("Alex");
        firstMatch.setLastName("Walker");
        entityManager.persistAndFlush(firstMatch);

        User secondMatch = persistUser("401");
        secondMatch.setFirstName("Alex");
        secondMatch.setLastName("Walker");
        entityManager.persistAndFlush(secondMatch);

        persistUser("402"); // Different names - should be ignored

        entityManager.clear();

        List<User> result = userRepository.findByFirstNameAndLastName("Alex", "Walker");

        assertThat(result)
            .extracting(User::getId)
            .containsExactlyInAnyOrder(firstMatch.getId(), secondMatch.getId());
    }

    private User persistUser(String identifier) {
        User user = TestDataFactory.createUser(identifier);
        return entityManager.persistAndFlush(user);
    }
}
