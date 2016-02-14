package ru.winside.UraniumProtect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class UraniumProtectMain extends JavaPlugin {
	public static List<Integer> blocked = new ArrayList<Integer>();
	private List<Integer> items = new ArrayList<Integer>();
	static int Helmet;
	static int Chestplate;
	static int Leggings;
	static int Boots;
	static String msg = "";
	private FileConfiguration Config = null;
	private File ConfigFile = null;
	
	public static String messageBuilder(String s){
		return ChatColor.AQUA + "[UraniumProtect] " + s;
	}
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new PickupItemListener(), this);

		try {
			Metrics metrics = new Metrics(this);
	        	metrics.start();
	        
			loadConfiguration();
			updateLists();
		} catch (IOException e) {
			System.out.print(messageBuilder("Can't load config!"));
		}
	}
	
	private void reloadConfiguration(){
		try {
			Config.load(ConfigFile);
			loadConfiguration();
			updateLists();
		} catch (IOException e) {
			System.out.print(messageBuilder("Can't load config!"));
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfiguration() throws IOException {
		if (ConfigFile == null) {
		    ConfigFile = new File(getDataFolder() + File.separator, "config.yml");
		}
		
		Config = YamlConfiguration.loadConfiguration(ConfigFile);
		Integer[] temp;
		
		Config.addDefault("Helmet", Helmet);
		Config.addDefault("Chestplate", Chestplate);
		Config.addDefault("Leggings", Leggings);
		Config.addDefault("Boots", Boots);
		temp = new Integer[] { 152, 155, 157, 161, 165, 166 };
		Config.addDefault("Items", Arrays.asList(temp));
		Config.options().copyDefaults(true);
		Config.addDefault("msg", "Чтобы поднять предмет одень защитный костюм");
		Config.save(ConfigFile);
		Helmet = Config.getInt("Helmet");
		Chestplate = Config.getInt("Chestplate");
		Leggings = Config.getInt("Leggings");
		Boots = Config.getInt("Boots");
		items = Config.getIntegerList("Items");
		msg = Config.getString("msg");
		msg = msg.replaceAll("&([0-9a-f])", "\u00A7$1");
	}

	public void updateLists() {
		blocked.clear();
		blocked.addAll(items);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(messageBuilder("Используйте команду /uraniumprotect help для получения списка комманд"));
			return true;
		} else if ((args.length == 1) && args[0].equalsIgnoreCase("help")) {
			help(sender);
			return true;
		} else if (sender.hasPermission("uprotect.all")) {
			if ((args.length == 1) && args[0].equalsIgnoreCase("list")) {
				sender.sendMessage(messageBuilder(blocked.toString()));
				return true;
			} else if ((args.length == 1) && args[0].equalsIgnoreCase("reload")) {
				reloadConfiguration();
				sender.sendMessage(messageBuilder("Плагин перезагружен"));
				return true;
			} else if ((args.length == 1) && args[0].equalsIgnoreCase("armor")) {
				sender.sendMessage(messageBuilder("Helmet-" + Helmet + " Chestplate-" + Chestplate
						+ " Leggings-" + Leggings + " Boots-" + Boots));
				return true;
			}
		} else {
			sender.sendMessage(messageBuilder(ChatColor.RED + "You don't have permissions."));
		}
		return false;
	}

	private void help(CommandSender sender) {
		sender.sendMessage(messageBuilder("/uraniumprotect reload "
				+ ChatColor.WHITE + "-" + ChatColor.AQUA
				+ " перезагрузить плагин"));
		sender.sendMessage(messageBuilder("/uraniumprotect list "
				+ ChatColor.WHITE + "-" + ChatColor.AQUA
				+ " вывести список предметов"));
	}
}
