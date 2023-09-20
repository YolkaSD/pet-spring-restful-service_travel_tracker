package com.example.people_tracker.services;

import com.example.people_tracker.models.*;
import com.example.people_tracker.repositories.DaoTravel;
import com.example.people_tracker.services.aggregate_calculator.AggregateCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;


@Service
public class TravelServiceImpl implements TravelService {

    private final DaoTravel dao;
    private final ExecutorService executorService;

    @Autowired
    public TravelServiceImpl(DaoTravel dao) {

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.dao = dao;
    }

    @Override
    public void add(TravelDTO travelDTO) {

        dao.add(travelDTO);
    }

    @Override
    public void addNewTravelFromList(List<TravelDTO> travelDTOList) {

        dao.addNewTravelFromList(travelDTOList);
    }

    @Override
    public List<ClientDTO> getClientList() {

        return dao.getClientList();
    }

    @Override
    public AggregateDTO getAggregateByClientId(Long clientId) {

        return dao.getAggregateByClientId(clientId);
    }

    @Override
    public void calculateAggregates() {
        int maxRow = dao.getMaxRowByTableTravels();
        int step = 500;
        int currentStep = 0;
        while (currentStep < (maxRow + step)) {

            List<TravelDTO> travelDTOList = dao.getTravelsByClientIdsRange(currentStep, (currentStep + step));
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    List<AggregateDTO> aggregateDTOList = new ArrayList<>();
                    for (Long clientId : travelDTOList.stream().map(TravelDTO::getClientId).distinct().toList()) {
                        List<TravelDTO> travelByIdList = travelDTOList.stream().filter(travelDTO -> travelDTO.getClientId().equals(clientId)).toList();
                        aggregateDTOList.add(AggregateCalculator.createAggregateDTO(travelByIdList));
                    }
                    dao.calculateAggregates(aggregateDTOList);
                }
            });

            currentStep += step;
        }
        executorService.shutdown();
    }

}
