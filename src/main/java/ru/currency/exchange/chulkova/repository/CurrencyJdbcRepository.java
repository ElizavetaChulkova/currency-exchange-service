package ru.currency.exchange.chulkova.repository;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.db.DatabaseConnection;
import ru.currency.exchange.chulkova.model.CurrencyModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.currency.exchange.chulkova.db.DatabaseConnection.DRIVER;
import static ru.currency.exchange.chulkova.db.DatabaseConnection.URL;

@Slf4j
public class CurrencyJdbcRepository implements BaseRepository<CurrencyModel> {

    private static final String SELECT_ALL = "SELECT * FROM currency";
    private static final String SELECT_BY_CODE = "SELECT * FROM currency WHERE code=?";
    private static final String SELECT_BY_ID = "SELECT * FROM currency WHERE id = ?";
    private static final String CREATE = "INSERT INTO currency (code, full_name, sign) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE currency SET code = ?, full_name = ?, sign = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM currency WHERE id = ?";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static CurrencyModel of(ResultSet rs) {
        CurrencyModel currency = new CurrencyModel();
        try {
            currency.setId(rs.getInt("id"));
            currency.setCode(rs.getString("code"));
            currency.setFullName(rs.getString("full_name"));
            currency.setSign(rs.getString("sign"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return currency;
    }

    public List<CurrencyModel> getAll() {
        List<CurrencyModel> currencies = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DatabaseConnection.URL)) {
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                CurrencyModel currency = of(resultSet);
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CurrencyModel getByCode(String code) {
        try (Connection connection = DriverManager.getConnection(DatabaseConnection.URL)) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_CODE);
            ps.setString(1, code);
            ResultSet resultSet = ps.executeQuery();
            CurrencyModel currency = new CurrencyModel();
            while (resultSet.next()) {
                currency = of(resultSet);
            }
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CurrencyModel> getById(int id) {
        try (Connection connection = DriverManager.getConnection(DatabaseConnection.URL)) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            CurrencyModel currency = new CurrencyModel();
            while (resultSet.next()) {
                currency = of(resultSet);
            }
            return Optional.of(currency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CurrencyModel create(CurrencyModel currency) {
        try (Connection connection = DriverManager.getConnection(DatabaseConnection.URL)) {
            PreparedStatement ps = connection.prepareStatement(CREATE);
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getFullName());
            ps.setString(3, currency.getSign());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            Integer id = null;
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            currency.setId(id);
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CurrencyModel update(CurrencyModel currency) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(UPDATE);
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getFullName());
            ps.setString(3, currency.getSign());
            ps.setInt(4, currency.getId());
            ps.executeUpdate();
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(DELETE);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
