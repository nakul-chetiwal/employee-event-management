package com.emansy.eventservice.business.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "details")
    private String details;

    @Column(name = "event_date")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    @Column(name = "start_time")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    @Column(name = "end_time")
    private LocalTime endTime;

}
