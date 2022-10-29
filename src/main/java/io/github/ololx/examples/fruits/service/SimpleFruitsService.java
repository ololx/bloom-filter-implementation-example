package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Fruit;
import io.github.ololx.examples.fruits.repository.FruitsRepository;
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
@Service("SimpleFruitsService")
public class SimpleFruitsService implements FruitsService<Fruit> {

    FruitsRepository repository;

    @Override
    public boolean create(Fruit entity) {
        final var storedFruit = this.repository.findFirstByName(entity.getName());

        if (storedFruit.isPresent()) {
            return false;
        }

        this.repository.save(entity);

        return true;
    }
}
