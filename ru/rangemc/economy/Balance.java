package ru.rangemc.economy;

import java.util.HashMap;
import java.util.TreeMap;

public class Balance {
  private String player;
  
  protected double balance;
  
  public Balance(String player, double balance) {
    this.player = player;
    this.balance = balance;
  }
  
  public String getPlayer() {
    return this.player;
  }
  
  public double getBalance() {
    return this.balance;
  }
  
  public boolean has(double bal) {
    return (this.balance >= bal);
  }
  
  public void setBalance(double bal) {
    this.balance = bal;
    MySQL.execute("UPDATE `accounts` SET `balance`='" + this.balance + "' WHERE `login`='" + this.player + "'");
  }
  
  public void addBalance(double bal) {
    setBalance(this.balance + bal);
  }
  
  public void removeBalance(double bal) {
    setBalance(this.balance - bal);
  }
  
  public static TreeMap getSortedMap() {
    HashMap<Object, Object> sorting = new HashMap<>();
    for (Balance vc : BalanceManager.balances.values())
      sorting.put(vc, Integer.valueOf((int)vc.getBalance())); 
    ValueComparator vc1 = new ValueComparator(sorting);
    TreeMap<Object, Object> res1 = new TreeMap<>(vc1);
    res1.putAll(sorting);
    return res1;
  }
}
