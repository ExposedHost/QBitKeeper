package com.xposed.qbitkeeper.repo;

import com.xposed.qbitkeeper.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {
    public List<Password> findByUserId(Long userId);
}
