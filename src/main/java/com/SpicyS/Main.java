package com.SpicyS;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    int time;
    boolean maintanance = false;
    Integer ID;
    ArrayList<String> l = new ArrayList();

    public Main() {
    }

    public void onEnable() {
        this.loadSetings();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("[SpicyKick] KubaKick is now enabled");
    }

    public void loadSetings() {
        FileConfiguration c = this.getConfig();
        c.addDefault("Kickall.message", "&bKubaFactions &7->&e Server Restart!");
        c.addDefault("Kickall.messagekickall", "&bKubaFactions &7->&e You kicked %online players.");
        c.addDefault("Kickall.noperm", "&bKubaFactions &7->&e You don't have access to do this!");
        c.addDefault("kickall.kickmessage", "&bKubaFactions &7->&e Server on maintanance!");
        c.options().copyDefaults(true);
        this.saveConfig();
    }

    public void onDisable() {
        this.getLogger().info("[SpicyKick] KubaKick is now disabled");
    }

    public String replace(String mesaj) {
        return mesaj.replace("&", "§").replace("->", "»").replace(">>", "➣");
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (!this.l.contains(p.getName()) && this.maintanance) {
            e.setResult(Result.KICK_OTHER);
            e.setKickMessage(this.replace(this.getConfig().getString("kickall.kickmessage")));
        }

    }

    public void start(final CommandSender sender) {
        this.ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                --Main.this.time;
                if (Main.this.time == 30) {
                    Bukkit.broadcastMessage(Main.this.replace("&7[&bKubaFactions&7] &eServer will be in maintanance in &f30 &eseconds."));
                }

                if (Main.this.time == 10) {
                    Bukkit.broadcastMessage(Main.this.replace("&7[&bKubaFactions&7] &eServer will be in maintanance in &f10 &eseconds."));
                }

                if (Main.this.time == 5) {
                    Bukkit.broadcastMessage(Main.this.replace("&7[&bKubaFactions&7] &eServer will be in maintanance in &f5 &eseconds."));
                }

                if (Main.this.time == 4) {
                    Bukkit.broadcastMessage(Main.this.replace("&7[&bKubaFactions&7] &eServer will be in maintanance in &f4 &eseconds."));
                }

                if (Main.this.time == 3) {
                    Bukkit.broadcastMessage(Main.this.replace("&7[&bKubaFactions&7] &eServer will be in maintanance in &f3 &eseconds."));
                }

                if (Main.this.time == 2) {
                    Bukkit.broadcastMessage(Main.this.replace("&7[&bKubaFactions&7] &eServer will be in maintanance in &f2 &eseconds."));
                }

                if (Main.this.time == 0) {
                    Bukkit.broadcastMessage(Main.this.replace("&7[&bKubaFactions&7] &eServer will be in maintanance in &f1 &esecond."));
                    Bukkit.setWhitelist(true);
                    Iterator var2 = Bukkit.getOnlinePlayers().iterator();

                    while(var2.hasNext()) {
                        Player p1 = (Player)var2.next();
                        if (!p1.getName().contains(sender.getName())) {
                            String kickallmessage = Main.this.getConfig().getString("Kickall.message");
                            p1.kickPlayer(Main.this.replace(kickallmessage));
                        }
                    }

                    Bukkit.getScheduler().cancelTask(Main.this.ID);
                    Main.this.maintanance = true;
                }

            }
        }, 20L, 20L);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kickall")) {
            if (!sender.hasPermission("kickall.kick")) {
                sender.sendMessage(this.replace(this.getConfig().getString("Kickall.noperm")));
                return true;
            } else {
                sender.sendMessage(this.replace(this.getConfig().getString("Kickall.messagekickall").replace("%online", String.valueOf(Bukkit.getOnlinePlayers().size() - 1))));
                String kickallmessage = this.getConfig().getString("Kickall.message");
                Iterator var7 = Bukkit.getOnlinePlayers().iterator();

                while(var7.hasNext()) {
                    Player p1 = (Player)var7.next();
                    if (!p1.getName().contains(sender.getName())) {
                        p1.kickPlayer(this.replace(kickallmessage));
                    }
                }

                return true;
            }
        } else if (!command.getName().equalsIgnoreCase("maintanance") && !command.getName().equalsIgnoreCase("mentenanta") && !command.getName().equalsIgnoreCase("mantenimiento")) {
            return false;
        } else if (!sender.hasPermission("kickall.maintanance")) {
            sender.sendMessage(this.replace(this.getConfig().getString("Kickall.noperm")));
            return true;
        } else if (args.length == 0) {
            sender.sendMessage(this.replace("&7[&bSpicyKick&7] &eVersion v1.0 by YoursTrulySpicy"));
            sender.sendMessage(this.replace(" &eCommand &6->&e /maintanance on/off"));
            sender.sendMessage(this.replace(" &eCommand &6->&e /maintanance add <player> &7(coming soon)"));
            sender.sendMessage(this.replace(" &eCommand &6->&e /kickall"));
            return true;
        } else if (args[0].equalsIgnoreCase("on")) {
            this.time = 60;
            this.start(sender);
            Bukkit.broadcastMessage(this.replace("&7[&bKubaFactions&7] &eServer will be in maintanance in &f60 &eseconds."));
            return true;
        } else if (args[0].equalsIgnoreCase("off")) {
            Bukkit.broadcastMessage(this.replace("&7[&bKubaFactions&7] &eMaintanance mode is now disabled"));
            this.maintanance = false;
            return true;
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args[1] != null) {
                sender.sendMessage("Player is not online.");
                return true;
            } else {
                this.l.add(args[1]);
                return true;
            }
        } else {
            return true;
        }
    }
}
