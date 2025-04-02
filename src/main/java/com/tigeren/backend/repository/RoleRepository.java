package com.tigeren.backend.repository;

import com.tigeren.backend.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    @Query("SELECT r FROM Role r WHERE r.name LIKE %:keyword%")
    List<Role> findByKeyword(String keyword, Pageable pageable);
}
