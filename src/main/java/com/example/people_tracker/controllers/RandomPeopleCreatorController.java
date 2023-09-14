package com.example.people_tracker.controllers;

import com.example.people_tracker.services.TravelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomPeopleCreatorController {

    private final TravelService service;

    public RandomPeopleCreatorController(TravelService service) {
        this.service = service;
    }

    @GetMapping("/peoplecreator")
    public String createPeople() {
        service.createPeople();
        return "dasdasdas";
    }
}
