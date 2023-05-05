package com.emansy.eventservice.business.mapper;

import com.emansy.eventservice.business.repository.model.EventEntity;
import com.emansy.eventservice.model.EventDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto entityToDto(EventEntity eventEntity);

    EventEntity dtoToEntity(EventDto eventDto);
}
