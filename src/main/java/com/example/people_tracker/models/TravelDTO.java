package com.example.people_tracker.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TravelDTO {
    private Long id;
    @JsonProperty("client_id")
    private Long clientId;
    private String name;
    private String surname;
    private LocalDate birthday;
    @JsonProperty("transport_type")
    //@Enumerated(EnumType.STRING)
    private TransportType transportType;
    @JsonProperty("departure_city_name")
    private String departureCityName;
    @JsonProperty("destination_city_name")
    private String destinationCityName;
    @JsonProperty("departure_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureTime;
    @JsonProperty("destination_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime destinationTime;
}
