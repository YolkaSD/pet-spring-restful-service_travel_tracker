package com.example.people_tracker.controllers;

import com.example.people_tracker.models.TravelDTO;
import com.example.people_tracker.services.TravelService;
import org.springframework.web.bind.annotation.*;

@RestController
public class PeopleTrackerController {

    private final TravelService travelService;

    public PeopleTrackerController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PostMapping("/travel/add")
    public String addNewTravel(@RequestBody TravelDTO travelDTO) {
        System.out.println(travelDTO);
        travelService.add(travelDTO);
        return null;
    }

//    @GetMapping("/travel/clients")
//    public List<ClientDTO> getAllClients() {
//        return null;
//    }
//
//    @PostMapping("/travel/aggregates/calculate")
//    public String calculateAggregates() {
//        return null;
//    }
//
//    @GetMapping("/travel/{client_id}/aggregates")
//    public ClientAggregatesDTO getClientAggregates(@PathVariable("client_id") Long clientId) {
//        return null;
//    }

}
