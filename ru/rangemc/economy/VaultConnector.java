package ru.rangemc.economy;

import java.util.ArrayList;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

public class VaultConnector implements Economy {
  private String getMsg(String msg) {
    return getConfig().getString(msg).replace('&', '§');
  }
  
  private FileConfiguration getConfig() {
    return Main.config;
  }
  
  public boolean isEnabled() {
    return (Main.plugin != null && Main.plugin.isEnabled());
  }
  
  public String getName() {
    return "KingEconomy";
  }
  
  public boolean hasBankSupport() {
    return false;
  }
  
  public int fractionalDigits() {
    return 2;
  }
  
  public String format(double amount) {
    return String.valueOf(amount);
  }
  
  public String currencyNamePlural() {
    return "монеток";
  }
  
  public String currencyNameSingular() {
    return "монеток";
  }
  
  public boolean hasAccount(String playerName) {
    return (BalanceManager.getBalance(playerName) != null);
  }
  
  public boolean hasAccount(OfflinePlayer offlinePlayer) {
    return hasAccount(offlinePlayer.getName());
  }
  
  public double getBalance(String playerName) {
    if (BalanceManager.getBalance(playerName) == null)
      return 0.0D; 
    return BalanceManager.getBalance(playerName).getBalance();
  }
  
  public double getBalance(OfflinePlayer offlinePlayer) {
    return getBalance(offlinePlayer.getName());
  }
  
  public boolean has(String playerName, double amount) {
    return BalanceManager.getBalance(playerName).has(amount);
  }
  
  public boolean has(OfflinePlayer offlinePlayer, double amount) {
    return has(offlinePlayer.getName(), amount);
  }
  
  public EconomyResponse withdrawPlayer(String playerName, double amount) {
    return withdrawPlayer(BalanceManager.getBalance(playerName), amount);
  }
  
  public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
    return withdrawPlayer(BalanceManager.getBalance(offlinePlayer.getName()), amount);
  }
  
  private EconomyResponse withdrawPlayer(Balance account, double amount) {
    if (account == null)
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, getMsg("messages.no-balance")); 
    if (account.getBalance() - amount < 0.0D)
      return new EconomyResponse(0.0D, account.getBalance(), EconomyResponse.ResponseType.FAILURE, getMsg("messages.no-money")); 
    account.removeBalance(amount);
    return new EconomyResponse(amount, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, getMsg("messages.take-success"));
  }
  
  public EconomyResponse depositPlayer(String playerName, double amount) {
    return depositPlayer(BalanceManager.getBalance(playerName), amount);
  }
  
  public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
    return depositPlayer(BalanceManager.getBalance(offlinePlayer.getName()), amount);
  }
  
  private EconomyResponse depositPlayer(Balance account, double amount) {
    if (account == null)
      return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, getMsg("messages.no-balance")); 
    if (account.getBalance() + amount > getConfig().getDouble("settings.max"))
      return new EconomyResponse(amount, account.getBalance(), EconomyResponse.ResponseType.FAILURE, getMsg("messages.max-balance")); 
    amount = BalanceManager.round(amount);
    account.addBalance(amount);
    return new EconomyResponse(amount, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, getMsg("messages.give-success"));
  }
  
  public EconomyResponse createBank(String name, String player) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse deleteBank(String name) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse bankBalance(String name) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse bankHas(String name, double amount) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse bankWithdraw(String name, double amount) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse bankDeposit(String name, double amount) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse isBankOwner(String name, String playerName) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse isBankMember(String name, String playerName) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
    return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Функция не поддерживается.");
  }
  
  public List getBanks() {
    return new ArrayList();
  }
  
  public boolean createPlayerAccount(String playerName) {
    if (!hasAccount(playerName))
      BalanceManager.addBalance(playerName); 
    return hasAccount(playerName);
  }
  
  public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
    return createPlayerAccount(offlinePlayer.getName());
  }
  
  public boolean createPlayerAccount(String playerName, String world) {
    return createPlayerAccount(playerName);
  }
  
  public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
    return createPlayerAccount(offlinePlayer.getName());
  }
  
  public EconomyResponse depositPlayer(String player, String world, double amount) {
    return depositPlayer(player, amount);
  }
  
  public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
    return depositPlayer(offlinePlayer, amount);
  }
  
  public double getBalance(String player, String world) {
    return getBalance(player);
  }
  
  public double getBalance(OfflinePlayer offlinePlayer, String world) {
    return getBalance(offlinePlayer);
  }
  
  public boolean has(String player, String world, double amount) {
    return has(player, amount);
  }
  
  public boolean has(OfflinePlayer offlinePlayer, String world, double amount) {
    return has(offlinePlayer, amount);
  }
  
  public boolean hasAccount(String player, String world) {
    return hasAccount(player);
  }
  
  public boolean hasAccount(OfflinePlayer offlinePlayer, String world) {
    return hasAccount(offlinePlayer);
  }
  
  public EconomyResponse withdrawPlayer(String player, String world, double amount) {
    return withdrawPlayer(player, amount);
  }
  
  public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
    return withdrawPlayer(offlinePlayer, amount);
  }
}
