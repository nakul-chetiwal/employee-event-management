package com.emansy.eventservice.business.service;


import com.emansy.eventservice.business.repository.model.EventEntity;
import com.emansy.eventservice.model.EventDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventService {

    public EventDto create(EventDto eventDto);

    public List<EventDto> getAllEventBetween(LocalDate startDate, LocalDate endDate);

    public EventDto getByID(Long Id);

    public Boolean existsInDb(Long id);

    public void deleteById(Long id);

    public Set<EventDto> getEventsByIdsAndDate(Set<Long> eventIds, String fromDate, String thruDate);


}
