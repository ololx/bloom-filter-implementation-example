package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Fruit;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

/**
 * @author Alexander A. Kropotin
 * @project bloom-filter-implementation-example
 * @created 30.09.2022 22:23
 * <p>
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@Slf4j
@NoArgsConstructor
class SimpleFruitsServiceTest {

    @Autowired
    SimpleFruitsService simpleFruitsService;

    @Autowired
    FilteredFruitsService filteredFruitsService;

    @BeforeEach
    void setUp() {
        IntStream.range(0, 500)
                .mapToObj(number -> {
                    return Fruit.builder()
                            .id(number)
                            .name(String.valueOf(number))
                            .build();
                })
                .forEach(fruit -> filteredFruitsService.create(fruit));
    }

    @Test
    void create() {
        long simpleTime = 0;
        long filteredTime = 0;

        for (var invocationNumber = 0; invocationNumber < 10; invocationNumber++) {
            simpleTime += evaluateExecutionTime(this.simpleFruitsService);
            filteredTime += evaluateExecutionTime(this.filteredFruitsService);
        }

        log.info(
                "\nSimpleService AVG time = {}\nFilteredService AVG time = {}\nFilteredService faster in times = {}",
                simpleTime / 10,
                filteredTime / 10,
                (simpleTime / 10) / (filteredTime / 10)
        );
    }

    long evaluateExecutionTime(FruitsService<Fruit> service) {
        long startTime = System.nanoTime();
        IntStream.range(0, 500)
                .mapToObj(number -> {
                    return Fruit.builder()
                            .id(number)
                            .name(String.valueOf(number))
                            .build();
                })
                .forEach(fruit -> {
                    final var created = service.create(fruit);
                    log.debug("Fruit {} was {} created", fruit, !created ? "not" : "");
                });
        long endTime = System.nanoTime();

        return endTime - startTime;
    }
}