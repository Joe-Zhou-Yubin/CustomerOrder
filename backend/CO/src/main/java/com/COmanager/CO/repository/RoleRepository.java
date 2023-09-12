package com.COmanager.CO.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.COmanager.CO.models.ERole;
import com.COmanager.CO.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}