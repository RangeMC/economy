package ru.rangemc.economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class MoneyCommand implements CommandExecutor {
  public FileConfiguration getConfig() {
    return Main.config;
  }
  
  public void showHelp(CommandSender s) {
    for (String m : getConfig().getStringList("messages.help"))
      s.sendMessage(m.replace('&', '§')); 
  }
  
  public String getMsg(String msg) {
    return getConfig().getString(msg).replace('&', '§');
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      if (sender instanceof org.bukkit.entity.Player) {
        sender.sendMessage(getMsg("messages.balance").replace("%count", String.valueOf((long)BalanceManager.getBalance(sender.getName()).getBalance())));
      } else {
        showHelp(sender);
      } 
      return true;
    } 
    if (args.length > 0) {
      String str;
      switch ((str = args[0]).hashCode()) {
        case 96417:
          if (!str.equals("add"))
            break; 
          if (sender.hasPermission("kingeconomy.add")) {
            if (args.length == 1) {
              sender.sendMessage(getMsg("messages.no-nick"));
              return true;
            } 
            if (args.length == 2) {
              sender.sendMessage(getMsg("messages.no-value"));
              return true;
            } 
            if (args.length == 3) {
              double add;
              Balance balance = BalanceManager.getBalance(args[1]);
              if (balance == null) {
                sender.sendMessage(getMsg("messages.no-balance"));
                return true;
              } 
              try {
                add = Math.abs(Double.parseDouble(args[2]));
              } catch (Exception ex) {
                Exception exception1;
                sender.sendMessage(getMsg("messages.int-error"));
                return true;
              } 
              double max = Main.config.getDouble("settings.max");
              if (balance.getBalance() + add > max) {
                add = max - balance.getBalance();
                sender.sendMessage(Main.config.getString("messages.max-balance").replace('&', '§'));
              } 
              balance.addBalance(add);
              sender.sendMessage(getMsg("messages.add").replace("%player", args[1]).replace("%count", String.valueOf((long)add)));
              return true;
            } 
          } else {
            sender.sendMessage(getMsg("messages.no-perms"));
          } 
          return false;
        case 113762:
          if (!str.equals("set"))
            break; 
          if (sender.hasPermission("kingeconomy.set")) {
            if (args.length == 1) {
              sender.sendMessage(getMsg("messages.no-nick"));
              return true;
            } 
            if (args.length == 2) {
              sender.sendMessage(getMsg("messages.no-value"));
              return true;
            } 
            if (args.length == 3) {
              double set;
              Balance balance = BalanceManager.getBalance(args[1]);
              if (balance == null) {
                sender.sendMessage(getMsg("messages.no-balance"));
                return true;
              } 
              try {
                set = Math.abs(Double.valueOf(args[2]).doubleValue());
              } catch (Exception ex) {
                sender.sendMessage(getMsg("messages.int-error"));
                return true;
              } 
              double d1 = Main.config.getDouble("settings.max");
              if (set > d1) {
                set = d1;
                sender.sendMessage(Main.config.getString("messages.max-balance").replace('&', '§'));
              } 
              balance.setBalance(set);
              sender.sendMessage(getMsg("messages.set").replace("%player", args[1]).replace("%count", String.valueOf((long)set)));
              return true;
            } 
          } else {
            sender.sendMessage(getMsg("messages.no-perms"));
          } 
          return false;
        case 115029:
          if (!str.equals("top"))
            break; 
          if (sender.hasPermission("kingeconomy.top")) {
            if (BalanceManager.sorted.size() == 0) {
              sender.sendMessage(getMsg("messages.no-accounts"));
              return true;
            } 
            sender.sendMessage(getMsg("messages.top_1").replace("%num", getConfig().getString("settings.top")));
            for (int b = 0; b < getConfig().getInt("settings.top") && BalanceManager.sorted.size() > b; b++) {
              Balance b1 = (Balance)BalanceManager.sorted.keySet().toArray()[b];
              sender.sendMessage(getConfig().getString("messages.top_2").replace("%player", b1.getPlayer()).replace("%count", String.valueOf((long)BalanceManager.round(b1.getBalance()))));
            } 
          } else {
            sender.sendMessage("messages.no-perms");
          } 
          return false;
        case 3198785:
          if (!str.equals("help"))
            break; 
          showHelp(sender);
          return false;
      } 
      Balance bal = BalanceManager.getBalance(args[0]);
      if (bal == null) {
        sender.sendMessage(getMsg("messages.no-balance"));
        return true;
      } 
      sender.sendMessage(getMsg("messages.other-balance").replace("%count", String.valueOf((long)bal.getBalance())).replace("%player", args[0]));
      return true;
    } 
    return false;
  }
}
