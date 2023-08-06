package ru.currency.exchange.chulkova.repository;

import ru.currency.exchange.chulkova.db.DataSource;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.ExchangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.*;

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

    private static Connection connection;

    static {
        try {
            connection = DataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ExchangeRate> getAll() {
        List<ExchangeRate> rates = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                ExchangeRate rate = of(resultSet);
                rates.add(rate);
            }
            return rates;
        } catch (SQLException e) {
            throw new ApplicationException(ERROR);
        }
    }

    public static ExchangeRate of(ResultSet rs) {
        ExchangeRate rate = new ExchangeRate();
        try {
            rate.setId(rs.getInt("id"));
            rate.setBase(rs.getInt("base_currency_id"));
            rate.setTarget(rs.getInt("target_currency_id"));
            rate.setRate(rs.getDouble("rate"));
        } catch (SQLException e) {
            throw new ApplicationException(ERROR);
        }
        return rate;
    }

    public Optional<ExchangeRate> getByCodePair(String baseCurrency, String targetCurrency) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CODE_PAIR)) {
            ExchangeRate rate = new ExchangeRate();
            ps.setInt(1, Integer.parseInt(baseCurrency));
            ps.setInt(2, Integer.parseInt(targetCurrency));
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                rate = of(resultSet);
                System.out.println(rate);
            }
            return Optional.of(rate);
        } catch (SQLException e) {
            throw new NotFoundException(PAIR_EXCHANGE_RATE_NOT_FOUND);
        }
    }

    @Override
    public Optional<ExchangeRate> getById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            ExchangeRate rate = new ExchangeRate();
            while (resultSet.next()) {
                rate = of(resultSet);
            }
            return Optional.of(rate);
        } catch (SQLException e) {
            throw new NotFoundException(PAIR_EXCHANGE_RATE_NOT_FOUND);
        }
    }

    @Override
    public ExchangeRate create(ExchangeRate rate) {
        try (PreparedStatement ps = connection.prepareStatement(CREATE, new String[]{"id"})) {
            ps.setInt(1, rate.getBase());
            ps.setInt(2, rate.getTarget());
            ps.setDouble(3, rate.getRate());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                rate.setId(generatedKeys.getInt(1));
            }
            return rate;
        } catch (SQLException e) {
            throw new AlreadyExistsException(PAIR_ALREADY_EXISTS);
        }
    }

    @Override
    public ExchangeRate update(ExchangeRate rate) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setInt(1, rate.getBase());
            ps.setInt(2, rate.getTarget());
            ps.setDouble(3, rate.getRate());
            ps.setInt(4, rate.getId());
            ps.executeUpdate();
            return rate;
        } catch (SQLException e) {
            throw new NotFoundException(PAIR_EXCHANGE_RATE_NOT_FOUND);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new NotFoundException(PAIR_EXCHANGE_RATE_NOT_FOUND);
        }
    }
}
