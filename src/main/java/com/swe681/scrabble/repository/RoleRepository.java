package com.swe681.scrabble.repository;

import com.swe681.scrabble.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}