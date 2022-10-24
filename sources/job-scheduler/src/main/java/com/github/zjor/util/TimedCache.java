package com.github.zjor.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TimedCache<K, V> {
    public static final long DEFAULT_TIMEOUT_MILLIS = 15_000L;

    private Map<K, TimestampedValue<V>> cache = new HashMap<>();

    private final long timeout;

    public TimedCache(long timeoutMillis) {
        this.timeout = timeoutMillis;
    }

    public TimedCache() {
        this(DEFAULT_TIMEOUT_MILLIS);
    }

    public V put(K key, V value) {
        Objects.requireNonNull(value);
        var prev = cache.put(key, new TimestampedValue<>(value));
        if (prev != null) {
            return prev.value;
        } else {
            return null;
        }
    }

    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }

        var existing = cache.get(key);
        if (existing.isExpired(timeout)) {
            cache.remove(key);
            return null;
        } else {
            return existing.value;
        }
    }


    private static class TimestampedValue<V> {
        final V value;
        final long timestamp;

        private TimestampedValue(V value) {
            this.value = value;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired(long timeout) {
            var now = System.currentTimeMillis();
            return (now - timestamp) >= timeout;
        }
    }
}
