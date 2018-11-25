package pl.mirotcz.groupchat;

import java.util.UUID;

import org.bukkit.Bukkit;

public class Utils {
	
	@SuppressWarnings("deprecation")
	public static UUID getPlayerId(String playerName) {
		if(Bukkit.getPlayer(playerName) != null) {
			return Bukkit.getPlayer(playerName).getUniqueId();
		}
		else if(Bukkit.getOfflinePlayer(playerName) != null) {
			return Bukkit.getOfflinePlayer(playerName).getUniqueId();
		}
		return null;
	}
	
	public static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		}
		catch(NumberFormatException e) { return false; }
	}
	
}
