package com.sccpa.winelist.data;

import com.sccpa.winelist.config.SqlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Repository
public class WineRepository {
    private JdbcTemplate jdbcTemplate;
    private SqlConfig sqlConfig;

    @Autowired
    public WineRepository(final JdbcTemplate template, final SqlConfig config) {
        jdbcTemplate = template;
        sqlConfig = config;
    }

    public List<WineEntry> fetchEntireList() {
        return queryList(sqlConfig.getFetchEntireList());
    }

    public long insertEntry(final WineEntry entry) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator preparedStatementCreator = connection -> {
            final PreparedStatement statement = connection.prepareStatement(
                    sqlConfig.getInsertEntry(), new String[] {"id"});
//            (producer, name, type, year, price, qty, bin, ready, rating)

            statement.setString(1, entry.getProducer());
            statement.setString(2, entry.getName());
            statement.setString(3, entry.getType());
            statement.setString(4, entry.getYear());
            statement.setString(5, entry.getPrice());
            statement.setString(6, entry.getQty());
            statement.setString(7, entry.getBin());
            statement.setString(8, entry.getReady());
            statement.setString(9, entry.getRating());
            return statement;
        };

        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private List<WineEntry> queryList(final String sql, final Object... parameters) {
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<WineEntry>(WineEntry.class), parameters);
        }
        catch (final EmptyResultDataAccessException ex) {
            // noinspection unchecked
            return Collections.EMPTY_LIST;
        }
    }
}
