package ru.currency.exchange.chulkova.repository;

import ru.currency.exchange.chulkova.db.DatabaseConnection;
import ru.currency.exchange.chulkova.exceptions.ApplicationException;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.CurrencyModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.currency.exchange.chulkova.exceptions.ErrorMessage.*;

public class CurrencyJdbcRepository implements BaseRepository<CurrencyModel> {

    private static final String SELECT_ALL = "SELECT * FROM currency";
    private static final String SELECT_BY_CODE = "SELECT * FROM currency WHERE code=?";
    private static final String SELECT_BY_ID = "SELECT * FROM currency WHERE id = ?";
    private static final String CREATE = "INSERT INTO currency (code, full_name, sign) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE currency SET code = ?, full_name = ?, sign = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM currency WHERE id = ?";

    private static Connection connection = DatabaseConnection.getConnection();

    public List<CurrencyModel> getAll() {
        List<CurrencyModel> currencies = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                CurrencyModel currency = of(resultSet);
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            throw new ApplicationException(ERROR);
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
            throw new ApplicationException(ERROR);
        }
        return currency;
    }

    public Optional<CurrencyModel> getByCode(String code) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CODE)) {
            ps.setString(1, code);
            ResultSet resultSet = ps.executeQuery();
            CurrencyModel currency = new CurrencyModel();
            while (resultSet.next()) {
                currency = of(resultSet);
            }
            return Optional.of(currency);
        } catch (SQLException e) {
            throw new NotFoundException(CURRENCY_NOT_FOUND);
        }
    }

    @Override
    public Optional<CurrencyModel> getById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            CurrencyModel currency = new CurrencyModel();
            while (resultSet.next()) {
                currency = of(resultSet);
            }
            return Optional.of(currency);
        } catch (SQLException e) {
            throw new NotFoundException(CURRENCY_NOT_FOUND);
        }
    }

    public CurrencyModel create(CurrencyModel currency) {
        try (PreparedStatement ps = connection.prepareStatement(CREATE, new String[] {"id"})) {
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
            throw new NotFoundException(ALREADY_EXISTS);
        }
    }

    @Override
    public CurrencyModel update(CurrencyModel currency) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getFullName());
            ps.setString(3, currency.getSign());
            ps.setInt(4, currency.getId());
            ps.executeUpdate();
            return currency;
        } catch (SQLException e) {
            throw new NotFoundException(CURRENCY_NOT_FOUND);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new NotFoundException(CURRENCY_NOT_FOUND);
        }
    }
}
