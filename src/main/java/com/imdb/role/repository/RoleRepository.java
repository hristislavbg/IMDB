package com.imdb.role.repository;

import com.imdb.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(String name);
}
