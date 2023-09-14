package com.example.people_tracker.services;

import com.example.people_tracker.models.*;
import com.example.people_tracker.repositories.people_creator_rep.DaoPeopleCreator;
import com.example.people_tracker.repositories.DaoTravel;
import com.example.people_tracker.services.aggregate_calculator.AggregateCalculator;
import com.example.people_tracker.services.aggregate_calculator.TravelStatistic;
import com.example.people_tracker.services.people_creator.ResourceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;


@Service
public class ServiceImpl implements TravelService {

    private final DaoTravel dao;
    private final DaoPeopleCreator daoPeopleCreator;
    private final AggregateCalculator calculateAggregate;
    private final ResourceGenerator resourceGenerator;


    @Autowired
    public ServiceImpl(DaoTravel dao, DaoPeopleCreator daoPeopleCreator, AggregateCalculator calculateAggregate, ResourceGenerator resourceGetter) {
        this.dao = dao;
        this.daoPeopleCreator = daoPeopleCreator;
        this.calculateAggregate = calculateAggregate;
        this.resourceGenerator = resourceGetter;
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
    public void calculateAggregatesFromDB() {
        dao.calculateAggregatesFromDB();
    }

    @Override
    public void calculateAggregatesFromJava() {
        int maxRow = dao.getMaxRowByTableTravels();
        int step = 1500;
        int currentStep = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        while (currentStep < (maxRow + step)) {

            List<TravelDTO> travelDTOList = dao.getTravelsByClientIdsRange(currentStep, (currentStep + step));

            for (Long clientId : travelDTOList.stream().map(TravelDTO::getClientId).distinct().toList()) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        AggregateDTO aggregateDTO = new AggregateDTO();
                        List<TravelDTO> travelByIdList = travelDTOList.stream().filter(travelDTO -> travelDTO.getClientId().equals(clientId)).toList();
                        int cntAllTrans = calculateAggregate.findCountAllTrans(travelByIdList);
                        int cntAllTransOneYear = calculateAggregate.findCountAllTransInLastYears(travelByIdList, 1);
                        int cntAllTransFiveYears = calculateAggregate.findCountAllTransInLastYears(travelByIdList, 5);
                        int cntAllTransBeforeEighteenYears = calculateAggregate.findCountTransByAge(travelByIdList, 18, true);
                        int cntAllTransAfterEighteenYears = calculateAggregate.findCountTransByAge(travelByIdList, 18, false);
                        TravelStatistic travelStatistic = calculateAggregate.findTravelStatistics(travelByIdList);
                        int minCntOfDaysInSamePlace = travelStatistic.getMax();
                        int maxCntOfDaysInSamePlace = travelStatistic.getMin();
                        double avgCntOfDaysInSamePlace = travelStatistic.getAvg();
                        int cntAllTransCar = calculateAggregate.findCountAllTransByType(travelByIdList, "CAR");
                        int cntAllTransBus = calculateAggregate.findCountAllTransByType(travelByIdList, "BUS");
                        int cntAllTransPlane = calculateAggregate.findCountAllTransByType(travelByIdList, "PLANE");
                        int cntAllTransTrain = calculateAggregate.findCountAllTransByType(travelByIdList, "TRAIN");

                        aggregateDTO.setClientId(clientId);
                        aggregateDTO.setCntAllTrans(cntAllTrans);
                        aggregateDTO.setCntAllTransOneYear(cntAllTransOneYear);
                        aggregateDTO.setCntAllTransFiveYears(cntAllTransFiveYears);
                        aggregateDTO.setCntAllTransBeforeEighteenYears(cntAllTransBeforeEighteenYears);
                        aggregateDTO.setCntAllTransAfterEighteenYears(cntAllTransAfterEighteenYears);
                        aggregateDTO.setMaxCntOfDaysInSamePlace(maxCntOfDaysInSamePlace);
                        aggregateDTO.setMinCntOfDaysInSamePlace(minCntOfDaysInSamePlace);
                        aggregateDTO.setAvgCntOfDaysInSamePlace(avgCntOfDaysInSamePlace);
                        aggregateDTO.setCntAllTransCar(cntAllTransCar);
                        aggregateDTO.setCntAllTransBus(cntAllTransBus);
                        aggregateDTO.setCntAllTransPlane(cntAllTransPlane);
                        aggregateDTO.setCntAllTransTrain(cntAllTransTrain);
                        dao.calculateAggregatesFromJava(aggregateDTO);
                    }
                });
            }

            currentStep += step;
        }
        executorService.shutdown();
    }

    @Override
    public void createPeople() {
        String nameUrl = "https://raw.githubusercontent.com/YolkaSD/text_resources/master/names.txt";
        String surnameUrl = "https://raw.githubusercontent.com/YolkaSD/text_resources/master/surnames.txt";
        String cityUrl = "https://raw.githubusercontent.com/YolkaSD/text_resources/master/worldcities.txt";

        List<String> names = resourceGenerator.fetchFromURL(nameUrl);
        List<String> surnames = resourceGenerator.fetchFromURL(surnameUrl);
        List<String> cities = resourceGenerator.fetchFromURL(cityUrl);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        long id = daoPeopleCreator.getMaxId() + 1;

        while (id <= 40_000) {
            String name = names.get(resourceGenerator.generateRandomValue(names.size() - 1));
            String surname = surnames.get(resourceGenerator.generateRandomValue(surnames.size() - 1));
            LocalDate birthDay = resourceGenerator.generateLocalDate(1950, 2000, 1, 12, 1, 31);

            TravelDTO travelDTO = new TravelDTO();
            travelDTO.setClientId(id);
            travelDTO.setName(name);
            travelDTO.setSurname(surname);
            travelDTO.setBirthday(birthDay);

            int i = 1;
            while (i <= 25) {

                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        String departureCityName = cities.get(resourceGenerator.generateRandomValue(cities.size() - 1));
                        String destinationCityName = cities.get(resourceGenerator.generateRandomValue(cities.size() - 1));
                        TransportType transportType = TransportType.values()[resourceGenerator.generateRandomValue(TransportType.values().length - 1)];

                        LocalDateTime departureTime = resourceGenerator.generateLocalDate(
                                travelDTO.getBirthday().getYear() + 10, 2015,
                                1, 12,
                                1, 31
                        ).atTime(resourceGenerator.generateRandomValue(23), resourceGenerator.generateRandomValue(59), 0);

                        LocalDateTime destinationTime = LocalDateTime.from(departureTime).plusDays(resourceGenerator.generateRandomValue(30)).plusHours(30).plusMinutes(30);


                        travelDTO.setTransportType(transportType);
                        travelDTO.setDepartureCityName(departureCityName);
                        travelDTO.setDestinationCityName(destinationCityName);
                        travelDTO.setDepartureTime(departureTime);
                        travelDTO.setDestinationTime(destinationTime);
                        dao.add(travelDTO);
                    }
                });

                i++;
            }
            id++;

        }
        executorService.shutdown();
    }
}
