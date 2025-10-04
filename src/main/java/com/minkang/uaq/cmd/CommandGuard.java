package com.minkang.uaq.cmd;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class CommandGuard implements Listener {
    private final Plugin plugin;
    private final Set<String> adminAliases;
    private final String adminPerm;
    private final long cooldownMs;
    private final Map<UUID, Long> lastUse = new ConcurrentHashMap<>();

    public CommandGuard(Plugin plugin, java.util.List<String> adminAliases, String adminPerm, long cooldownMs) {
        this.plugin = plugin;
        this.adminAliases = new HashSet<>(adminAliases);
        this.adminPerm = adminPerm;
        this.cooldownMs = cooldownMs;
    }

    @EventHandler
    public void onPreprocess(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();
        if (!msg.startsWith("/")) return;

        long now = System.currentTimeMillis();
        long last = lastUse.getOrDefault(p.getUniqueId(), 0L);
        if (now - last < cooldownMs) {
            e.setCancelled(true);
            p.sendMessage("§7잠시 후 다시 시도하세요.");
            return;
        }
        lastUse.put(p.getUniqueId(), now);

        // base command
        String[] parts = msg.substring(1).split("\s+");
        String base = parts.length > 0 ? parts[0].toLowerCase(Locale.ROOT) : "";

        // 관리자형 명령 보호
        if (adminAliases.contains(base)) {
            if (!p.hasPermission(adminPerm)) {
                e.setCancelled(true);
                p.sendMessage("§c권한이 없습니다.");
            }
        }
    }
}
