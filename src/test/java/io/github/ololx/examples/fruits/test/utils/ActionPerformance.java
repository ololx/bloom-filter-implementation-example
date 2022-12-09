package io.github.ololx.examples.fruits.test.utils;

import lombok.extern.slf4j.Slf4j;

import com.sun.management.OperatingSystemMXBean;
import com.sun.management.ThreadMXBean;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.Clock;
import java.time.Duration;

/**
 * project bloom-filter-implementation-example
 * created 29.10.2022 23:15
 *
 * @author Alexander A. Kropotin
 */
@Slf4j
public final class ActionPerformance {

    private final OperatingSystemMXBean operatingSystemMXBean;

    private final ThreadMXBean threadMXBean;

    public ActionPerformance() throws IOException {
        MBeanServerConnection mBeanServerConnection = ManagementFactory.getPlatformMBeanServer();
        this.operatingSystemMXBean = ManagementFactory.newPlatformMXBeanProxy(
                mBeanServerConnection, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class
        );
        this.threadMXBean = ManagementFactory.newPlatformMXBeanProxy(
                mBeanServerConnection, ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class
        );
    }

    public Result evaluate(Action action) {
        long currentThreadId = Thread.currentThread().getId();

        long cpuBefore = operatingSystemMXBean.getProcessCpuTime();
        long freeMemBefore = threadMXBean.getThreadAllocatedBytes(currentThreadId);
        var nanoBefore = Clock.systemDefaultZone().instant();//System.nanoTime();

        this.execute(action);

        var nanoAfter = Clock.systemDefaultZone().instant();//System.nanoTime();
        long cpuAfter = operatingSystemMXBean.getProcessCpuTime();
        long freeMemAfter = threadMXBean.getThreadAllocatedBytes(currentThreadId);

        var duration = Duration.between(nanoBefore, nanoAfter);

        return new Result(
                nanoAfter.compareTo(nanoBefore) > 0
                        ? ((cpuAfter - cpuBefore) * 100L) / (duration.toNanos())
                        : Duration.ZERO.getNano(),
                duration.toMillis(),
                freeMemAfter - freeMemBefore
        );
    }

    public Result evaluate(Action action, long delayInMillis) {
        this.await(delayInMillis);

        return this.evaluate(action);
    }

    private boolean execute(Action action) {
        //log.info("Run the function");

        try {
            action.execute();
        } catch (Exception e) {
            log.warn("Catch an exception when running the method:\n" + e.getMessage());

            return false;
        }

        return true;
    }

    private void await(long millis) {
        //log.info("Wait " + millis + " ms before running");

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn("Catch an exception when calling the sleep method:\n" + e.getMessage());

        }

    }

    public static interface Action {

        void execute();
    }

    public static class Result {

        public final long runningTime;

        public final long cpuLoad;

        public final long memoryUsage;

        private Result(long cpuLoad, long runningTime, long memoryUsage) {
            this.runningTime = runningTime;
            this.cpuLoad = cpuLoad;
            this.memoryUsage = memoryUsage;
        }
    }
}
