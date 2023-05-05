package com.emansy.eventservice.business.service;

import com.emansy.eventservice.business.handler.NoContentException;
import com.emansy.eventservice.business.handler.ResourceNotFoundException;
import com.emansy.eventservice.business.mapper.EventMapper;
import com.emansy.eventservice.business.repository.EventRepository;
import com.emansy.eventservice.business.repository.model.EventEntity;
import com.emansy.eventservice.model.EventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMapper eventMapper;
    private Set<Long> eventIds;
    List<EventEntity> eventEntityList = new ArrayList<>();
    private Set<EventDto> eventDtoSet = new HashSet<>();

    private EventEntity eventEntity;
    private EventDto eventDto;

    @BeforeEach
    void inIt() {
        eventIds = new HashSet<>();
        eventIds.add(1L);
        eventEntity = EventEntity.builder()
                .id(1L)
                .title("scrum")
                .details("project update")
                .date(LocalDate.of(2000, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();

        eventDto = EventDto.builder()
                .id(1L)
                .title("scrum")
                .details("project update")
                .date(LocalDate.of(2000, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();
        eventEntityList.add(eventEntity);
        eventDtoSet.add(eventDto);
    }

    @Test
    void getEventsByIdsAndDateTest_WithEndDateParameter() {
        LocalDate targetDate1 = LocalDate.of(2000, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2021, 12, 12);
        when(eventRepository.findByIdInAndDateBetween(eventIds, targetDate1, targetDate2)).thenReturn(eventEntityList);
        when(eventMapper.entityToDto(eventEntity)).thenReturn(eventDto);
        assertThat(eventService.getEventsByIdsAndDate(eventIds, "2000-01-01", "2021-12-12")).hasSize(1);
        verify(eventRepository).findByIdInAndDateBetween(eq(eventIds), eq(targetDate1), eq(targetDate2));
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void getEventsByIdsAndDateTest_WithoutEndDateParameter() {
        LocalDate targetDate1 = LocalDate.of(2000, 1, 1);
        when(eventRepository.findByIdInAndDateGreaterThanEqual(eventIds, LocalDate.of(2000, 01, 01))).thenReturn(eventEntityList);
        when(eventMapper.entityToDto(eventEntity)).thenReturn(eventDto);
        assertThat(eventService.getEventsByIdsAndDate(eventIds, "2000-01-01", null)).hasSize(1);
        verify(eventRepository).findByIdInAndDateGreaterThanEqual(eq(eventIds), eq(targetDate1));
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void create() {
        when(eventMapper.entityToDto(eventEntity)).thenReturn(eventDto);
        when(eventMapper.dtoToEntity(eventDto)).thenReturn(eventEntity);
        when(eventRepository.save(eventEntity)).thenReturn(eventEntity);
        EventDto actual = eventService.create(eventDto);
        assertThat(actual).isNotNull().isEqualTo(eventDto);
        assertThat(actual.getId()).isEqualTo(1L);
        verify(eventRepository).save(eventEntity);
    }

    @Test
    void getAllEventBetweenTest() {

        LocalDate targetDate1 = LocalDate.of(2000, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2021, 12, 12);
        when(eventRepository.findByDateBetween(targetDate1, targetDate2)).thenReturn(eventEntityList);
        when(eventMapper.entityToDto(eventEntity)).thenReturn(eventDto);
        List<EventDto> allEventBetween = eventService.getAllEventBetween(targetDate1, targetDate2);
        assertThat(allEventBetween).isNotNull().hasSize(1);
        assertThat(allEventBetween.get(0).getTitle()).isEqualTo("scrum");
        verify(eventRepository).findByDateBetween(eq(targetDate1), eq(targetDate2));

    }

    @Test
    void getAllEventBetweenEmptyListTest() {
    List<EventEntity> eventEntityList1=new ArrayList<>();
        LocalDate targetDate1 = LocalDate.of(2000, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2021, 12, 12);
        when(eventRepository.findByDateBetween(targetDate1, targetDate2)).thenReturn(eventEntityList1);
        assertThrows(NoContentException.class,() ->eventService.getAllEventBetween(targetDate1, targetDate2));
        verify(eventRepository).findByDateBetween(eq(targetDate1), eq(targetDate2));

    }


    @Test
    void existsInDbTest() {
        when(eventRepository.existsById(1L)).thenReturn(true);
        assertThat(eventService.existsInDb(1L)).isTrue();
        verify(eventRepository).existsById(eq(1L));
    }


    @Test
    void deleteByIdTest() {
        doNothing().when(eventRepository).deleteById(anyLong());
        eventRepository.deleteById(1L);
        verify(eventRepository).deleteById(eq(1L));
    }

    @Test
    void deleteByIdExceptionThrownTest() {
      when(eventRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class,()->eventService.deleteById(anyLong()));
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void getByIDTest() {
        when(eventRepository.findById(1L)).thenReturn(Optional.ofNullable(eventEntity));
        when(eventMapper.entityToDto(eventEntity)).thenReturn(eventDto);
        assertThat(eventService.getByID(1L)).isNotNull().isEqualTo(eventDto);
        verify(eventRepository).findById(1L);
    }

    @Test
    void getByIDExceptionThrownTest() {
        doThrow(ResourceNotFoundException.class).when(eventRepository).findById(1L);
        assertThrows(ResourceNotFoundException.class,() ->eventService.getByID(1L));
        verify(eventRepository).findById(1L);
        verifyNoInteractions(eventMapper);
    }
}
