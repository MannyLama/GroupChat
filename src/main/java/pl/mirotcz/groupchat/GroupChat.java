package pl.mirotcz.groupchat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import pl.mirotcz.groupchat.ConfigManager;
import pl.mirotcz.groupchat.storage.Storage;
import pl.mirotcz.groupchat.storage.Storage_MySQL;
import pl.mirotcz.groupchat.storage.Storage_SQLite;

public final class GroupChat extends JavaPlugin implements Listener{
	
	public Logger log;
	private static Permissions perms = null;
	private static ConfigManager config, lang;
	private static GroupChat instance;
	Map<String, String> invites = new HashMap<String, String>();
	public static String prefix;
	private static Storage storage = null;
	private static CommandListener commandListener = new CommandListener();
	
	@Override
	public void onEnable() {
		instance = this;
		/* Load configs */
		config = new ConfigManager(instance, "config.yml");
		config.getConfig();
	    config.saveDefaultConfig();
	    lang = new ConfigManager(instance, "lang.yml");
		lang.getConfig();
	    lang.saveDefaultConfig();
	    prefix = ChatColor.GREEN + lang.getConfig().getString("GroupPrefix") + ChatColor.WHITE;
	    log = getLogger();
	    perms = new Permissions();
	    perms.setupPermissions();
		Messages.loadMessages();
		Settings.loadSettings();
		setupStorage();
		Groups.loadGroups();
		Groups.loadGroupsChatList();
		Players.loadPlayersCurrrentGroups();
		Players.loadPlayersActiveMessagesGroups();
		getCommand("groupchat").setExecutor(commandListener);
	}
	
	@Override
	public void onDisable() {
		storage.closePool();
		perms.unloadPermissions();
	}
	
	private static void setupStorage() {
		if(Settings.STORAGE_TYPE.equalsIgnoreCase("sqlite")) {
			storage = new Storage_SQLite();
		}
		else {
			storage = new Storage_MySQL();
		}
	}
	
	public static Storage getStorage() {
		return storage;
	}
	
	public static Permissions getPermissions() {
        return perms;
    }
	
	public static ConfigManager getMainConfigManager() {
		return config;
	}
	
	public static ConfigManager getLangConfigManager() {
		return lang;
	}
	
	public static GroupChat getInstance() {
		return instance;
	}
	
	public static void reloadConfigs() {
		config.reloadConfig();
		config.saveConfig();
		lang.reloadConfig();
		lang.saveConfig();
		Settings.loadSettings();
		Messages.loadMessages();
	}
}
