package pl.mirotcz.groupchat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Messenger {	
	
	/* Used to send message to unknown command sender */
	
	public static void send(CommandSender recipient, String message) {
		if(recipient != null && message != null) {
			if(recipient instanceof Player) {
				if(!((Player) recipient).isOnline()) {
					return;
				}
			}
			recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PLUGIN_PREFIX + message));
		}
	}
	
	public static void sendCustomPrefix(CommandSender recipient, String prefix, String message) {
		if(recipient != null && message != null) {
			if(recipient instanceof Player) {
				if(!((Player) recipient).isOnline()) {
					return;
				}
			}
			recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
		}
	}
	
	/* Used to send message to server console */
	
	public static void sendConsole(String message) {
		if(message != null) {
			System.out.println(ChatColor.translateAlternateColorCodes('&', Messages.PLUGIN_PREFIX + message));
		}
	}
}