package com.example.people_tracker.services;

import com.example.people_tracker.models.AggregateDTO;
import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;

import java.util.List;

public interface TravelService {
    void add(TravelDTO travelDTO);

    void addNewTravelFromList(List<TravelDTO> travelDTOList);

    List<ClientDTO> getClientList();

    void calculateAggregatesFromDB();

    void calculateAggregatesFromJava();

    void createPeople();

    AggregateDTO getAggregateByClientId(Long clientId);
}
