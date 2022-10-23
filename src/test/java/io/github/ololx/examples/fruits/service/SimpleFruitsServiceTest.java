package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Fruit;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Alexander A. Kropotin
 * project bloom-filter-implementation-example
 * created 30.09.2022 22:23
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
@Slf4j
@NoArgsConstructor
class SimpleFruitsServiceTest {

    private static final String POSTGRES_IMAGE_NAME = "postgres:11.1";

    @ClassRule
    private static final PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE_NAME)
            .withDatabaseName("fruits_db")
            .withUsername("bloom")
            .withPassword("qwerty");

    @Autowired
    SimpleFruitsService simpleFruitsService;

    @Autowired
    FilteredFruitsService filteredFruitsService;

    @BeforeAll
    static void init() {
        dbContainer.start();

        System.setProperty("DB_URL", dbContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", dbContainer.getUsername());
        System.setProperty("DB_PASSWORD", dbContainer.getPassword());
    }

    @BeforeEach
    void setUp() {
        providesFruitsNames().stream()
                .map(name -> Fruit.builder().name(String.valueOf(name)).build())
                .forEach(fruit -> {
                    filteredFruitsService.create(fruit);
                });
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
        IntStream.range(0, 50).forEach(number -> {
            providesFruitsNames().stream()
                    .map(name -> Fruit.builder().name(String.valueOf(name)).build())
                    .forEach(fruit -> {
                        final var created = service.create(fruit);
                        log.debug("Fruit {} was {} created", fruit, !created ? "not" : "");
                    });
        });
        long endTime = System.nanoTime();

        return endTime - startTime;
    }

    List<String> providesFruitsNames() {
        return List.of(
                "Apple", 
                "Banana", 
                "Apricot", 
                "Atemoya", 
                "Avocados", 
                "Blueberry", 
                "Blackcurrant", 
                "Ackee", 
                "Cranberry", 
                "Cantaloupe", 
                "Cherry", 
                "Black sapote/Chocolate pudding fruit", 
                "Dragonrfruit", 
                "Dates", 
                "Cherimoya", 
                "Buddha’s hand fruit", 
                "Finger Lime", 
                "Fig", 
                "Coconut", 
                "Cape gooseberry/Inca berry/Physalis", 
                "Grapefruit", 
                "Gooseberries", 
                "Custard apple/Sugar apple/Sweetsop", 
                "Chempedak", 
                "Hazelnut", 
                "Honeyberries", 
                "Dragon fruit", 
                "Durian", 
                "Horned Melon", 
                "Hog Plum", 
                "Egg fruit", 
                "Feijoa/Pineapple guava/Guavasteen", 
                "Indian Fig", 
                "Ice Apple", 
                "Guava", 
                "Fuyu Persimmon", 
                "Jackfruit", 
                "Jujube", 
                "Honeydew melon", 
                "Jenipapo", 
                "Kiwi", 
                "Kabosu", 
                "Kiwano", 
                "Kaffir lime/Makrut Lime", 
                "Lime", 
                "Lychee", 
                "Longan", 
                "Langsat", 
                "Mango", 
                "Mulberry", 
                "Pear", 
                "Lucuma", 
                "Muskmelon", 
                "Naranjilla", 
                "Passion fruit", 
                "Mangosteen", 
                "Nectarine", 
                "Nance", 
                "Quince", 
                "Medlar fruit", 
                "Olive", 
                "Oranges", 
                "Ramphal", 
                "Mouse melon", 
                "Papaya", 
                "Peach", 
                "Rose apple/Water apple", 
                "Noni fruit", 
                "Pomegranate", 
                "Pineapple", 
                "Rambutan", 
                "Snake fruit/Salak", 
                "Raspberries", 
                "Strawberries", 
                "Starfruit/Carambola", 
                "Soursop", 
                "Tangerine", 
                "Watermelon", 
                "Sapota", 
                "Star apple"
        );
    }
}