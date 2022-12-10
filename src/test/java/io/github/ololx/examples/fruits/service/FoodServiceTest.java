package io.github.ololx.examples.fruits.service;

import io.github.ololx.examples.fruits.entity.Food;
import io.github.ololx.examples.fruits.test.utils.ActionPerformance;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
class FoodServiceTest {

    private static final String POSTGRES_IMAGE_NAME = "postgres:11.1";

    private static final long TIMES = 20;

    @ClassRule
    public static final PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE_NAME)
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
    static void initAll() throws IOException {
        dbContainer.start();

        System.setProperty("DB_URL", dbContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", dbContainer.getUsername());
        System.setProperty("DB_PASSWORD", dbContainer.getPassword());

        actionPerformance = new ActionPerformance();
    }

    @Test
    void create_whenCreateEntitiesWithDuplicates_thenServiceWithBloomMustBeFaster() throws FileNotFoundException {
        final var food = providesFoodEntities();
        List<ActionPerformance.Result> simpleServiceResults = new ArrayList<>();
        List<ActionPerformance.Result> filteredServiceResults = new ArrayList<>();

        for (var invocationNumber = 0; invocationNumber < TIMES; invocationNumber++) {
            this.filteredFruitsService.deleteAll();
            filteredServiceResults.add(evaluateCreateExecutionTime(this.filteredFruitsService, food));

            this.simpleFruitsService.deleteAll();
            simpleServiceResults.add(evaluateCreateExecutionTime(this.simpleFruitsService, food));
        }

        writeOutResult(simpleServiceResults, filteredServiceResults);
    }

    @Test
    void findByName_whenNoOneEntityExistsWithDefinedName_thenReturnNotFoundStatus() throws FileNotFoundException {
        final var food = providesFoodEntities();
        List<ActionPerformance.Result> simpleServiceResults = new ArrayList<>();
        List<ActionPerformance.Result> filteredServiceResults = new ArrayList<>();

        for (var invocationNumber = 0; invocationNumber < TIMES; invocationNumber++) {
            this.filteredFruitsService.deleteAll();
            filteredServiceResults.add(evaluateFindExecutionTime(this.filteredFruitsService, food));

            this.simpleFruitsService.deleteAll();
            simpleServiceResults.add(evaluateFindExecutionTime(this.simpleFruitsService, food));
        }

        writeOutResult(simpleServiceResults, filteredServiceResults);
    }

    ActionPerformance.Result evaluateCreateExecutionTime(FoodService<Food> service, Collection<Food> food) {
        return actionPerformance.evaluate(
                () -> {
                    for (var eachFoodEntity : food) {
                        final var created = service.create(eachFoodEntity);
                    }
                }
        );
    }

    ActionPerformance.Result evaluateFindExecutionTime(FoodService<Food> service, Collection<Food> food) {
        return actionPerformance.evaluate(
                () -> {
                    for (var eachFoodEntity : food) {
                        final var created = service.findByName(eachFoodEntity.getName());
                    }
                }
        );
    }

    void writeOutResult(List<ActionPerformance.Result> simpleServiceResults, List<ActionPerformance.Result> filteredServiceResults) {
        final String rowFormat = "| %-2s | %-16s | %-16s | %-14s | %-22s |%n";
        StringBuilder record = new StringBuilder();
        record.append(String.format(
                rowFormat,
                "#",
                "TIME (MS) B|NB ",
                "TIME (MS) NB-B ",
                "CPU (%) B|NB",
                "MEMORY (KB) B|NB"
        ));

        for (int resultIndex = 0; resultIndex < TIMES; resultIndex++) {
            record.append(String.format(
                    rowFormat,
                    resultIndex + 1,
                    filteredServiceResults.get(resultIndex).runningTime + " | " + simpleServiceResults.get(resultIndex).runningTime,
                    simpleServiceResults.get(resultIndex).runningTime - filteredServiceResults.get(resultIndex).runningTime,
                    filteredServiceResults.get(resultIndex).cpuLoad + " | " + simpleServiceResults.get(resultIndex).cpuLoad,
                    filteredServiceResults.get(resultIndex).memoryUsage / 1024 + " | " + simpleServiceResults.get(resultIndex).memoryUsage / 1024
            ));
        }

        log.info("\nB - service with Bloom filter\nNB service without Bloom filer\n{}", record.toString());
    }

    Set<Food> providesFoodEntities() throws FileNotFoundException {
        final var foodCsvStream =  getClass()
                .getClassLoader()
                .getResourceAsStream("foodNames.csv");
        final var reader = new BufferedReader(new InputStreamReader(foodCsvStream));

        return reader.lines()
                .parallel()
                .map(name -> Food.builder().name(String.valueOf(name)).build())
                .collect(Collectors.toSet());
    }
}