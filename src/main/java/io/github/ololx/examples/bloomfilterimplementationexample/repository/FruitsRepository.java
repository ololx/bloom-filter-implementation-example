package io.github.ololx.examples.bloomfilterimplementationexample.repository;

import io.github.ololx.examples.bloomfilterimplementationexample.entity.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * project bloom-filter-implementation-example
 * created 30.09.2022 13:47
 *
 * @author Alexander A. Kropotin
 */
@Repository("FruitsJpaRepository")
public interface FruitsRepository extends JpaRepository<Fruit, Integer> {


}
