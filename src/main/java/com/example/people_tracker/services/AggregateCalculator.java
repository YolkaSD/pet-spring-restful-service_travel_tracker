package com.example.people_tracker.services;

import com.example.people_tracker.models.TravelDTO;
import com.example.people_tracker.models.TravelStatistic;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class AggregateCalculator {

    public int findCountAllTrans(List<TravelDTO> travelDTOList) {

        return travelDTOList.size();
    }

    public int findCountAllTransInLastYears(List<TravelDTO> travelDTOList, int years) {

        return (int) travelDTOList.stream()
                .map(TravelDTO::getDepartureTime)
                .filter(localDateTime -> localDateTime.isAfter(LocalDateTime.now().minusYears(years)))
                .count();
    }

    public int findCountTransByAge(List<TravelDTO> travelDTOList, int age, boolean before) {

        return (int) travelDTOList.stream().
                map(travelDTO -> new Pair<>(travelDTO.getBirthday(), travelDTO.getDepartureTime()))
                .filter(pair -> isTransBeforeEighteen(pair, age, before))
                .count();
    }

    public int findCountAllTransByType(List<TravelDTO> travelDTOList, String transport) {

        return (int) travelDTOList.stream()
                .map(TravelDTO::getTransportType)
                .filter(transportType -> transportType.toString().equals(transport))
                .count();
    }

    public TravelStatistic findTravelStatistics(List<TravelDTO> travelDTOList) {

        TravelStatistic travelStatistic = new TravelStatistic();

        if (travelDTOList.size() < 2) {
            return travelStatistic;
        }

        int max = 0;
        int min = Integer.MAX_VALUE;
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
        double avg = (double) sum / count;

        travelStatistic.setMax(max);
        travelStatistic.setMin(min);
        travelStatistic.setAvg(avg);

        return travelStatistic;
    }

    private boolean isTransBeforeEighteen(Pair<LocalDate, LocalDateTime> pair, int age, boolean before) {
        LocalDate birthday = pair.a;
        LocalDateTime departureTime = pair.b;

        return before == departureTime.isBefore(birthday.plusYears(age).atStartOfDay());
    }


}
