package io.github.ololx.examples.fruits.repository;

import io.github.ololx.examples.fruits.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * project bloom-filter-implementation-example
 * created 30.09.2022 13:47
 *
 * @author Alexander A. Kropotin
 */
@Repository("FruitsRepository")
public interface FoodRepository extends JpaRepository<Food, Integer> {

    Optional<Food> findFirstByName(String name);
}
