package ru.currency.exchange.chulkova.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@NoArgsConstructor
public class DataSource {

    private static final HikariConfig config;
    private static final HikariDataSource ds;

    static {
        try {
            config = new HikariConfig(
                    PropertiesLoader.loadProperties("datasource.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
