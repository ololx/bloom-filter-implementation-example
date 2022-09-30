package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Fruit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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
    SimpleFruitsService service;

    @BeforeEach
    void setUp() {
        IntStream.range(0, 500)
                .mapToObj(number -> {
                    return Fruit.builder()
                            .id(number)
                            .name(String.valueOf(number))
                            .build();
                })
                .forEach(fruit -> service.create(fruit));
    }

    @Test
    void create() {
        final var startTime = System.nanoTime();
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
        final var endTime = System.nanoTime();
        log.warn("Spend {} nanosec", endTime - startTime);
    }
}