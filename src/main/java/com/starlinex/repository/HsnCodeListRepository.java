package com.starlinex.repository;

import com.starlinex.entity.HsnCodeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HsnCodeListRepository extends JpaRepository<HsnCodeList, Integer> {

    @Modifying
    @Query("SELECT h FROM HsnCodeList h WHERE UPPER(h.itemDesc) LIKE UPPER(CONCAT(:key, '%'))")
    List<HsnCodeList> searchByKeyword(@Param("key") String key);
}
