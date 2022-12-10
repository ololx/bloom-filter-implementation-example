package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Food;
import io.github.ololx.examples.fruits.repository.FoodRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * project bloom-filter-implementation-example
 * created 30.09.2022 21:55
 *
 * @author Alexander A. Kropotin
 */
@RequiredArgsConstructor
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true
)
@Service("SimpleFruitsService")
public class SimpleFruitsService implements FoodService<Food> {

    FoodRepository repository;

    @Override
    public boolean create(Food entity) {
        final var storedFruit = this.repository.findFirstByName(entity.getName());

        if (storedFruit.isPresent()) {
            return false;
        }

        this.repository.save(entity);

        return true;
    }

    @Override
    public ResponseEntity<Food> findByName(String entityName) {
        final var storedFruit = this.repository.findFirstByName(entityName);
        if (storedFruit.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(storedFruit.get());
    }

    @Override
    public void deleteAll() {
        this.repository.deleteAll();
    }
}
