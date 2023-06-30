package com.starlinex.repository;

import com.starlinex.entity.AirWayBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirWayBillRepository extends JpaRepository<AirWayBill, Integer> {
    List<AirWayBill> findAllByUserId(Integer id);
}
