package ru.currency.exchange.chulkova.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    Optional<T> getById(int id);
    List<T> getAll();
    T create(T entity);
    T update(T entity);
    void delete(int id);
}
