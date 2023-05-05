package com.emansy.eventservice.business.service;

import com.emansy.eventservice.business.handler.NoContentException;
import com.emansy.eventservice.business.handler.ResourceNotFoundException;
import com.emansy.eventservice.business.mapper.EventMapper;
import com.emansy.eventservice.business.repository.EventRepository;
import com.emansy.eventservice.business.repository.model.EventEntity;
import com.emansy.eventservice.business.service.EventService;
import com.emansy.eventservice.model.EventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    @Override
    public EventDto create(EventDto eventDto) {
        log.info("Service Layer: Event details are :{}.", eventDto);
        return eventMapper.entityToDto(eventRepository.save(eventMapper.dtoToEntity(eventDto)));
    }

    @Override
    public List<EventDto> getAllEventBetween(LocalDate startDate, LocalDate endDate) {
        List<EventEntity> list = eventRepository.findByDateBetween(startDate, endDate);
        if(list.isEmpty()) {
            throw new NoContentException("No Content Found between Date Range: " + startDate + " and " + endDate);

        }
        return list.stream().map(eventMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public EventDto getByID(Long id) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Id: " + id + " Not Found"));
        EventDto eventDto = eventMapper.entityToDto(eventEntity);
        log.info("Service layer -> Event with id {} is {}", id, eventDto);
        return eventDto;
    }

    @Override
    public Boolean existsInDb(Long id) {
        return eventRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        if (!eventRepository.existsById(id)) {
            log.warn("Event with id {} does not exist");
            throw new ResourceNotFoundException("Id Number: " + id + " does not exist. Cannot delete the event");
        }
        eventRepository.deleteById(id);
        log.info("Event Deleted id : {}", id);
    }

    @Override
    public Set<EventDto> getEventsByIdsAndDate(Set<Long> eventIds, String fromDate, String thruDate) {
        LocalDate startDate = getDate(fromDate);

        if (Objects.nonNull(thruDate)) {
            LocalDate endDate = LocalDate.parse(thruDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return eventRepository.findByIdInAndDateBetween(eventIds, startDate, endDate).stream()
                    .map(eventMapper::entityToDto)
                    .collect(Collectors.toSet());
        } else {
            return eventRepository.findByIdInAndDateGreaterThanEqual(eventIds, startDate).stream()
                    .map(eventMapper::entityToDto)
                    .collect(Collectors.toSet());
        }

    }

    private LocalDate getDate(String date) {
        if (Objects.nonNull(date)) {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }
}
