package com.a2823kevin.pdfreader.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a2823kevin.pdfreader.backend.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Role findByName(String name);
}