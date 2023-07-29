package com.starlinex.repository;

import com.starlinex.entity.CountryData;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryDataRepository extends JpaRepository<CountryData,Integer> {
}
