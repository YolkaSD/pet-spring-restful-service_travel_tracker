package com.example.people_tracker.services.people_creator;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ResourceGenerator {

    public List<String> fetchFromURL(String urlString) {
        List<String> result = new ArrayList<>();
        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.add(line);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public int generateRandomValue(int value) {
        return generateRandomValue(0, value);
    }

    public int generateRandomValue(int minValue, int maxValue) {
        return ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
    }

    public LocalDate generateLocalDate(int minYear, int maxYear, int minMonth, int maxMonth, int minMonthDay, int maxMonthDay) {
        int year = generateRandomValue(minYear, maxYear);
        int month = generateRandomValue(minMonth, maxMonth);
        int maxDay;
        if (maxMonthDay > 28) {
            switch (month) {
                case 2 -> {
                    if (Year.of(year).isLeap()) {
                        maxDay = 29;
                    } else {
                        maxDay = 28;
                    }
                }
                case 4, 6, 9, 11 -> maxDay = 30;
                default -> maxDay = 31;
            }
        } else {
            maxDay = maxMonthDay;
        }

        int day = generateRandomValue(minMonthDay, maxDay);

        return LocalDate.of(year, month, day);
    }

    @Data
    static class TravelStatistic {

        private int max;
        private int min;
        private double avg;
    }

}
