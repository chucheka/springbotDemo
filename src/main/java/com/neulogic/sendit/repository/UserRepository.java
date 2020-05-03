package com.neulogic.sendit.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.neulogic.sendit.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

}
