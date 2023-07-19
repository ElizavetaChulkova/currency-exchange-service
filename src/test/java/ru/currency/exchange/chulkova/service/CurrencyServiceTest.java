package ru.currency.exchange.chulkova.service;

import org.junit.jupiter.api.*;
import ru.currency.exchange.chulkova.TestData;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.CurrencyModel;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.currency.exchange.chulkova.TestData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CurrencyServiceTest {

    private CurrencyService service;

    @BeforeEach
    void setUp() {
        service = new CurrencyService();
    }

    @Test
    @Order(1)
    void getAll() {
        List<CurrencyModel> actual = service.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(TestData.currencies);
    }

    @Test
    @Order(2)
    void getByCode() {
        CurrencyModel actual = service.getByCode(CODE);
        assertThat(actual).usingRecursiveComparison().isEqualTo(currency1);
    }

    @Test
    @Order(3)
    void getByNotExistedCode() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getByCode(INVALID_CODE));
    }

    @Test
    @Order(4)
    void getById() {
        CurrencyModel actual = service.getById(CURRENCY_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(currency1);
    }

    @Test
    @Order(5)
    void getByNotExistedId() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getById(INVALID_ID));
    }

    @Test
    @Order(6)
    void create() {
        service.create(CURRENCY_TO_CREATE);
        assertThat(CURRENCY_TO_CREATE).usingRecursiveComparison()
                .isEqualTo(service.getByCode(CURRENCY_TO_CREATE.getCode()));
    }

    @Test
    @Order(7)
    void createExisted() {
        Assertions.assertThrows(AlreadyExistsException.class, () -> service.create(currency1));
    }

    @Test
    @Order(8)
    void update() {
        CURRENCY_TO_UPDATE.setId(service.getByCode(CURRENCY_TO_CREATE.getCode()).getId());
        service.update(CURRENCY_TO_UPDATE);
        assertThat(CURRENCY_TO_UPDATE).usingRecursiveComparison()
                .isEqualTo(service.getByCode(CURRENCY_TO_UPDATE.getCode()));
    }

    @Test
    @Order(9)
    void updateWithInvalidId() {
        CURRENCY_TO_UPDATE.setId(INVALID_ID);
        Assertions.assertThrows(NotFoundException.class, () -> service.update(CURRENCY_TO_UPDATE));
    }

    @Test
    @Order(10)
    void updateWithNullId() {
        Assertions.assertThrows(NotFoundException.class, () -> service.update(CURRENCY_TO_UPDATE));
    }

    @Test
    @Order(11)
    void delete() {
        int id = service.getByCode(CURRENCY_TO_UPDATE.getCode()).getId();
        service.delete(id);
        Assertions.assertThrows(NotFoundException.class, () -> service.getById(id));
    }

    @Test
    @Order(12)
    void deleteWithInvalidId() {
        Assertions.assertThrows(NotFoundException.class, () -> service.delete(INVALID_ID));
    }
}