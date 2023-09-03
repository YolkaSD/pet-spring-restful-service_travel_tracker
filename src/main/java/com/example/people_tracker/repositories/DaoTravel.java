package com.example.people_tracker.repositories;

import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;

import java.util.List;

public interface DaoTravel {
    void add(TravelDTO travelDTO);

    List<ClientDTO> getClientList();
}
