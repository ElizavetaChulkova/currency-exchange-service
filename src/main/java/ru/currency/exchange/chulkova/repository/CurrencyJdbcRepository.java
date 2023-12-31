package ru.currency.exchange.chulkova.repository;

import ru.currency.exchange.chulkova.db.DataSource;
import ru.currency.exchange.chulkova.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyJdbcRepository implements BaseRepository<Currency> {

    private static final String SELECT_ALL = "SELECT * FROM currency";
    private static final String SELECT_BY_CODE = "SELECT * FROM currency WHERE code=?";
    private static final String SELECT_BY_ID = "SELECT * FROM currency WHERE id = ?";
    private static final String CREATE = "INSERT INTO currency (code, full_name, sign) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE currency SET code = ?, full_name = ?, sign = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM currency WHERE id = ?";

    private static final Connection connection;

    static {
        try {
            connection = DataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Currency currency = of(resultSet);
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Currency of(ResultSet rs) {
        Currency currency = new Currency();
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

    public static Optional<Currency> getCurrency(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return Optional.empty();
        } else {
            return Optional.of(of(resultSet));
        }
    }

    public Optional<Currency> getByCode(String code) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CODE)) {
            ps.setString(1, code);
            return getCurrency(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Currency> getById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            return getCurrency(ps.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency create(Currency currency) {
        try (PreparedStatement ps = connection.prepareStatement(CREATE, new String[]{"id"})) {
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getFullName());
            ps.setString(3, currency.getSign());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                currency.setId(generatedKeys.getInt(1));
            }
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Currency update(Currency currency) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
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
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
