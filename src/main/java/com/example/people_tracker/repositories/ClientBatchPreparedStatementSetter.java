package com.example.people_tracker.repositories;

import com.example.people_tracker.models.TravelDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ClientBatchPreparedStatementSetter implements BatchPreparedStatementSetter {

    private final List<TravelDTO> travelDTOList;

    public ClientBatchPreparedStatementSetter(List<TravelDTO> travelDTOList) {
        this.travelDTOList = travelDTOList;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        TravelDTO travelDTO = travelDTOList.get(i);
        ps.setLong(1, travelDTO.getClientId());
        ps.setString(2, travelDTO.getName());
        ps.setString(3, travelDTO.getSurname());
        ps.setDate(4, Date.valueOf(travelDTO.getBirthday()));
        ps.setString(5, travelDTO.getTransportType().toString());
        ps.setString(6, travelDTO.getDepartureCityName());
        ps.setString(7, travelDTO.getDestinationCityName());
        ps.setTimestamp(8, Timestamp.valueOf(travelDTO.getDepartureTime()));
        ps.setTimestamp(9, Timestamp.valueOf(travelDTO.getDestinationTime()));
    }

    @Override
    public int getBatchSize() {
        return travelDTOList.size();
    }
}
