package com.starlinex.repository;

import com.starlinex.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempUserRepository extends JpaRepository<TempUser, Integer> {
}
