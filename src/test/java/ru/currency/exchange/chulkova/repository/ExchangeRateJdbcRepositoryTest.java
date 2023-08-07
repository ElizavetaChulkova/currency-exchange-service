package ru.currency.exchange.chulkova.repository;

import org.junit.jupiter.api.*;
import ru.currency.exchange.chulkova.model.ExchangeRate;
import ru.currency.exchange.chulkova.service.ExchangeRateService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.currency.exchange.chulkova.TestData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExchangeRateJdbcRepositoryTest {

    private ExchangeRateJdbcRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ExchangeRateJdbcRepository();
    }

    @Test
    @Order(1)
    void getAll() {
        List<ExchangeRate> actual = repository.getAll();
        Assertions.assertTrue(actual.containsAll(rates));
//        assertThat(actual).hasSameElementsAs(TestData.rates);
    }

    @Test
    @Order(2)
    void getByCodePair() {
        ExchangeRate actual = repository.getByCodePair(BASE, TARGET).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(rate1);
    }

    @Test
    @Order(3)
    void getById() {
        ExchangeRate actual = repository.getById(RATE_ID).orElse(null);
        assertThat(actual).usingRecursiveComparison().isEqualTo(rate1);
    }

    @Test
    @Order(4)
    void create() {
        ExchangeRate rate = repository.create(RATE_TO_CREATE);
        assertThat(rate).usingRecursiveComparison()
                .isEqualTo(repository.getByCodePair(ExchangeRateService.getTo(RATE_TO_CREATE).getBase().getCode(),
                        ExchangeRateService.getTo(RATE_TO_CREATE).getTarget().getCode()).get());
    }

    @Test
    @Order(5)
    void update() {
        RATE_TO_UPDATE.setId(repository.getByCodePair(ExchangeRateService.getTo(RATE_TO_CREATE).getBase().getCode(),
                ExchangeRateService.getTo(RATE_TO_CREATE).getTarget().getCode()).get().getId());
        repository.update(RATE_TO_UPDATE);
        assertThat(RATE_TO_UPDATE).usingRecursiveComparison()
                .isEqualTo(repository.getById(RATE_TO_UPDATE.getId()).get());
    }

    @Test
    @Order(6)
    void delete() {
        int id = repository.getByCodePair(ExchangeRateService.getTo(RATE_TO_UPDATE).getBase().getCode(),
                ExchangeRateService.getTo(RATE_TO_UPDATE).getTarget().getCode()).get().getId();
        repository.delete(id);
        assertTrue(repository.getById(id).isEmpty());
    }
}