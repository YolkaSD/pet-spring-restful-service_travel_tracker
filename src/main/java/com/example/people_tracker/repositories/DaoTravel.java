package com.example.people_tracker.repositories;

import com.example.people_tracker.models.AggregateDTO;
import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;

import java.util.List;

public interface DaoTravel {
    void add(TravelDTO travelDTO);

    void addNewTravelFromList(List<TravelDTO> travelDTOList);

    List<ClientDTO> getClientList();

    void calculateAggregatesFromDB();

    void calculateAggregatesFromJava(AggregateDTO aggregateDTO);

    List<Long> getClientIdList();

    List<TravelDTO> getTravelDTOList(Long clientId);
}
