package com.example.people_tracker.repositories.people_creator_rep;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DaoPeopleCreatorImpl implements DaoPeopleCreator {

    final JdbcTemplate jdbcTemplate;

    public DaoPeopleCreatorImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long getMaxId() {
        String sql = "SELECT MAX(client_id) FROM store_travel.travels";
        Long id = jdbcTemplate.queryForObject(sql, Long.class);
        return id != null ? id : 0L;
    }
}
