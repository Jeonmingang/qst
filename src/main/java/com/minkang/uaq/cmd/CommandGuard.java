package com.minkang.uaq.cmd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * CommandGuard
 * - Fixes compile-time "illegal escape character" by using proper Java string escapes for regex.
 * - Keeps behavior minimal/safe: only parses the base command; you can extend guard logic later.
 */
public final class CommandGuard implements Listener {

    private static final Pattern WS = Pattern.compile("\\s+"); // proper escaping

    @EventHandler(ignoreCancelled = true)
    public void onPreprocess(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage(); // raw message including leading "/"

        if (msg == null || msg.length() <= 1 || msg.charAt(0) != '/') {
            return;
        }

        // Split once by whitespace using a precompiled pattern to avoid escaping mistakes
        String[] parts = WS.split(msg.substring(1).trim(), 2);
        if (parts.length == 0) {
            return;
        }

        String base = parts[0].toLowerCase(Locale.ROOT);

        // TODO: insert your allow/deny logic here if needed, e.g.:
        // if (isBlocked(base)) {
        //     event.setCancelled(true);
        //     event.getPlayer().sendMessage("§cThis command is blocked.");
        // }
    }
}
