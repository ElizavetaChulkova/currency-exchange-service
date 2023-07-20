package ru.currency.exchange.chulkova.repository;

import lombok.extern.slf4j.Slf4j;
import ru.currency.exchange.chulkova.model.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.currency.exchange.chulkova.db.DatabaseConnection.DRIVER;
import static ru.currency.exchange.chulkova.db.DatabaseConnection.URL;

@Slf4j
public class ExchangeRateJdbcRepository implements BaseRepository<ExchangeRate> {
    private static final String SELECT_ALL = "SELECT * FROM exchangeRate";
    private static final String SELECT_BY_CODE_PAIR = "SELECT * FROM exchangeRate e " +
            "WHERE base_currency_id = ? AND target_Currency_id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM exchangeRate WHERE id = ?";
    private static final String CREATE = "INSERT INTO exchangeRate (base_currency_id, target_currency_id, rate) " +
            "VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE exchangeRate SET base_currency_id = ?, target_currency_id = ?," +
            "rate = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM exchangeRate WHERE id = ?";
    private static final CurrencyJdbcRepository currencyRepo = new CurrencyJdbcRepository();

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExchangeRate of(ResultSet rs) {
        ExchangeRate rate = new ExchangeRate();
        try {
            rate.setId(rs.getInt("id"));
            rate.setBase(currencyRepo.getById(rs.getInt("base_currency_id")).orElseThrow());
            rate.setTarget(currencyRepo.getById(rs.getInt("target_currency_id")).orElseThrow());
            rate.setRate(rs.getDouble("rate"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rate;
    }

    @Override
    public List<ExchangeRate> getAll() {
        List<ExchangeRate> rates = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                ExchangeRate rate = of(resultSet);
                rates.add(rate);
            }
            return rates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExchangeRate> getByCodePair(String baseCurrency, String targetCurrency) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_CODE_PAIR);
            ExchangeRate rate = new ExchangeRate();
            ps.setInt(1, currencyRepo.getByCode(baseCurrency).get().getId());
            ps.setInt(2, currencyRepo.getByCode(targetCurrency).get().getId());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                rate = of(resultSet);
                System.out.println(rate);
            }
            return Optional.of(rate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ExchangeRate> getById(int id) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            ExchangeRate rate = new ExchangeRate();
            while (resultSet.next()) {
                rate = of(resultSet);
            }
            return Optional.of(rate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRate create(ExchangeRate rate) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(CREATE);
            ps.setInt(1, rate.getBase().getId());
            ps.setInt(2, rate.getTarget().getId());
            ps.setDouble(3, rate.getRate());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            Integer id = null;
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            rate.setId(id);
            return rate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRate update(ExchangeRate rate) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement ps = connection.prepareStatement(UPDATE);
            ps.setInt(1, rate.getBase().getId());
            ps.setInt(2, rate.getTarget().getId());
            ps.setDouble(3, rate.getRate());
            ps.setInt(4, rate.getId());
            ps.executeUpdate();
            return rate;
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
