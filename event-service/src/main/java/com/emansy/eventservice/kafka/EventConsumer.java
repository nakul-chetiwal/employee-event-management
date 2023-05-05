package com.emansy.eventservice.kafka;


import com.emansy.eventservice.business.service.EventService;
import com.emansy.eventservice.model.EventDto;
import com.emansy.eventservice.model.EventIdsWithinDatesDto;
import com.emansy.eventservice.model.EventsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventConsumer {

    private final EventService eventService;

    @KafkaListener(topics = "events-request", groupId = "event-group")
    @SendTo
    public Message<EventsDto> handleEventRequestBetween(ConsumerRecord<String, EventIdsWithinDatesDto> consumerRecord) {
        EventIdsWithinDatesDto eventIdsWithinDatesDto = consumerRecord.value();
        log.info("Request for filtering Events between date range for the specific employee is received from Kafka topic events-request -> {}", eventIdsWithinDatesDto);
        Set<Long> eventDtoSet = eventIdsWithinDatesDto.getIds();
        String fromDate = eventIdsWithinDatesDto.getFromDate();
        String thruDate = eventIdsWithinDatesDto.getThruDate();


        if (thruDate.isEmpty()) {
            thruDate = null;
        }
        Set<EventDto> eventDto = eventService.getEventsByIdsAndDate(eventDtoSet, fromDate, thruDate);
        log.info("Response sent to Kafka topic events-response-> {}", eventDto);
        EventsDto eventsDto = new EventsDto(eventDto);
        return MessageBuilder.withPayload(eventsDto).build();
    }
}
