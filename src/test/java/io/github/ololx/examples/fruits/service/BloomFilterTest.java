package io.github.ololx.examples.fruits.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * @author Alexander A. Kropotin
 * project bloom-filter-implementation-example
 * created 17.10.2022 11:30
 */
class BloomFilterTest {

    @ParameterizedTest
    @MethodSource("providesFruitsNames")
    void add_whenValueWasAdded_thenFilterContainsValue(String fruitName) {
        BloomFilter<String> bloomFilter = new BloomFilter.SimpleBloomFilter(10, 100);

        Assertions.assertFalse(bloomFilter.contains(fruitName));

        bloomFilter.add(fruitName);

        Assertions.assertTrue(bloomFilter.contains(fruitName));
    }

    @ParameterizedTest
    @MethodSource("providesFruitsNames")
    void contains_whenFilterMayContainsValue_thenReturnTrue(String fruitName,
                                                            String fruitNameForFilter,
                                                            boolean expected) {
        BloomFilter<String> bloomFilter = new BloomFilter.SimpleBloomFilter(10, 100);
        bloomFilter.add(fruitName);

        Assertions.assertEquals(expected, bloomFilter.contains(fruitNameForFilter));
    }

    static Stream<Arguments> providesFruitsNames() {
        return Stream.of(
                Arguments.of("Apple", "Apple", true),
                Arguments.of("Banana", "Apple", false),
                Arguments.of("Apricot", "Apple", false),
                Arguments.of("Atemoya", "Atemoya", true)
        );
    }
}