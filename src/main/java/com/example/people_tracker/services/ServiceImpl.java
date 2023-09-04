package com.example.people_tracker.services;

import com.example.people_tracker.models.AggregateDTO;
import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;
import com.example.people_tracker.models.TravelStatistic;
import com.example.people_tracker.repositories.DaoTravel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ServiceImpl implements TravelService {

    private final DaoTravel dao;
    private final AggregateCalculator calculateAggregate;

    @Autowired
    public ServiceImpl(DaoTravel dao, AggregateCalculator calculateAggregate) {
        this.dao = dao;
        this.calculateAggregate = calculateAggregate;
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
        // 1 - получить список client_id
        List<Long> clientIdList = dao.getClientIdList();
        // 2 - получить листы всех записи каждого client_id
        clientIdList.parallelStream().forEach(clientId -> {
            // Получить список записей для каждого client_id
            List<TravelDTO> travelDTOList = dao.getTravelDTOList(clientId);
            // 3 - Для каждого элемента списка выполнить расчет
            // 3.1 - Количество перемещений всего за все время
            int cntAllTrans = calculateAggregate.findCountAllTrans(travelDTOList);
            // 3.2 - Количество перемещений за последний год
            int cntAllTransOneYear = (int) calculateAggregate.findCountAllTransInLastYears(travelDTOList, 1);
            // 3.3 - Количество перемещений за последние 5 лет
            int cntAllTransFiveYears = calculateAggregate.findCountAllTransInLastYears(travelDTOList, 5);
            // 3.4 - Количество перемещений до 18 лет
            int cntAllTransBeforeEighteenYears = calculateAggregate.findCountTransByAge(travelDTOList, 18, true);
            // 3.5 - Количество перемещений после 18 лет
            int cntAllTransAfterEighteenYears = calculateAggregate.findCountTransByAge(travelDTOList, 18, false);
            // 3.6 - Максимальное количество дней без перемещений
            // 3.7 - Mинимальное количество дней без перемещений
            // 3.8 - Среднее количество дней без перемещений
            TravelStatistic travelStatistic = calculateAggregate.findTravelStatistics(travelDTOList);
            int minCntOfDaysInSamePlace = travelStatistic.getMax();
            int maxCntOfDaysInSamePlace = travelStatistic.getMin();
            double avgCntOfDaysInSamePlace = travelStatistic.getAvg();
            // 3.9 - Количество перемещений на машине
            int cntAllTransCar = calculateAggregate.findCountAllTransByType(travelDTOList, "CAR");
            // 3.10 - Количество перемещений на автобусе
            int cntAllTransBus = calculateAggregate.findCountAllTransByType(travelDTOList, "BUS");
            // 3.11 - Количество перемещений на самолете
            int cntAllTransPlane = calculateAggregate.findCountAllTransByType(travelDTOList, "PLANE");
            // 3.12 - Количество перемещений на поезде
            int cntAllTransTrain = calculateAggregate.findCountAllTransByType(travelDTOList, "TRAIN");

            AggregateDTO aggregateDTO = new AggregateDTO();

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
        });
    }
}
