package com.colins.springutils.entity;


import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;

public class HystrixWindowArray {
    private int windowLengthInMs;
    private int sampleCount;
    private int intervalInMs;

    private final AtomicReferenceArray<HystrixWindow> array;

    /**
     * The conditional (predicate) update lock is used only when current bucket is deprecated.
     */
    private final ReentrantLock updateLock = new ReentrantLock();


    public HystrixWindowArray(int sampleCount, int intervalInMs) {
        Assert.isTrue(sampleCount > 0, "bucket count is invalid: " + sampleCount);
        Assert.isTrue(intervalInMs > 0, "total time interval of the sliding window should be positive");
        Assert.isTrue(intervalInMs % sampleCount == 0, "time span needs to be evenly divided");

        this.windowLengthInMs = intervalInMs / sampleCount;
        this.intervalInMs = intervalInMs;
        this.sampleCount = sampleCount;

        this.array = new AtomicReferenceArray<>(sampleCount);
    }

    /**
     * 获取当前时间所在的窗口下标索引
     */
    private int calculateTimeIdx(long timeMillis) {
        long timeId = timeMillis / windowLengthInMs;
        // Calculate current index so we can map the timestamp to the leap array.
        return (int)(timeId % array.length());
    }

    /**
     * 获取当前时间所在的窗口开始时间
     */
    private long calculateWindowStart(long timeMillis) {
        return timeMillis - timeMillis % windowLengthInMs;
    }

    private HystrixEntity newEmptyWindowValue(long timeMillis){
        return new HystrixEntity();
    }


    /**
     * 获取当前窗口
     */
    public HystrixWindow currentWindow() {
        return currentWindow(System.currentTimeMillis());
    }

    private HystrixWindow currentWindow(long timeMillis) {
        if (timeMillis < 0) {
            return null;
        }

        int idx = calculateTimeIdx(timeMillis);
        // Calculate current bucket start time.
        long windowStart = calculateWindowStart(timeMillis);

        /*
         * 根据当前时间获取时间窗口对象
         *
         * (1) 如果获取为空，说明窗口还没创建，所以我们创建一个新的窗口(CAS保证线程安全)
         * (2) Bucket is up-to-date, then just return the bucket.
         * (3) Bucket is deprecated, then reset current bucket and clean all deprecated buckets.
         */
        while (true) {
            HystrixWindow old = array.get(idx);
            if (old == null) {
                // 如果获取为空，说明窗口还没创建，所以我们创建一个新的窗口(CAS保证线程安全)
                HystrixWindow window = new HystrixWindow(windowLengthInMs, windowStart, newEmptyWindowValue(timeMillis));
                if (array.compareAndSet(idx, null, window)) {
                    // 创建成功则返回当前窗口
                    return window;
                } else {
                    // 创建失败说明发生了竞争，所以暂时先让出CPU
                    Thread.yield();
                }
            } else if (windowStart == old.getWindowStartInMs()) {
                // 如果窗口已经存在，则对比窗口的开始时间是否相同，相同说明是用同一个窗口，直接返回窗口就可以了
                return old;
            } else if (windowStart > old.getWindowStartInMs()) {
                // 如果窗口已经存在，而且窗口开始时间比之前的窗口开始时间要大
                // 说明原来的窗口已经过时了，需要替换一个新的窗口
                // 所以加锁防止竞争
                if (updateLock.tryLock()) {
                    try {
                        // 这里我选择直接重置之前的窗口（）
                        return resetWindowTo(old, windowStart);
                    } finally {
                        updateLock.unlock();
                    }
                } else {
                    // Contention failed, the thread will yield its time slice to wait for bucket available.
                    Thread.yield();
                }
            } else if (windowStart < old.getWindowStartInMs()) {
                // 窗口的开始时间比之前的窗口开始时间还会小，这种属于异常情况
                // 要是真出现了也只能新建一个窗口返回了
                return new HystrixWindow(windowLengthInMs, windowStart, newEmptyWindowValue(timeMillis));
            }
        }
    }


    /**
     * 获取当前窗口内的值
     */
    public HystrixEntity getWindowValue() {
        return getWindowValue(System.currentTimeMillis());
    }

    public HystrixEntity getWindowValue(long timeMillis) {
        if (timeMillis < 0) {
            return null;
        }
        int idx = calculateTimeIdx(timeMillis);
        HystrixWindow bucket = array.get(idx);
        if (bucket == null || !bucket.isTimeInWindow(timeMillis)) {
            return null;
        }
        return bucket.getHystrixEntity();
    }


    /**
     * 重置一个窗口
     */
    private HystrixWindow resetWindowTo(HystrixWindow window, long startTime){
        return window.resetTo(startTime);
    }

    public List<HystrixEntity> values() {
        return values(System.currentTimeMillis());
    }

    private List<HystrixEntity> values(long timeMillis) {
        if (timeMillis < 0) {
            return new ArrayList<HystrixEntity>();
        }
        int size = array.length();
        List<HystrixEntity> result = new ArrayList<HystrixEntity>(size);

        for (int i = 0; i < size; i++) {
            HystrixWindow window = array.get(i);
            if (window == null || isWindowDeprecated(timeMillis, window)) {
                continue;
            }
            result.add(window.getHystrixEntity());
        }
        return result;
    }

    /**
     * 判断窗口是否有效
     */
    public boolean isWindowDeprecated(long time, HystrixWindow window) {
        return time - window.getWindowStartInMs() > intervalInMs;
    }



}
