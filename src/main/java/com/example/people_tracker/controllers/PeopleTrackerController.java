package com.example.people_tracker.controllers;

import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;
import com.example.people_tracker.services.TravelService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PeopleTrackerController {

    private final TravelService travelService;

    public PeopleTrackerController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PostMapping("/travel/add")
    public String addNewTravel(@RequestBody TravelDTO travelDTO) {
        travelService.add(travelDTO);
        return null;
    }

    @PostMapping("/travel/addfromlist")
    public String addNewTravelFromList(@RequestBody List<TravelDTO> travelDTOList) {
        travelService.addNewTravelFromList(travelDTOList);
        return null;
    }

    @GetMapping("/travel/clients")
    public List<ClientDTO> getAllClients() {
        return travelService.getClientList();
    }

//    @PostMapping("/travel/aggregates/calculate/fromdb")
//    public String calculateAggregatesFromDB() {
//        long startTime = System.currentTimeMillis();
//        travelService.calculateAggregatesFromDB();
//        long endTime = System.currentTimeMillis();
//        System.out.println("TimeMillis: " + (startTime - endTime));
//        return null;
//    }

    @PostMapping("/travel/aggregates/calculate/fromjava")
    public String calculateAggregatesFromJava() {
        long startTime = System.currentTimeMillis();
        travelService.calculateAggregatesFromJava();
        long endTime = System.currentTimeMillis();
        System.out.println("TimeMillis: " + (endTime - startTime));

        return null;
    }

//    @GetMapping("/travel/{client_id}/aggregates")
//    public ClientAggregatesDTO getClientAggregates(@PathVariable("client_id") Long clientId) {
//        return null;
//    }

}
