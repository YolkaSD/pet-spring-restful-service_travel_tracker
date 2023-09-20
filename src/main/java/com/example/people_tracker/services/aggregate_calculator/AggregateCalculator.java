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

    private static TravelStatistic travelStatistic = null;

    public static AggregateDTO createAggregateDTO(List<TravelDTO> travelDTOList) {
        travelStatistic = new TravelStatistic(travelDTOList);

        return AggregateDTO.builder()
                .clientId(travelDTOList.get(0).getClientId())
                .cntAllTrans(findCountAllTrans(travelDTOList))
                .cntAllTransOneYear(findCountAllTransInLastYears(travelDTOList, 1))
                .cntAllTransFiveYears(findCountAllTransInLastYears(travelDTOList, 5))
                .cntAllTransBeforeEighteenYears(findCountTransByAge(travelDTOList, 18, true))
                .cntAllTransAfterEighteenYears(findCountTransByAge(travelDTOList, 18, false))
                .maxCntOfDaysInSamePlace(getMax())
                .minCntOfDaysInSamePlace(getMin())
                .avgCntOfDaysInSamePlace(getAvg())
                .cntAllTransCar(findCountAllTransByType(travelDTOList, "CAR"))
                .cntAllTransBus(findCountAllTransByType(travelDTOList, "BUS"))
                .cntAllTransPlane(findCountAllTransByType(travelDTOList, "PLANE"))
                .cntAllTransTrain(findCountAllTransByType(travelDTOList, "TRAIN"))
                .build();
    }


    private static int findCountAllTrans(List<TravelDTO> travelDTOList) {

        return travelDTOList.size();
    }

    private static int findCountAllTransInLastYears(List<TravelDTO> travelDTOList, int years) {

        return (int) travelDTOList.stream()
                .map(TravelDTO::getDepartureTime)
                .filter(localDateTime -> localDateTime.isAfter(LocalDateTime.now().minusYears(years)))
                .count();
    }

    private static int findCountTransByAge(List<TravelDTO> travelDTOList, int age, boolean before) {

        return (int) travelDTOList.stream().
                map(travelDTO -> new Pair<>(travelDTO.getBirthday(), travelDTO.getDepartureTime()))
                .filter(pair -> isTransBeforeEighteen(pair, age, before))
                .count();
    }

    private static int findCountAllTransByType(List<TravelDTO> travelDTOList, String transport) {

        return (int) travelDTOList.stream()
                .map(TravelDTO::getTransportType)
                .filter(transportType -> transportType.toString().equals(transport))
                .count();
    }


    private static boolean isTransBeforeEighteen(Pair<LocalDate, LocalDateTime> pair, int age, boolean before) {

        LocalDate birthday = pair.a;
        LocalDateTime departureTime = pair.b;

        return before == departureTime.isBefore(birthday.plusYears(age).atStartOfDay());
    }

    private static int getMax() {

        return travelStatistic.max;
    }

    private static int getMin() {

        return travelStatistic.min;
    }

    private static double getAvg() {

        return travelStatistic.avg;
    }

    @Data
    static class TravelStatistic {

        private int max = 0;
        private int min = Integer.MAX_VALUE;
        private double avg = 0;

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
