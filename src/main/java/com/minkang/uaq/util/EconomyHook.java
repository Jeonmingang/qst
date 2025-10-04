package com.minkang.uaq.util;

import com.minkang.uaq.UAQPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyHook {
    private final UAQPlugin plugin;
    private Economy economy;

    public EconomyHook(UAQPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    private void setup() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("[UAQ] Vault not found - economy features will be disabled.");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
        }
        if (economy == null) {
            plugin.getLogger().warning("[UAQ] No Economy provider found - economy features will be disabled.");
        } else {
            plugin.getLogger().info("[UAQ] Hooked economy: " + economy.getName());
        }
    }

    public boolean hasProvider() {
        return economy != null;
    }

    public boolean has(String playerName, double amount) {
        if (economy == null) return true; // treat as free if no economy
        return economy.has(playerName, amount);
    }

    public void deposit(String playerName, double amount) {
        if (economy == null) return;
        economy.depositPlayer(playerName, amount);
    }

    public boolean withdraw(String playerName, double amount) {
        if (economy == null) return true;
        return economy.withdrawPlayer(playerName, amount).transactionSuccess();
    }
}