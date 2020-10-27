package ru.rangemc.economy;

import org.bukkit.configuration.file.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class PayCommand implements CommandExecutor
{
    public FileConfiguration getConfig() {
        return Main.config;
    }
    
    public String getMsg(final String msg) {
        return this.getConfig().getString(msg).replace('&', '§');
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(this.getMsg("messages.no-nick"));
            return true;
        }
        final Balance b = BalanceManager.getBalance(args[0]);
        if (b == null) {
            sender.sendMessage(this.getMsg("messages.no-balance"));
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage(this.getMsg("messages.no-value"));
            return true;
        }
        double sum;
        try {
            sum = Math.abs(Double.valueOf(args[1]));
        }
        catch (Exception ex) {
            sender.sendMessage(this.getMsg("messages.int-error"));
            return true;
        }
        final Balance userBal = BalanceManager.getBalance(sender.getName());
        if (!userBal.has(sum)) {
            sender.sendMessage(this.getMsg("messages.no-money"));
            return true;
        }
        if (b.getBalance() + sum > this.getConfig().getDouble("settings.max")) {
            sender.sendMessage(this.getMsg("messages.max-balance"));
            return true;
        }
        sum = BalanceManager.round(sum);
        userBal.removeBalance(sum);
        b.addBalance(sum);
        sender.sendMessage(this.getMsg("messages.pay").replace("%player", args[0]).replace("%count", args[1]));
        return true;
    }
}
