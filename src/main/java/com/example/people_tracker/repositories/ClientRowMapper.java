package com.example.people_tracker.repositories;

import com.example.people_tracker.models.ClientDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRowMapper implements RowMapper<ClientDTO> {
    @Override
    public ClientDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId((rs.getLong("client_id")));
        clientDTO.setName((rs.getString("name")));
        clientDTO.setSurname((rs.getString("surname")));
        return clientDTO;
    }
}
