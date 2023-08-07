package ru.currency.exchange.chulkova.repository;

import ru.currency.exchange.chulkova.db.DataSource;
import ru.currency.exchange.chulkova.model.ExchangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private static final Connection connection;

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
            throw new RuntimeException(e);
        }
    }

    public static ExchangeRate of(ResultSet rs) {
        ExchangeRate rate = new ExchangeRate();
        try {
            rate.setId(rs.getInt("id"));
            rate.setBaseCode(currencyRepo.getById(rs.getInt("base_currency_id")).get().getCode());
            rate.setTargetCode(currencyRepo.getById(rs.getInt("target_currency_id")).get().getCode());
            rate.setRate(rs.getDouble("rate"));
            return rate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExchangeRate> getByCodePair(String baseCurrency, String targetCurrency) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CODE_PAIR)) {
            ps.setInt(1, currencyRepo.getByCode(baseCurrency).get().getId());
            ps.setInt(2, currencyRepo.getByCode(targetCurrency).get().getId());
            return getExchangeRate(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<ExchangeRate> getExchangeRate(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return Optional.empty();
        } else {
            return Optional.of(of(resultSet));
        }
    }

    @Override
    public Optional<ExchangeRate> getById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            return getExchangeRate(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRate create(ExchangeRate rate) {
        try (PreparedStatement ps = connection.prepareStatement(CREATE, new String[]{"id"})) {
            ps.setInt(1, currencyRepo.getByCode(rate.getBaseCode()).get().getId());
            ps.setInt(2, currencyRepo.getByCode(rate.getTargetCode()).get().getId());
            ps.setDouble(3, rate.getRate());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                rate.setId(generatedKeys.getInt(1));
            }
            return rate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRate update(ExchangeRate rate) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setInt(1, currencyRepo.getByCode(rate.getBaseCode()).get().getId());
            ps.setInt(2, currencyRepo.getByCode(rate.getTargetCode()).get().getId());
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
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
