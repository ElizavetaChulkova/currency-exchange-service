package ru.currency.exchange.chulkova.repository;

import org.junit.jupiter.api.*;
import ru.currency.exchange.chulkova.model.Currency;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        List<Currency> actual = repository.getAll();
        Assertions.assertTrue(actual.containsAll(currencies));
//        assertThat(actual).hasSameElementsAs(TestData.currencies);
    }

    @Test
    @Order(2)
    void getByCode() {
        Currency actual = repository.getByCode(CODE).get();
        assertThat(actual).usingRecursiveComparison().isEqualTo(currency1);
    }

    @Test
    @Order(3)
    void getById() {
        Currency actual = repository.getById(CURRENCY_ID).orElse(null);
        assertThat(actual).usingRecursiveComparison().isEqualTo(currency1);
    }

    @Test
    @Order(4)
    void create() {
        Currency currency = repository.create(CURRENCY_TO_CREATE);
        assertThat(currency).usingRecursiveComparison()
                .isEqualTo(repository.getByCode(CURRENCY_TO_CREATE.getCode()).get());
    }

    @Test
    @Order(5)
    void update() {
        CURRENCY_TO_UPDATE.setId(repository.getByCode(CURRENCY_TO_CREATE.getCode()).get().getId());
        repository.update(CURRENCY_TO_UPDATE);
        assertThat(CURRENCY_TO_UPDATE).usingRecursiveComparison()
                .isEqualTo(repository.getByCode(CURRENCY_TO_UPDATE.getCode()).get());
    }

    @Test
    @Order(6)
    void delete() {
        repository.delete(repository.getByCode(CURRENCY_TO_UPDATE.getCode()).get().getId());
        assertTrue(repository.getByCode(CURRENCY_TO_UPDATE.getCode()).isEmpty());
    }
}