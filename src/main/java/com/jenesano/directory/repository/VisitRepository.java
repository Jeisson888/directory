package com.jenesano.directory.repository;

import com.jenesano.directory.entity.Page;
import com.jenesano.directory.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    int countByPage(Page page);

    @Query("SELECT COUNT(v) FROM Visit v WHERE v.page = :page AND v.timestamp >= :startDate")
    int countByPageSince(@Param("page") Page page, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT v.typeBusinessId, COUNT(v) AS count FROM Visit v WHERE v.page = :page AND v.typeBusinessId IS NOT NULL " +
            "GROUP BY v.typeBusinessId ORDER BY count DESC")
    List<Object[]> findMostPopularTypeBusiness(@Param("page") Page page);
}
