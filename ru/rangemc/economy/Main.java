package ru.rangemc.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  public static FileConfiguration config;
  
  public static Plugin plugin;
  
  public void onEnable() {
    saveDefaultConfig();
    plugin = (Plugin)this;
    config = getConfig();
    MySQL.connect();
    MySQL.getData();
    MySQL.updater();
    registerEconomy();
    for (Player pl : Bukkit.getOnlinePlayers()) {
      if (BalanceManager.getBalance(pl.getName()) == null)
        BalanceManager.addBalance(pl.getName()); 
    } 
    Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)this, new Runnable() {
          public void run() {
            BalanceManager.sorted = Balance.getSortedMap();
          }
        },  0L, 100L);
    Bukkit.getPluginManager().registerEvents(new Listener() {
          @EventHandler
          public void onJoin(PlayerJoinEvent e) {
            String n = e.getPlayer().getName();
            if (BalanceManager.getBalance(n) == null)
              BalanceManager.addBalance(n); 
          }
        },  (Plugin)this);
    if (config.getBoolean("settings.bind-commands")) {
      getCommand("money").setExecutor(new MoneyCommand());
      getCommand("pay").setExecutor(new PayCommand());
    } 
  }
  
  private void registerEconomy() {
    if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
      Bukkit.getLogger().info("Vault не найден!");
    } else {
      ServicesManager sm = getServer().getServicesManager();
      sm.register(Economy.class, new VaultConnector(), (Plugin)this, ServicePriority.Highest);
      Bukkit.getLogger().info("Подключение к Vault выполнено.");
    } 
  }
}
