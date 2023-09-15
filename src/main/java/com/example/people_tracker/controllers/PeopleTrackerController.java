package com.example.people_tracker.controllers;

import com.example.people_tracker.models.AggregateDTO;
import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;
import com.example.people_tracker.services.TravelService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/travel")
public class PeopleTrackerController {

    private final TravelService travelService;

    public PeopleTrackerController(TravelService travelService) {

        this.travelService = travelService;
    }

    @PostMapping("/add")
    public void addNewTravel(@RequestBody TravelDTO travelDTO) {

        travelService.add(travelDTO);
    }

    @PostMapping("/addfromlist")
    public void addNewTravelFromList(@RequestBody List<TravelDTO> travelDTOList) {

        travelService.addNewTravelFromList(travelDTOList);
    }

    @GetMapping("/clients")
    public List<ClientDTO> getAllClients() {

        return travelService.getClientList();
    }

    @PostMapping("/aggregates/calculate")
    public void calculateAggregates() {

        long startTime = System.currentTimeMillis();
        travelService.calculateAggregates();
        long endTime = System.currentTimeMillis();
        System.out.println("TimeMillis: " + (endTime - startTime));

    }

    @GetMapping("/{client_id}/aggregates")
    public AggregateDTO getClientAggregates(@PathVariable("client_id") Long clientId) {

        return travelService.getAggregateByClientId(clientId);
    }

}
