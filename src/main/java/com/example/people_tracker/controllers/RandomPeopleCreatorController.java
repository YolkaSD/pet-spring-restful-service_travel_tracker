package com.example.people_tracker.controllers;

import com.example.people_tracker.services.TravelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/creator")
public class RandomPeopleCreatorController {

    private final TravelService service;

    public RandomPeopleCreatorController(TravelService service) {
        this.service = service;
    }

    @GetMapping("/people")
    public void createPeople() {

        long startTime = System.currentTimeMillis();
        service.createPeople();
        long endTime = System.currentTimeMillis();
        System.out.println("TimeMillis: " + (endTime - startTime));
    }
}
