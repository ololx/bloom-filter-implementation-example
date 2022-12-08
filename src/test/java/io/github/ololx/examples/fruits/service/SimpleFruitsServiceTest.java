package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Fruit;
import io.github.ololx.examples.fruits.test.utils.ActionPerformance;
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

import java.io.IOException;
import java.util.ArrayList;
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
    public static final PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer(POSTGRES_IMAGE_NAME)
            .withDatabaseName("fruits_db")
            .withUsername("bloom")
            .withPassword("qwerty");


    @ClassRule
    public static ActionPerformance actionPerformance;

    @Autowired
    SimpleFruitsService simpleFruitsService;

    @Autowired
    FilteredFruitsService filteredFruitsService;

    @BeforeAll
    static void init() throws IOException {
        dbContainer.start();

        System.setProperty("DB_URL", dbContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", dbContainer.getUsername());
        System.setProperty("DB_PASSWORD", dbContainer.getPassword());

        actionPerformance = new ActionPerformance();
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
        List<ActionPerformance.Result> simpleServiceResults = new ArrayList<>();
        List<ActionPerformance.Result> filteredServiceResults = new ArrayList<>();

        for (var invocationNumber = 0; invocationNumber < 10; invocationNumber++) {
            simpleServiceResults.add(evaluateExecutionTime(this.simpleFruitsService));
            filteredServiceResults.add(evaluateExecutionTime(this.filteredFruitsService));
        }

        final String rowFormat = "| %-17s | %-10s | %-8s | %-12s |%n";
        StringBuilder record = new StringBuilder();
        record.append(String.format(
                rowFormat,
                "BLOOM - NOT BLOOM",
                "TIME MS",
                "CPU %",
                "MEMORY KB"
        ));

        for (int resultIndex = 0; resultIndex < 10; resultIndex++) {
            record.append(String.format(
                    rowFormat,
                    "BLOOM - NOT BLOOM",
                    filteredServiceResults.get(resultIndex).runningTime / 1_000_000 + " - " + simpleServiceResults.get(resultIndex).runningTime / 1_000_000,
                    filteredServiceResults.get(resultIndex).cpuLoad + " - " + simpleServiceResults.get(resultIndex).cpuLoad,
                    filteredServiceResults.get(resultIndex).memoryUsage / 1024 + " - " + simpleServiceResults.get(resultIndex).memoryUsage / 1024
            ));
        }

        log.info("\n{}", record.toString());
    }

    ActionPerformance.Result evaluateExecutionTime(FruitsService<Fruit> service) {
        return actionPerformance.evaluate(
                () -> {
                    IntStream.range(0, 50).forEach(number -> {
                        providesFruitsNames().stream()
                                .map(name -> Fruit.builder().name(String.valueOf(name)).build())
                                .forEach(fruit -> {
                                    final var created = service.create(fruit);
                                    log.debug("Fruit {} was {} created", fruit, !created ? "not" : "");
                                });
                    });
                },
                1000L //delay before evaluation in ms
        );
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