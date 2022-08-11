package com.chord.nft.metadata.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;

public class Repository {
    public JdbcTemplate getJdbcTemplate(Connection connection) {
        return new JdbcTemplate(new SingleConnectionDataSource(connection, false));
    }
}
