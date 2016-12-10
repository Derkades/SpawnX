package com.robinmc.spawnx;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	private static Plugin plugin;
	
	static Plugin getPlugin(){
		return plugin;
	}
	
	@Override
	public void onEnable(){
		super.getServer().getPluginManager().registerEvents(this, this);
		super.saveDefaultConfig();
		plugin = this;
	}
	
	@Override
	public void onDisable(){
		plugin = null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (command.getName().equalsIgnoreCase("setspawn")){
			if (sender instanceof Player){
				Player player = (Player) sender;
				if (player.hasPermission("jointp.setspawn")){
					setSpawnLocation(player.getLocation());
					player.sendMessage(ChatColor.DARK_AQUA + "The spawn location has been set!");
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
					return true;
				}
			} else {
				sender.sendMessage("You have to be a player in order to execute this command.");
				return true;
			}
		} else if (command.getName().equalsIgnoreCase("spawn")){
			if (sender instanceof Player){
				Player player = (Player) sender;
				if (player.hasPermission("jointp.spawn")){
					player.teleport(getSpawnLocation());
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
					return true;
				}
			} else {
				sender.sendMessage("You have to be a player in order to execute this command.");
				return true;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		if (Config.getConfig().getBoolean("teleport-on-join")){
			Player player = event.getPlayer();
			player.teleport(getSpawnLocation());
		}
	}
	
	private Location getSpawnLocation(){
		String worldName = Config.getConfig().getString("world-name");
		double x = Config.getConfig().getDouble("x");
		double y = Config.getConfig().getDouble("y");
		double z = Config.getConfig().getDouble("z");
		float pitch = (float) Config.getConfig().getDouble("pitch");
		float yaw = (float) Config.getConfig().getDouble("yaw");
		return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
	}
	
	private void setSpawnLocation(Location loc){
		Config.getConfig().set("world-name", loc.getWorld().getName());
		Config.getConfig().set("x", loc.getX());
		Config.getConfig().set("y", loc.getY());
		Config.getConfig().set("z", loc.getZ());
		Config.getConfig().set("pitch", loc.getPitch());
		Config.getConfig().set("yaw", loc.getYaw());
		super.saveConfig();
	}

}
