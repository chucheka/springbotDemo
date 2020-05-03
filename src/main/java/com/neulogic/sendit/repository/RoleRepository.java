package com.neulogic.sendit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neulogic.sendit.models.ERole;
import com.neulogic.sendit.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);

}
