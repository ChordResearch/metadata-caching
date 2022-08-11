package com.chord.nft.metadata.repository;

import com.chord.nft.metadata.entity.Global;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class GlobalRepository extends Repository {

    public Global getCurrentBlock(Connection connection) {
        Global global = getJdbcTemplate(connection).queryForObject("Select * from global limit 1", BeanPropertyRowMapper.newInstance(Global.class));
        return global;
    }

    public int incrementBlock(int incrementBy, Connection connection) {
        return getJdbcTemplate(connection).update("Update global set block = block + ?, \"updatedAt\"=? where status = true",
                new Object[]{incrementBy, new java.sql.Timestamp(new java.util.Date().getTime())}
        );
    }
}
