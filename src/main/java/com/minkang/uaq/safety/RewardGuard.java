package com.minkang.uaq.safety;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/** 간단한 재진입 잠금: 보상/지급 중복을 수 ms내 방지 */
public final class RewardGuard {
    private static final ConcurrentHashMap<String, Long> inFlight = new ConcurrentHashMap<>();
    private static final long TTL_MS = TimeUnit.SECONDS.toMillis(3);

    private static String key(UUID uuid, String kind, LocalDate day) {
        return uuid.toString() + "|" + kind + "|" + day.toString();
    }

    public static boolean tryLock(UUID uuid, String kind, LocalDate day) {
        final String k = key(uuid, kind, day);
        final long now = System.currentTimeMillis();
        final Long prev = inFlight.putIfAbsent(k, now);
        if (prev == null) return true;
        if (now - prev > TTL_MS) { inFlight.replace(k, prev, now); return true; }
        return false;
    }

    public static void unlock(UUID uuid, String kind, LocalDate day) {
        inFlight.remove(key(uuid, kind, day));
    }
}
