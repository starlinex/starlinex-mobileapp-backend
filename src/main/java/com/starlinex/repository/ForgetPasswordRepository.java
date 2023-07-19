package com.starlinex.repository;

import com.starlinex.entity.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword,Integer> {
    Optional<ForgetPassword> findByUserId(Integer userId);
    void deleteByUserId(Integer userId);
}
