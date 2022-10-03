package io.github.ololx.examples.fruits.service;

import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * project bloom-filter-implementation-example
 * created 01.10.2022 18:52
 *
 * @author Alexander A. Kropotin
 */
public interface BloomFilter<T> {

    void add(T value);

    boolean contains(T value);

    public static class SimpleBloomFilter implements BloomFilter<String> {

        private final int[] bitSet;

        private final int[] seeds;

        private final BiFunction<String, Integer, Integer> hashFunction = (value, seed) -> {
            return Integer.MAX_VALUE & (seed * value.hashCode());
        };

        public SimpleBloomFilter(int maxSize, int maxProbability) {
            int m = (int) ((maxSize * (Math.log(maxProbability))) / Math.pow(Math.log(2), 2));
            this.bitSet = new int[m];

            int k = (int) ((m / maxSize) * Math.log(2));
            this.seeds = IntStream.range(0, k).toArray();
        }

        @Override
        public void add(String value) {
            for (var seed : seeds) {
                this.bitSet[this.hashFunction.apply(value, seed) % this.bitSet.length] = 0x1;
            }
        }

        @Override
        public boolean contains(String value) {
            for (var seed : seeds) {
                if (this.bitSet[this.hashFunction.apply(value, seed) % this.bitSet.length] != 0x1) {
                    return false;
                }
            }

            return true;
        }
    }
}
