package ru.rangemc.economy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;

public class MySQL {
  public static Connection connection = null;
  
  public static void connect() {
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable() {
            public void run() {
              try {
                MySQL.connection = DriverManager.getConnection("jdbc:mysql://" + Main.config.getString("mysql.host") + ":" + Main.config.getString("mysql.port") + "/" + Main.config.getString("mysql.database") + "?useUnicode=true&characterEncoding=UTF-8&" + "user=" + Main.config.getString("mysql.username") + "&password=" + Main.config.getString("mysql.password"));
                MySQL.log("Соединение с базой данных установлено.");
              } catch (SQLException e) {
                MySQL.log("MySQL ERROR: " + e.getMessage());
              } 
            }
          });
    } catch (Exception e) {
      log("MySQL ERROR: " + e.getMessage());
    } 
  }
  
  public static boolean hasConnected() {
    try {
      return !connection.isClosed();
    } catch (Exception e) {
      return false;
    } 
  }
  
  private static void log(String msg) {
    Main.plugin.getLogger().info(msg);
  }
  
  public static String strip(String str) {
    str = str.replaceAll("<[^>]*>", "");
    str = str.replace("\\", "\\\\");
    str = str.trim();
    return str;
  }
  
  public static void executeSync(String query) {
    try {
      if (!hasConnected())
        connect(); 
      Statement e = connection.createStatement();
      e.execute(strip(query));
      e.close();
    } catch (Exception e) {
      log("Произошла ошибка: " + e.getMessage());
    } 
  }
  
  public static void execute(final String query) {
    Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable() {
          public void run() {
            try {
              if (!MySQL.hasConnected())
                MySQL.connect(); 
              Statement e = MySQL.connection.createStatement();
              e.execute(MySQL.strip(query));
              e.close();
            } catch (Exception exception) {}
          }
        });
  }
  
  public static void getData() {
    ResultSet eco = null;
    try {
      eco = executeQuery("SELECT * FROM `accounts`");
      while (eco.next()) {
        try {
          String e1 = eco.getString("login");
          Balance b = new Balance(e1, eco.getDouble("balance"));
          BalanceManager.balances.put(e1, b);
        } catch (Exception var13) {
          log("Произошла ошибка: " + var13.getMessage());
        } 
      } 
    } catch (Exception var14) {
      log("Произошла ошибка: " + var14.getMessage());
    } finally {
      try {
        if (eco != null && !eco.isClosed())
          eco.close(); 
      } catch (SQLException var12) {
        log("Произошла ошибка: " + var12.getMessage());
      } 
    } 
  }
  
  public static void updater() {
    Bukkit.getScheduler().runTaskTimerAsynchronously(Main.plugin, new Runnable() {
          public void run() {
            ResultSet eco = null;
            try {
              eco = MySQL.executeQuery("SELECT * FROM `accounts`");
              while (eco.next()) {
                try {
                  String e = eco.getString("login");
                  Balance b = BalanceManager.getBalance(e);
                  if (b == null) {
                    BalanceManager.balances.put(e, new Balance(e, eco.getDouble("balance")));
                    continue;
                  } 
                  if (b.getBalance() != eco.getDouble("balance"))
                    b.balance = eco.getDouble("balance"); 
                } catch (Exception var13) {
                  MySQL.log("Произошла ошибка: " + var13.getMessage());
                } 
              } 
            } catch (Exception exception) {
              try {
                if (eco != null && !eco.isClosed())
                  eco.close(); 
              } catch (SQLException var12) {
                MySQL.log("Произошла ошибка: " + var12.getMessage());
              } 
            } finally {
              try {
                if (eco != null && !eco.isClosed())
                  eco.close(); 
              } catch (SQLException var12) {
                MySQL.log("Произошла ошибка: " + var12.getMessage());
              } 
            } 
          }
        }, 0L, 100L);
  }
  
  public static ResultSet executeQuery(String query) throws Exception {
    ResultSet var3;
    if (!hasConnected())
      connect(); 
    Statement st = connection.createStatement();
    try {
      var3 = connection.createStatement().executeQuery(strip(query));
    } finally {
      st.close();
    } 
    return var3;
  }
  
  public static void disconnect() {
    try {
      if (connection != null)
        connection.close(); 
    } catch (Exception exception) {}
  }
}
