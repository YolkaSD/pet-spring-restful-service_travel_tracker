package com.example.people_tracker.repositories;

import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DaoImpl implements DaoTravel {

    private final JdbcTemplate jdbcTemplate;

    public DaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    //@Transaction
    public void add(TravelDTO travelDTO) {
        String sql = """
                INSERT INTO store_travel.travels
                (client_id, name, surname,
                birthday, transport_type, departure_city_name,
                destination_city_name, departure_time, destination_time)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        Object[] args = {
                travelDTO.getClientId(), travelDTO.getName(), travelDTO.getSurname(),
                travelDTO.getBirthday(), travelDTO.getTransportType().toString(), travelDTO.getDepartureCityName(),
                travelDTO.getDestinationCityName(), travelDTO.getDepartureTime(), travelDTO.getDestinationTime()
        };

        jdbcTemplate.update(sql, args);

    }

    @Override
    public List<ClientDTO> getClientList() {
        String sql = """
                SELECT DISTINCT client_id, name, surname
                FROM store_travel.travels;
                """;

        return jdbcTemplate.query(sql, new ClientRowMapper());
    }
}
