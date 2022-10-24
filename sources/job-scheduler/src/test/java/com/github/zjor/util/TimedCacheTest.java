package com.github.zjor.util;

import org.junit.Assert;
import org.junit.Test;

public class TimedCacheTest {

    @Test
    public void shouldPutAndGet() {
        var cache = new TimedCache<String, String>(1000L);
        cache.put("name", "alice");
        Assert.assertEquals("alice", cache.get("name"));
    }

    @Test
    public void shouldPutAndEvict() throws InterruptedException {
        var cache = new TimedCache<String, String>(5L);
        cache.put("name", "alice");
        Thread.sleep(10L);
        Assert.assertNull(cache.get("name"));
    }

}