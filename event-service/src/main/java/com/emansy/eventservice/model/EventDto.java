package com.emansy.eventservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class EventDto {
    //@Positive(message = "Positive integer is required")
    @ApiModelProperty(value = "Unique id of an event")
    private Long id;

    @ApiModelProperty(value = "Title of an event")
    @NotEmpty(message = "required")
    private String title;

    @ApiModelProperty(value = "Details of an event")
    @NotEmpty(message = "required")
    private String details;

    @ApiModelProperty(value = "Date of an event")
    @FutureOrPresent(message = "Only Present or future date is allowed")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    @ApiModelProperty(value = "Start time of an event")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime startTime;

    @ApiModelProperty(value = "End time of an event")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime endTime;

}
