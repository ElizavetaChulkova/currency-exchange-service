package ru.currency.exchange.chulkova.service;

import org.junit.jupiter.api.*;
import ru.currency.exchange.chulkova.exceptions.AlreadyExistsException;
import ru.currency.exchange.chulkova.exceptions.NotFoundException;
import ru.currency.exchange.chulkova.model.ExchangeRate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.currency.exchange.chulkova.TestData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExchangeRateServiceTest {

    private ExchangeRateService service;

    @BeforeEach
    void setUp() {
        service = new ExchangeRateService();
    }

    @Test
    @Order(1)
    void getAll() {
        List<ExchangeRate> actual = service.getAll();
        Assertions.assertTrue(actual.containsAll(rates));
//        assertThat(actual).containsExactlyInAnyOrderElementsOf(TestData.rates);
    }

    @Test
    @Order(2)
    void getByCodePair() {
        ExchangeRate actual = service.getByCodePair(BASE, TARGET);
        assertThat(actual).usingRecursiveComparison().isEqualTo(rate1);
    }

    @Test
    @Order(3)
    void getByNotExistedPair() {
        Assertions.assertThrows(NotFoundException.class,
                () -> service.getByCodePair(BASE, INVALID_CODE));
    }

    @Test
    @Order(4)
    void getById() {
        ExchangeRate actual = service.getById(RATE_ID).orElse(null);
        assertThat(actual).usingRecursiveComparison().isEqualTo(rate1);
    }

    @Test
    @Order(5)
    void getByNotExistedId() {
        Assertions.assertThrows(NotFoundException.class, () -> service.getById(INVALID_ID));
    }

    @Test
    @Order(6)
    void create() {
        ExchangeRate rate = service.create(RATE_TO_CREATE);
        assertThat(rate).usingRecursiveComparison()
                .isEqualTo(service.getByCodePair(RATE_TO_CREATE.getBase().getCode(),
                        RATE_TO_CREATE.getTarget().getCode()));
    }

    @Test
    @Order(8)
    void createExisted() {
        Assertions.assertThrows(AlreadyExistsException.class, () -> service.create(rate1));
    }

    @Test
    @Order(7)
    void update() {
        RATE_TO_UPDATE.setId(service.getByCodePair(RATE_TO_CREATE.getBase().getCode(),
                RATE_TO_CREATE.getTarget().getCode()).getId());
        ExchangeRate actual = service.update(RATE_TO_UPDATE);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(service.getByCodePair(RATE_TO_UPDATE.getBase().getCode(),
                        RATE_TO_UPDATE.getTarget().getCode()));
    }

    @Test
    @Order(9)
    void updateWithInvalidId() {
        RATE_TO_UPDATE.setId(INVALID_ID);
        Assertions.assertThrows(NotFoundException.class, () -> service.update(RATE_TO_UPDATE));
    }

    @Test
    @Order(10)
    void updateWithNullId() {
        Assertions.assertThrows(NotFoundException.class, () -> service.update(RATE_TO_UPDATE));
    }

    @Test
    @Order(11)
    void delete() {
        int id = service.getByCodePair(RATE_TO_UPDATE.getBase().getCode(),
                RATE_TO_UPDATE.getTarget().getCode()).getId();
        service.delete(id);
        Assertions.assertThrows(NotFoundException.class, () -> service.getById(id));
    }

    @Test
    @Order(12)
    void deleteWithInvalidId() {
        Assertions.assertThrows(NotFoundException.class, () -> service.delete(INVALID_ID));
    }
}