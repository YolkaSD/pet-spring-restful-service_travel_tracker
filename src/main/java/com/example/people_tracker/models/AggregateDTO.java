package com.example.people_tracker.models;

import lombok.Data;

@Data
public class AggregateDTO {
    private long clientId;
    private int cntAllTrans;
    private int cntAllTransOneYear;
    private int cntAllTransFiveYears;
    private int cntAllTransBeforeEighteenYears;
    private int cntAllTransAfterEighteenYears;
    private int maxCntOfDaysInSamePlace;
    private int minCntOfDaysInSamePlace;
    private double avgCntOfDaysInSamePlace;
    private int cntAllTransCar;
    private int cntAllTransBus;
    private int cntAllTransPlane;
    private int cntAllTransTrain;
}
