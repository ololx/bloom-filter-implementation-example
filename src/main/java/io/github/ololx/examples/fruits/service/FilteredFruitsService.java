package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Food;
import io.github.ololx.examples.fruits.repository.FoodRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    public void deleteAll() {
        this.repository.deleteAll();
        this.filter.clear();
    }
}
