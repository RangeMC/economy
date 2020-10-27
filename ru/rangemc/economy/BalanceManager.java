package ru.rangemc.economy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceManager {
  public static ConcurrentHashMap<String, Balance> balances = new ConcurrentHashMap<>();
  
  public static TreeMap sorted = new TreeMap<>();
  
  public static Balance addBalance(String player) {
    Balance b = getBalance(player);
    if (getBalance(player) != null)
      return b; 
    b = new Balance(player, Main.config.getDouble("settings.start"));
    MySQL.execute("INSERT INTO `accounts` (`login`, `balance`) VALUES ('" + player + "', '" + Main.config.getDouble("settings.start") + "')");
    balances.put(player, b);
    return b;
  }
  
  public static void removeBalance(String player) {
    balances.remove(player);
    MySQL.execute("UPDATE `accounts` SET `balance`='" + Main.config.getDouble("settings.start") + "' WHERE `login`='" + player + "'");
  }
  
  public static Balance getBalance(String player) {
    return balances.get(player);
  }
  
  public static double round(double d) {
    return (new BigDecimal(d)).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }
}
