package com.example.people_tracker.services;

import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;
import com.example.people_tracker.repositories.DaoTravel;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ServiceImpl implements TravelService {

    private final DaoTravel dao;

    public ServiceImpl(DaoTravel dao) {
        this.dao = dao;
    }

    @Override
    public void add(TravelDTO travelDTO) {
        dao.add(travelDTO);
    }

    @Override
    public List<ClientDTO> getClientList() {
        return dao.getClientList();
    }
}
