package ru.currency.exchange.chulkova.db;

import java.sql.*;

public class DatabaseConnection {

    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:C:/Users/chulk/";
    public static final String DATABASE = "sqlitecurrency-exchange.db";

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL + DATABASE);
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_TABLES);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String CREATE_TABLES = "DROP TABLE exchangeRate;\n" +
            "DROP TABLE currency;\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS currency (\n" +
            "                          id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "                          code VARCHAR NOT NULL ,\n" +
            "                          full_name VARCHAR NOT NULL ,\n" +
            "                          sign VARCHAR NOT NULL );\n" +
            "\n" +
            "CREATE UNIQUE INDEX IF NOT EXISTS idx_code ON currency (code);\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS exchangeRate (\n" +
            "                              id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "                              base_currency_id INTEGER NOT NULL ,\n" +
            "                              target_currency_id INTEGER NOT NULL ,\n" +
            "                              rate DECIMAL(6) NOT NULL ,\n" +
            "                              FOREIGN KEY (base_currency_id) REFERENCES currency (id) ON DELETE CASCADE,\n" +
            "                              FOREIGN KEY (target_currency_id) REFERENCES currency (id) ON DELETE CASCADE );\n" +
            "\n" +
            "CREATE UNIQUE INDEX IF NOT EXISTS idx_base_target ON ExchangeRate (base_currency_id, target_currency_id);\n" +
            "\n" +
            "INSERT INTO currency (code, full_name, sign)\n" +
            "VALUES ('AUD', 'Australian dollar', 'A$'),\n" +
            "       ('USD', 'US dollar', '$'),\n" +
            "       ('RUB', 'Russian ruble', '₽'),\n" +
            "       ('EUR', 'Euro', '€');\n" +
            "\n" +
            "INSERT INTO exchangeRate (base_currency_id, target_currency_id, rate)\n" +
            "VALUES (2, 3, 90.59), (2, 4, 0.89);";
}
