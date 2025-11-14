package com.bytser.campso.api.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);
}
