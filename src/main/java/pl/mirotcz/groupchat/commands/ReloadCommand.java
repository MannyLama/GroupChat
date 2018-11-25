package pl.mirotcz.groupchat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;

public class ReloadCommand {
	
	public ReloadCommand(CommandSender sender, String[] args) {
		if(GroupChat.getPermissions().hasPermission(sender, "groupchat.admin") || sender instanceof ConsoleCommandSender) {
			GroupChat.reloadConfigs();
			Messenger.send(sender, Messages.INFO_PLUGIN_RELOADED);
		}
		else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
	}
}