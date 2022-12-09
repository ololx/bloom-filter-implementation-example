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

    int UNIT = 0b1;

    int NIL = 0b0;

    void add(T value);

    boolean contains(T value);

    void clear();

    public static class SimpleBloomFilter implements BloomFilter<String> {

        private final int[] bitSet;

        private final int[] seeds;

        private final BiFunction<String, Integer, Integer> hashFunction = (value, seed) -> {
            return Integer.MAX_VALUE & (seed * value.hashCode());
        };

        public SimpleBloomFilter(int maxSize, int maxProbability) {
            int m = (int) Math.ceil(((maxSize * (Math.log(maxProbability))) / Math.pow(Math.log(2), 2)));
            this.bitSet = new int[m];

            int k = (int) Math.ceil((m / maxSize) * Math.log(2));
            this.seeds = IntStream.range(0, k).toArray();
        }

        @Override
        public void add(String value) {
            for (var seed : seeds) {
                this.bitSet[this.hashFunction.apply(value, seed) % this.bitSet.length] = UNIT;
            }
        }

        @Override
        public boolean contains(String value) {
            for (var seed : seeds) {
                if (this.bitSet[this.hashFunction.apply(value, seed) % this.bitSet.length] != UNIT) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public void clear() {
            for (var bit : this.bitSet) {
                this.bitSet[bit] = NIL;
            }
        }
    }
}
