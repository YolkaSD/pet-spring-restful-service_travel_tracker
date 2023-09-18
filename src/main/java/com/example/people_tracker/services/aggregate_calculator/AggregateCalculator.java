package com.example.people_tracker.services.aggregate_calculator;

import com.example.people_tracker.models.AggregateDTO;
import com.example.people_tracker.models.TravelDTO;
import lombok.Data;
import org.antlr.v4.runtime.misc.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class AggregateCalculator {

    private final TravelStatistic travelStatistic;

    private final List<TravelDTO> travelDTOList;

    public AggregateCalculator(List<TravelDTO> travelDTOList) {
        this.travelStatistic = new TravelStatistic(travelDTOList);
        this.travelDTOList = travelDTOList;
    }

    private int findCountAllTrans() {

        return travelDTOList.size();
    }

    private int findCountAllTransInLastYears(int years) {

        return (int) travelDTOList.stream()
                .map(TravelDTO::getDepartureTime)
                .filter(localDateTime -> localDateTime.isAfter(LocalDateTime.now().minusYears(years)))
                .count();
    }

    private int findCountTransByAge(int age, boolean before) {

        return (int) travelDTOList.stream().
                map(travelDTO -> new Pair<>(travelDTO.getBirthday(), travelDTO.getDepartureTime()))
                .filter(pair -> isTransBeforeEighteen(pair, age, before))
                .count();
    }

    private int findCountAllTransByType(String transport) {

        return (int) travelDTOList.stream()
                .map(TravelDTO::getTransportType)
                .filter(transportType -> transportType.toString().equals(transport))
                .count();
    }


    private boolean isTransBeforeEighteen(Pair<LocalDate, LocalDateTime> pair, int age, boolean before) {

        LocalDate birthday = pair.a;
        LocalDateTime departureTime = pair.b;

        return before == departureTime.isBefore(birthday.plusYears(age).atStartOfDay());
    }

    private int getMax() {

        return travelStatistic.max;
    }

    private int getMin() {

        return travelStatistic.min;
    }

    private double getAvg() {

        return travelStatistic.avg;
    }

    public AggregateDTO createAggregateDTO(Long clientId) {

        return AggregateDTO.builder()
                .clientId(clientId)
                .cntAllTrans(findCountAllTrans())
                .cntAllTransOneYear(findCountAllTransInLastYears(1))
                .cntAllTransFiveYears(findCountAllTransInLastYears(5))
                .cntAllTransBeforeEighteenYears(findCountTransByAge(18, true))
                .maxCntOfDaysInSamePlace(getMax())
                .minCntOfDaysInSamePlace(getMin())
                .avgCntOfDaysInSamePlace(getAvg())
                .cntAllTransCar(findCountAllTransByType("CAR"))
                .cntAllTransBus(findCountAllTransByType("BUS"))
                .cntAllTransPlane(findCountAllTransByType("PLANE"))
                .cntAllTransTrain(findCountAllTransByType("TRAIN"))
                .build();
    }

    @Data
    static class TravelStatistic {

        int max = 0;
        int min = Integer.MAX_VALUE;
        double avg = 0;

        public TravelStatistic(List<TravelDTO> travelDTOList) {
            findTravelStatistics(travelDTOList);
        }

        private void findTravelStatistics(List<TravelDTO> travelDTOList) {
            int sum = 0;
            for (int i = 0; i < travelDTOList.size() - 1; i++) {
                TravelDTO current = travelDTOList.get(i);
                TravelDTO next = travelDTOList.get(i + 1);

                LocalDateTime destinationTime = current.getDestinationTime();
                LocalDateTime departureTime = next.getDepartureTime();

                int daysWithoutTravel = (int) ChronoUnit.DAYS.between(destinationTime, departureTime);
                if (daysWithoutTravel > max) {
                    max = daysWithoutTravel;
                }
                if (daysWithoutTravel < min) {
                    min = daysWithoutTravel;
                }
                sum += daysWithoutTravel;
            }

            int count = travelDTOList.size() - 1;
            avg = (double) sum / count;

        }

    }

}
