package com.emansy.eventservice.web.controller;


import com.emansy.eventservice.business.handler.NoContentException;
import com.emansy.eventservice.business.handler.ResourceNotFoundException;
import com.emansy.eventservice.business.mapper.EventMapper;
import com.emansy.eventservice.business.repository.model.EventEntity;
import com.emansy.eventservice.business.service.EventService;
import com.emansy.eventservice.model.EventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {
    private static final String END_POINT_PATH = "/api/v1/events";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private EventMapper eventMapper;

    @Test
    public void saveEventReturn400BadRequest() throws Exception {
        String requestUri = END_POINT_PATH + "/save";
        EventDto eventDto = EventDto.builder()
                .id(1L)
                .title("")
                .details("")
                .date(LocalDate.of(2000, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();

        String requestBody = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post(requestUri).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        verifyNoInteractions(eventService);
    }


    @Test
    public void saveEventReturn201CreatedRequest() throws Exception {
        String title = "scrum";
        String requestUri = END_POINT_PATH + "/save";
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
        when(eventService.create(eventDto)).thenReturn(eventDto);
        when(eventMapper.dtoToEntity(eventDto)).thenReturn(eventEntity);
        mockMvc.perform(post(requestUri).contentType("application/json")
                        .content(requestBody))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andDo(print());
        verify(eventService).create(eventDto);
    }

    @Test
    void getEventById200StatusTest() throws Exception {
        Long id = 1L;
        String requestUri = END_POINT_PATH + "/" + id;
        EventDto eventDto = EventDto.builder()
                .id(1L)
                .title("scrum")
                .details("Discuss everyday update")
                .date(LocalDate.of(2024, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();

        when(eventService.getByID(id)).thenReturn(eventDto);
        mockMvc.perform(get(requestUri).contentType("application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("scrum"))
                .andDo(print());
        verify(eventService).getByID(id);
    }


    @Test
    void getEventByIdExceptionThrown404Test() throws Exception {
        Long id = 111L;
        String requestUri = END_POINT_PATH + "/" + id;
        when(eventService.getByID(id)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get(requestUri).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(eventService).getByID(id);
    }


    @Test
    void deleteEventByIdExceptionThrown404Test() throws Exception {
        Long id = 111L;
        String requestUri = END_POINT_PATH + "/" + id;
        doThrow(ResourceNotFoundException.class).when(eventService).deleteById(id);

        mockMvc.perform(delete(requestUri).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(eventService).deleteById(id);
    }

    @Test
    void deleteEventById200StatusTest() throws Exception {
        Long id = 1L;
        String requestUri = END_POINT_PATH + "/" + id;
        doNothing().when(eventService).deleteById(id);

        mockMvc.perform(delete(requestUri).contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());
        verify(eventService).deleteById(id);
    }

    @Test
    void updateEvent200StatusTest() throws Exception {
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
        when(eventService.existsInDb(eventDto.getId())).thenReturn(true);
        when(eventService.create(eventDto)).thenReturn(eventDto);
        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("scrum"))
                .andDo(print());

        verify(eventService).create(eventDto);
        verify(eventService).existsInDb(eventDto.getId());

    }


    @Test
    void updateEventExceptionThrown404Test() throws Exception {
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
       when(eventService.existsInDb(eventDto.getId())).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(eventService).existsInDb(eventDto.getId());
        verifyNoMoreInteractions(eventService);

    }


    @Test
    public void updateEventReturn400BadRequest() throws Exception {
        EventDto eventDto = EventDto.builder()
                .id(1L)
                .title("")
                .details("")
                .date(LocalDate.of(2000, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();

        String requestBody = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());

                verifyNoInteractions(eventService);
    }


    @Test
    void findAllEventBetween200StatusTest() throws Exception {

        LocalDate targetDate1 = LocalDate.of(2000, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2024, 12, 12);
        String requestUri =  END_POINT_PATH + "/"  + targetDate1 + "/" + targetDate2;

        EventDto eventDto = EventDto.builder()
                .id(1L)
                .title("scrum")
                .details("Every Day update")
                .date(LocalDate.of(2000, 12, 12))
                .startTime(LocalTime.of(01, 10))
                .endTime(LocalTime.of(01, 50))
                .build();
        List<EventDto> eventDtoList=new ArrayList<>();
        eventDtoList.add(eventDto);


        when(eventService.getAllEventBetween(targetDate1,targetDate2)).thenReturn(eventDtoList);
        mockMvc.perform(get(requestUri).contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].details").value("Every Day update"))
                .andDo(print());

         verify(eventService).getAllEventBetween(targetDate1,targetDate2);

}

    @Test
    void findAllEventBetween204StatusTest() throws Exception {

        LocalDate targetDate1 = LocalDate.of(2000, 1, 1);
        LocalDate targetDate2 = LocalDate.of(2024, 12, 12);
        String requestUri =  END_POINT_PATH + "/"  + targetDate1 + "/" + targetDate2;

        doThrow(NoContentException.class).when(eventService).getAllEventBetween(targetDate1,targetDate2);
        mockMvc.perform(get(requestUri).contentType("application/json"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json"))
                .andDo(print());

        verify(eventService).getAllEventBetween(targetDate1,targetDate2);
    }

}
