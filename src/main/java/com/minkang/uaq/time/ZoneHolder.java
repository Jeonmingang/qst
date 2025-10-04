package com.minkang.uaq.time;

import java.time.ZoneId;

public final class ZoneHolder {
    private static ZoneId ZONE = ZoneId.of("Asia/Seoul");
    public static ZoneId server() { return ZONE; }
    public static void set(ZoneId id) { if (id != null) ZONE = id; }
}
