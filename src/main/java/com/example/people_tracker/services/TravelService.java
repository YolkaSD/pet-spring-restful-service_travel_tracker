package com.example.people_tracker.services;

import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;

import java.util.List;

public interface TravelService {
    void add(TravelDTO travelDTO);

    List<ClientDTO> getClientList();
}
