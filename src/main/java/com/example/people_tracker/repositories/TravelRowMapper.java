package com.example.people_tracker.repositories;

import com.example.people_tracker.models.TransportType;
import com.example.people_tracker.models.TravelDTO;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TravelRowMapper implements RowMapper<TravelDTO> {
    @Override
    public TravelDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        TravelDTO travelDTO = new TravelDTO();
        travelDTO.setId(rs.getLong("id"));
        travelDTO.setClientId(rs.getLong("client_id"));
        travelDTO.setName(rs.getString("name"));
        travelDTO.setSurname(rs.getString("surname"));
        travelDTO.setBirthday(rs.getDate("birthday").toLocalDate());
        travelDTO.setTransportType(TransportType.valueOf(rs.getString("transport_type")));
        travelDTO.setDepartureCityName(rs.getString("departure_city_name"));
        travelDTO.setDestinationCityName(rs.getString("destination_city_name"));
        travelDTO.setDepartureTime(rs.getTimestamp("departure_time").toLocalDateTime());
        travelDTO.setDestinationTime(rs.getTimestamp("destination_time").toLocalDateTime());
        return travelDTO;
    }
}
