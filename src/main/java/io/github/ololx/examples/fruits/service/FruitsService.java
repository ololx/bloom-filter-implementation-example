package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Fruit;
import org.springframework.stereotype.Service;

/**
 * project bloom-filter-implementation-example
 * created 30.09.2022 21:53
 *
 * @author Alexander A. Kropotin
 */
@Service("FruitsService")
public interface FruitsService<T extends Fruit> {

    boolean create(T entity);
}
