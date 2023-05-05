package com.emansy.eventservice.web.controller;


import com.emansy.eventservice.business.repository.EventRepository;
import com.emansy.eventservice.business.repository.model.EventEntity;
import com.emansy.eventservice.model.EventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventRepository eventRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String END_POINT_PATH = "/api/v1/events";


    @Test
    public void saveEventIT() throws Exception {

        String requestUri = END_POINT_PATH + "/save";
        EventDto eventDto = EventDto.builder()
                .id(5L)
                .title("scrum")
                .details("Discuss everyday update")
                .date(LocalDate.of(2024, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();
        EventEntity eventEntity = EventEntity.builder()
                .id(5L)
                .title("scrum")
                .details("Discuss everyday update")
                .date(LocalDate.of(2024, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();

        when(eventRepository.save(eventEntity)).thenReturn(eventEntity);
        String requestBody = objectMapper.writeValueAsString(eventDto);
        this.mockMvc.perform(post(requestUri).contentType("application/json")
                        .content(requestBody))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.title").value("scrum"))
                .andDo(print());
        verify(eventRepository).save(eventEntity);

    }

    @Test
    public void getEventById200IT() throws Exception {
        Long id = 5L;
        String requestUri = END_POINT_PATH + "/" + id;
        EventEntity eventEntity = EventEntity.builder()
                .id(5L)
                .title("scrum")
                .details("Discuss everyday update")
                .date(LocalDate.of(2024, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(eventEntity));
        this.mockMvc.perform(get(requestUri).contentType("application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.title").value("scrum"))
                .andDo(print());

    }

    @Test
    public void getEventById404IT() throws Exception {
        Long id = 5L;
        String requestUri = END_POINT_PATH + "/" + id;
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        this.mockMvc.perform(get(requestUri).contentType("application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(eventRepository).findById(id);
    }


    @Test
    void deleteEventByIdExceptionThrown404IT() throws Exception {
        Long id = 1L;
        String requestUri = END_POINT_PATH + "/" + id;
        when(eventRepository.existsById(id)).thenReturn(false);
        mockMvc.perform(delete(requestUri).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(eventRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void deleteEventById200StatusIT() throws Exception {
        Long id = 1L;
        String requestUri = END_POINT_PATH + "/" + id;
        when(eventRepository.existsById(id)).thenReturn(true);
        doNothing().when(eventRepository).deleteById(id);

        mockMvc.perform(delete(requestUri).contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());
        verify(eventRepository).deleteById(id);
        verify(eventRepository).existsById(id);
    }

    @Test
    void updateEvent200StatusIT() throws Exception {
        EventDto eventDto = EventDto.builder()
                .id(1L)
                .title("scrum")
                .details("Discuss everyday update")
                .date(LocalDate.of(2024, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();

        EventEntity eventEntity = EventEntity.builder()
                .id(1L)
                .title("scrum")
                .details("Discuss everyday update")
                .date(LocalDate.of(2024, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();

        String requestBody = objectMapper.writeValueAsString(eventDto);
        when(eventRepository.save(eventEntity)).thenReturn(eventEntity);
        when(eventRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("scrum"))
                .andDo(print());

        verify(eventRepository).save(eventEntity);
        verify(eventRepository).existsById(1L);
        verifyNoMoreInteractions(eventRepository);
    }


    @Test
    void findAllEventBetween200StatusIT() throws Exception {

        LocalDate targetDate1 = LocalDate.of(2000, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2024, 12, 12);
        String requestUri = END_POINT_PATH + "/" + targetDate1 + "/" + targetDate2;

        EventEntity eventEntity = EventEntity.builder()
                .id(1L)
                .title("scrum")
                .details("Every Day update")
                .date(LocalDate.of(2000, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();
        List<EventEntity> eventEntityList = new ArrayList<>();
        eventEntityList.add(eventEntity);


        when(eventRepository.findByDateBetween(targetDate1, targetDate2)).thenReturn(eventEntityList);
        mockMvc.perform(get(requestUri).contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].details").value("Every Day update"))
                .andDo(print());
        verify(eventRepository).findByDateBetween(targetDate1, targetDate2);
        verifyNoMoreInteractions(eventRepository);

    }

    @Test
    void findAllEventBetween204StatusIT() throws Exception {

        LocalDate targetDate1 = LocalDate.of(2000, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2024, 12, 12);
        String requestUri = END_POINT_PATH + "/" + targetDate1 + "/" + targetDate2;

        when(eventRepository.findByDateBetween(targetDate1, targetDate2)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(requestUri).contentType("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
        verify(eventRepository).findByDateBetween(targetDate1, targetDate2);
    }

}
