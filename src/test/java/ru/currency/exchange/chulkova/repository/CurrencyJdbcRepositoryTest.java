package ru.currency.exchange.chulkova.repository;

import org.junit.jupiter.api.*;
import ru.currency.exchange.chulkova.model.CurrencyModel;
import ru.currency.exchange.chulkova.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.currency.exchange.chulkova.TestData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CurrencyJdbcRepositoryTest {
    private CurrencyJdbcRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CurrencyJdbcRepository();
    }

    @Test
    @Order(1)
    void getAll() {
        List<CurrencyModel> actual = repository.getAll();
        assertThat(actual).hasSameElementsAs(TestData.currencies);
    }

    @Test
    @Order(2)
    void getByCode() {
        CurrencyModel actual = repository.getByCode(CODE).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(currency1);
    }

    @Test
    @Order(3)
    void getById() {
        CurrencyModel actual = repository.getById(CURRENCY_ID).orElse(null);
        assertThat(actual).usingRecursiveComparison().isEqualTo(currency1);
    }

    @Test
    @Order(4)
    void create() {
        repository.create(CURRENCY_TO_CREATE);
        assertThat(CURRENCY_TO_CREATE).usingRecursiveComparison()
                .isEqualTo(repository.getByCode(CURRENCY_TO_CREATE.getCode()));
    }

    @Test
    @Order(5)
    void update() {
        CURRENCY_TO_UPDATE.setId(repository.getByCode(CURRENCY_TO_CREATE.getCode()).get().getId());
        repository.update(CURRENCY_TO_UPDATE);
        assertThat(CURRENCY_TO_UPDATE).usingRecursiveComparison()
                .isEqualTo(repository.getByCode(CURRENCY_TO_UPDATE.getCode()));
    }

    @Test
    @Order(6)
    void delete() {
        repository.delete(repository.getByCode(CURRENCY_TO_UPDATE.getCode()).get().getId());
        assertNull(repository.getByCode(CURRENCY_TO_UPDATE.getCode()).get().getId());
    }
}