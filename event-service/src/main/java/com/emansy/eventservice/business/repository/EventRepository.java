package com.emansy.eventservice.business.repository;

import com.emansy.eventservice.business.repository.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<EventEntity> findByIdInAndDateBetween(Set<Long> eventIds, LocalDate startDate, LocalDate endDate);

    List<EventEntity> findByIdInAndDateGreaterThanEqual(Set<Long> eventIds, LocalDate startDate);

}
