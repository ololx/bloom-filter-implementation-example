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
@Service("FilteredFruitsService")
public class FilteredFruitsService implements FoodService<Food> {

    FoodRepository repository;

    BloomFilter<String> filter = new BloomFilter.SimpleBloomFilter(3000, 100);

    @Override
    public boolean create(Food entity) {
        if (filter.contains(entity.getName())) {
            final var storedFruit = this.repository.findFirstByName(entity.getName());

            if (storedFruit.isPresent()) {
                return false;
            }
        }

        this.repository.save(entity);
        filter.add(entity.getName());

        return true;
    }

    @Override
    public ResponseEntity<Food> findByName(String entityName) {
        if (!filter.contains(entityName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final var storedFruit = this.repository.findFirstByName(entityName);
        if (storedFruit.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(storedFruit.get());
    }

    @Override
    public void deleteAll() {
        this.repository.deleteAll();
        this.filter.clear();
    }
}
