package com.emansy.eventservice.business.repository;

import com.emansy.eventservice.business.repository.model.EventEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void findByDateBetweenTest() {
        LocalDate targetDate1 = LocalDate.of(2023, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2023, 12, 12);
        List<EventEntity> eventEntities = eventRepository.findByDateBetween(targetDate1, targetDate2);
        assertThat(eventEntities).isNotNull().hasSize(2);
        assertThat(eventEntities.stream().filter(e -> e.getId().equals(1L)).map(e -> e.getDetails())).isEqualTo(Arrays.asList("discuss Microservice Update"));
      //  assertThat(eventRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    void findByDateBetween_isEmptyTest() {
        LocalDate targetDate1 = LocalDate.of(2020, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2020, 12, 12);
        List<EventEntity> eventEntities = eventRepository.findByDateBetween(targetDate1, targetDate2);
        assertThat(eventEntities).isEmpty();
    }

    @Test
    void findByIdInAndDateBetweenTest() {
        LocalDate targetDate1 = LocalDate.of(2024, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2024, 12, 12);
        Set<Long> eventIds= new HashSet<>();
        eventIds.add(1L);
        eventIds.add(2L);
        eventIds.add(3L);
        List<EventEntity> eventEntities = eventRepository.findByIdInAndDateBetween(eventIds,targetDate1, targetDate2);
        assertThat(eventEntities).isNotNull().hasSize(1);
        assertThat(eventEntities.stream().filter(e-> e.getId().equals(3L)).findFirst().get().getTitle()).isEqualTo("coffee meet");
    }

    @Test
    void findByIdInAndDateBetween_isEmptyTest() {
        LocalDate targetDate1 = LocalDate.of(2020, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2020, 12, 12);
        Set<Long> eventIds= new HashSet<>();
        eventIds.add(1L);
        eventIds.add(2L);
        eventIds.add(3L);
        List<EventEntity> eventEntities = eventRepository.findByIdInAndDateBetween(eventIds,targetDate1, targetDate2);
        assertThat(eventEntities).isEmpty();
    }

    @Test
    void findByIdInAndDateGreaterThanEqualTest(){
        LocalDate targetDate1 = LocalDate.of(2023, 1, 1);
        Set<Long> eventIds= new HashSet<>();
        eventIds.add(1L);
        eventIds.add(2L);
        eventIds.add(3L);
        eventIds.add(4L);
        List<EventEntity> eventEntities = eventRepository.findByIdInAndDateGreaterThanEqual(eventIds,targetDate1);
        assertThat(eventEntities).isNotNull().hasSize(4);
       assertThat(eventEntities.stream().filter(e-> e.getId().equals(4L)).findFirst().get().getTitle()).isEqualTo("devops talk");
    }

    @Test
    void findByIdInAndDateGreaterThanEqual_isEmptyTest(){
        LocalDate targetDate1 = LocalDate.of(2026, 1, 1);
        Set<Long> eventIds= new HashSet<>();
        eventIds.add(1L);
        eventIds.add(2L);
        eventIds.add(3L);
        eventIds.add(4L);
        List<EventEntity> eventEntities = eventRepository.findByIdInAndDateGreaterThanEqual(eventIds,targetDate1);
        assertThat(eventEntities).isEmpty();
    }
}

