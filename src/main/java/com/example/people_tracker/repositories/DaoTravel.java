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

    void calculateAggregates(AggregateDTO aggregateDTO);
    void calculateAggregates(List<AggregateDTO> aggregateDTOList);

    List<Long> getClientIdList();

    List<TravelDTO> getTravelDTOListById(Long clientId);

    List<TravelDTO> getAllTravelDTOList();

    List<TravelDTO> getTravelsByClientIdsRange(int min, int max);

    Integer getMaxRowByTableTravels();

    AggregateDTO getAggregateByClientId(Long clientId);
}
