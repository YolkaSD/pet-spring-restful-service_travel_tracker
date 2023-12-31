package com.example.people_tracker.repositories;

import com.example.people_tracker.models.AggregateDTO;
import com.example.people_tracker.models.ClientDTO;
import com.example.people_tracker.models.TravelDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DaoImpl implements DaoTravel {

    private final JdbcTemplate jdbcTemplate;

    public DaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(TravelDTO travelDTO) {

        String sql = """
                INSERT INTO
                    store_travel.travels
                    (client_id, name, surname,
                    birthday, transport_type, departure_city_name,
                    destination_city_name, departure_time, destination_time)
                VALUES
                    (?, ?, ?,
                    ?, ?, ?,
                    ?, ?, ?);
                """;

        Object[] args = {
                travelDTO.getClientId(), travelDTO.getName(), travelDTO.getSurname(),
                travelDTO.getBirthday(), travelDTO.getTransportType().toString(), travelDTO.getDepartureCityName(),
                travelDTO.getDestinationCityName(), travelDTO.getDepartureTime(), travelDTO.getDestinationTime()
        };

        jdbcTemplate.update(sql, args);

    }

    @Override
    public void addNewTravelFromList(List<TravelDTO> travelDTOList) {

        String sql = """
                INSERT INTO store_travel.travels
                (client_id, name, surname,
                birthday, transport_type, departure_city_name,
                destination_city_name, departure_time, destination_time)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        jdbcTemplate.batchUpdate(sql, new ClientBatchPreparedStatementSetter(travelDTOList));

    }

    @Override
    public List<ClientDTO> getClientList() {

        String sql = """
                SELECT DISTINCT client_id, name, surname
                FROM store_travel.travels
                ORDER BY
                    client_id;
                """;

        return jdbcTemplate.query(sql, new ClientRowMapper());
    }


    @Override
    public void calculateAggregates(AggregateDTO aggregateDTO) {

        String sql = """
                INSERT INTO
                    store_travel.travel_aggregates
                    (client_id, cnt_all_trans, cnt_all_trans_1year,
                    cnt_all_trans_5years, cnt_all_trans_before18yo, cnt_all_trans_after18yo,
                    max_cnt_of_days_in_same_place, min_cnt_of_days_in_same_place, avg_cnt_of_days_in_same_place,
                    cnt_all_trans_car, cnt_all_trans_bus, cnt_all_trans_plane, cnt_all_trans_train)
                VALUES
                    (?,?,?,
                    ?,?,?,
                    ?,?,?,
                    ?,?,?,?)
                ON CONFLICT
                    (client_id)
                DO UPDATE SET
                    cnt_all_trans = EXCLUDED.cnt_all_trans,
                    cnt_all_trans_1year = EXCLUDED.cnt_all_trans_1year,
                    cnt_all_trans_5years = EXCLUDED.cnt_all_trans_5years,
                    cnt_all_trans_before18yo = EXCLUDED.cnt_all_trans_before18yo,
                    cnt_all_trans_after18yo = EXCLUDED.cnt_all_trans_after18yo,
                    max_cnt_of_days_in_same_place = EXCLUDED.max_cnt_of_days_in_same_place,
                    min_cnt_of_days_in_same_place = EXCLUDED.min_cnt_of_days_in_same_place,
                    avg_cnt_of_days_in_same_place = EXCLUDED.avg_cnt_of_days_in_same_place,
                    cnt_all_trans_car = EXCLUDED.cnt_all_trans_car,
                    cnt_all_trans_bus = EXCLUDED.cnt_all_trans_bus,
                    cnt_all_trans_plane = EXCLUDED.cnt_all_trans_plane,
                    cnt_all_trans_train = EXCLUDED.cnt_all_trans_train
                """;

        Object[] args = {
                aggregateDTO.getClientId(), aggregateDTO.getCntAllTrans(), aggregateDTO.getCntAllTransOneYear(),
                aggregateDTO.getCntAllTransFiveYears(), aggregateDTO.getCntAllTransBeforeEighteenYears(), aggregateDTO.getCntAllTransAfterEighteenYears(),
                aggregateDTO.getMaxCntOfDaysInSamePlace(), aggregateDTO.getMinCntOfDaysInSamePlace(), aggregateDTO.getAvgCntOfDaysInSamePlace(),
                aggregateDTO.getCntAllTransCar(), aggregateDTO.getCntAllTransBus(), aggregateDTO.getCntAllTransPlane(), aggregateDTO.getCntAllTransTrain()
        };

        jdbcTemplate.update(sql, args);
    }

    @Override
    public void calculateAggregates(List<AggregateDTO> aggregateDTOList){

        String sql = """
                INSERT INTO
                    store_travel.travel_aggregates
                    (client_id, cnt_all_trans, cnt_all_trans_1year,
                    cnt_all_trans_5years, cnt_all_trans_before18yo, cnt_all_trans_after18yo,
                    max_cnt_of_days_in_same_place, min_cnt_of_days_in_same_place, avg_cnt_of_days_in_same_place,
                    cnt_all_trans_car, cnt_all_trans_bus, cnt_all_trans_plane, cnt_all_trans_train)
                VALUES
                    (?,?,?,
                    ?,?,?,
                    ?,?,?,
                    ?,?,?,?)
                ON CONFLICT
                    (client_id)
                DO UPDATE SET
                    cnt_all_trans = EXCLUDED.cnt_all_trans,
                    cnt_all_trans_1year = EXCLUDED.cnt_all_trans_1year,
                    cnt_all_trans_5years = EXCLUDED.cnt_all_trans_5years,
                    cnt_all_trans_before18yo = EXCLUDED.cnt_all_trans_before18yo,
                    cnt_all_trans_after18yo = EXCLUDED.cnt_all_trans_after18yo,
                    max_cnt_of_days_in_same_place = EXCLUDED.max_cnt_of_days_in_same_place,
                    min_cnt_of_days_in_same_place = EXCLUDED.min_cnt_of_days_in_same_place,
                    avg_cnt_of_days_in_same_place = EXCLUDED.avg_cnt_of_days_in_same_place,
                    cnt_all_trans_car = EXCLUDED.cnt_all_trans_car,
                    cnt_all_trans_bus = EXCLUDED.cnt_all_trans_bus,
                    cnt_all_trans_plane = EXCLUDED.cnt_all_trans_plane,
                    cnt_all_trans_train = EXCLUDED.cnt_all_trans_train
                """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AggregateDTO aggregateDTO = aggregateDTOList.get(i);
                ps.setLong(1, aggregateDTO.getClientId());
                ps.setInt(2, aggregateDTO.getCntAllTrans());
                ps.setInt(3, aggregateDTO.getCntAllTransOneYear());
                ps.setInt(4, aggregateDTO.getCntAllTransFiveYears());
                ps.setInt(5, aggregateDTO.getCntAllTransBeforeEighteenYears());
                ps.setInt(6, aggregateDTO.getCntAllTransAfterEighteenYears());
                ps.setInt(7, aggregateDTO.getMaxCntOfDaysInSamePlace());
                ps.setInt(8, aggregateDTO.getMinCntOfDaysInSamePlace());
                ps.setDouble(9, aggregateDTO.getAvgCntOfDaysInSamePlace());
                ps.setInt(10, aggregateDTO.getCntAllTransCar());
                ps.setInt(11, aggregateDTO.getCntAllTransBus());
                ps.setInt(12, aggregateDTO.getCntAllTransPlane());
                ps.setInt(13, aggregateDTO.getCntAllTransTrain());
            }

            @Override
            public int getBatchSize() {
                return aggregateDTOList.size();
            }
        });

    }

    @Override
    public List<TravelDTO> getTravelsByClientIdsRange(int min, int max) {

        String sql = """
                SELECT
                    *
                FROM
                    store_travel.travels
                WHERE
                    client_id
                IN (SELECT
                        *
                    FROM
                        pg_catalog.generate_series (?, ?));
                """;

        return jdbcTemplate.query(sql, new TravelRowMapper(), min, max);
    }


    @Override
    public Integer getMaxRowByTableTravels() {

        String sql = """
                SELECT
                    COUNT(distinct client_id)
                 FROM
                    store_travel.travels
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public AggregateDTO getAggregateByClientId(Long clientId) {
        String sql = """
                SELECT
                    *
                FROM
                    store_travel.travel_aggregates
                WHERE
                    client_id = ?
                """;
        return jdbcTemplate.queryForObject(sql, new RowMapper<AggregateDTO>() {
            @Override
            public AggregateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

                AggregateDTO aggregateDTO = AggregateDTO.builder().build();
                aggregateDTO.setClientId(rs.getLong("client_id"));
                aggregateDTO.setCntAllTrans(rs.getInt("cnt_all_trans"));
                aggregateDTO.setCntAllTransOneYear(rs.getInt("cnt_all_trans_1year"));
                aggregateDTO.setCntAllTransFiveYears(rs.getInt("cnt_all_trans_5years"));
                aggregateDTO.setCntAllTransBeforeEighteenYears(rs.getInt("cnt_all_trans_before18yo"));
                aggregateDTO.setCntAllTransAfterEighteenYears(rs.getInt("cnt_all_trans_after18yo"));
                aggregateDTO.setMaxCntOfDaysInSamePlace(rs.getInt("max_cnt_of_days_in_same_place"));
                aggregateDTO.setMinCntOfDaysInSamePlace(rs.getInt("min_cnt_of_days_in_same_place"));
                aggregateDTO.setAvgCntOfDaysInSamePlace(rs.getDouble("avg_cnt_of_days_in_same_place"));
                aggregateDTO.setCntAllTransCar(rs.getInt("cnt_all_trans_car"));
                aggregateDTO.setCntAllTransBus(rs.getInt("cnt_all_trans_bus"));
                aggregateDTO.setCntAllTransPlane(rs.getInt("cnt_all_trans_plane"));
                aggregateDTO.setCntAllTrans(rs.getInt("cnt_all_trans_train"));
                return aggregateDTO;
            }
        }, clientId);
    }
}
