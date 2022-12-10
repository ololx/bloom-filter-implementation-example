package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Food;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * project bloom-filter-implementation-example
 * created 30.09.2022 21:53
 *
 * @author Alexander A. Kropotin
 */
@Service("FruitsService")
public interface FoodService<T extends Food> {

    boolean create(T entity);

    ResponseEntity<Food> findByName(String entityName);

    void deleteAll();
}
