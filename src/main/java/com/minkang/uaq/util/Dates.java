
package com.minkang.uaq.util;
import org.bukkit.configuration.file.FileConfiguration; import java.time.*; import java.time.format.DateTimeFormatter;
public class Dates {
    private final ZoneId zoneId; private final int resetHour;
    public Dates(FileConfiguration cfg){ this.zoneId=ZoneId.of(cfg.getString("timezone","Asia/Seoul")); this.resetHour=cfg.getInt("resetHourKST",5); }
    public LocalDate todayGameDate(){ ZonedDateTime now=ZonedDateTime.now(zoneId); return now.getHour()<resetHour? now.minusDays(1).toLocalDate() : now.toLocalDate(); }
    public ZonedDateTime nextReset(){ ZonedDateTime now=ZonedDateTime.now(zoneId); ZonedDateTime next = now.withHour(resetHour).withMinute(0).withSecond(0).withNano(0); if(!now.isBefore(next)) next = next.plusDays(1); return next; }
    public String fmt(LocalDate d){ return d.format(DateTimeFormatter.ISO_DATE); }
    public String fmtTime(ZonedDateTime t){ return t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z")); }
}
