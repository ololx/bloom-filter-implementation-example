package io.github.ololx.examples.fruits.test.utils;

import lombok.extern.slf4j.Slf4j;

import com.sun.management.OperatingSystemMXBean;
import com.sun.management.ThreadMXBean;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;

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

    public Result evaluate(Runnable function) {
        long currentThreadId = Thread.currentThread().getId();

        long nanoBefore = System.nanoTime();
        long cpuBefore = operatingSystemMXBean.getProcessCpuTime();
        long freeMemBefore = threadMXBean.getThreadAllocatedBytes(currentThreadId);

        this.run(function);

        long cpuAfter = operatingSystemMXBean.getProcessCpuTime();
        long nanoAfter = System.nanoTime();
        long freeMemAfter = threadMXBean.getThreadAllocatedBytes(currentThreadId);

        return new Result(
                nanoAfter > nanoBefore ? ((cpuAfter - cpuBefore) * 100L) / (nanoAfter - nanoBefore) : 0,
                nanoAfter - nanoBefore,
                freeMemAfter - freeMemBefore
        );
    }

    public Result evaluate(Runnable function, long delayInMillis) {
        this.await(delayInMillis);

        return this.evaluate(function);
    }

    private boolean run(Runnable function) {
        log.info("Run the function");

        try {
            function.run();
        } catch (Exception e) {
            log.warn("Catch an exception when running the method:\n" + e.getMessage());

            return false;
        }

        return true;
    }

    private boolean await(long millis) {
        log.info("Wait " + millis + " ms before running");

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn("Catch an exception when calling the sleep method:\n" + e.getMessage());

            return false;
        }

        return true;
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
