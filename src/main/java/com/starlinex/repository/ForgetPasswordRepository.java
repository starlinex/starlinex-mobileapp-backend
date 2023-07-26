package com.starlinex.repository;

import com.starlinex.entity.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword,Integer> {
    Optional<ForgetPassword> findByUserId(Integer userId);
    @Modifying
    @Query("delete from ForgetPassword where userId = :userId")
    void removeByUserId(@Param("userId") Integer userId);
}
