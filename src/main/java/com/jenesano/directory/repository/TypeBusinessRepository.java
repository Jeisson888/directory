package com.jenesano.directory.repository;

import com.jenesano.directory.entity.TypeBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeBusinessRepository extends JpaRepository<TypeBusiness, Long> {
}
