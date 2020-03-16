package pl.mirotcz.groupchat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;

public class HelpCommand {
	
	public HelpCommand(CommandSender sender) {
		Messenger.send(sender, "&7&m---|--------------&r&7>&r&6&l Groupchat help &r&7<&m--------------|---");
		if(GroupChat.getPermissions().hasPermission(sender, "groupchat.user")  || sender instanceof ConsoleCommandSender) {
			Messenger.send(sender, Messages.HELP_COMMAND_SAY);
			Messenger.send(sender, Messages.HELP_COMMAND_JOIN);
			Messenger.send(sender, Messages.HELP_COMMAND_INFO);
			Messenger.send(sender, Messages.HELP_COMMAND_SHOW);
			Messenger.send(sender, Messages.HELP_COMMAND_LEAVE);

			if(!GroupChat.getPermissions().hasPermission(sender, "groupchat.guildOwner")){
				Messenger.send(sender, "&3If you want to create your own group, visit our &dDiscord &3for more info. --> &ddiscord&7.&emcfal&7.&eorg");
			}

			if(GroupChat.getPermissions().hasPermission(sender, "groupchat.guildOwner") || sender instanceof ConsoleCommandSender) {
				Messenger.send(sender, "&7&m---|-------------&r&7>&r&6 GuildOwner privileges &r&7<&m-------------|---");
				Messenger.send(sender, Messages.HELP_COMMAND_INVITE);
				Messenger.send(sender, Messages.HELP_COMMAND_KICK);
				Messenger.send(sender, Messages.HELP_COMMAND_BAN);
			}
			if(GroupChat.getPermissions().hasPermission(sender, "groupchat.admin") || sender instanceof ConsoleCommandSender) {
				Messenger.send(sender, "&7&m---|---------------&r&7>&r&6 Admin commands &r&7<&m---------------|---");
				Messenger.send(sender, Messages.HELP_COMMAND_SET_TYPE);
				Messenger.send(sender, Messages.HELP_COMMAND_SET_OWNER);
				Messenger.send(sender, Messages.HELP_COMMAND_MY_GROUPS);
				Messenger.send(sender, Messages.HELP_COMMAND_SWITCH);
				Messenger.send(sender, Messages.HELP_COMMAND_CREATE);
				Messenger.send(sender, Messages.HELP_COMMAND_LIST);
				Messenger.send(sender, Messages.HELP_COMMAND_DELETE);
				Messenger.send(sender, Messages.HELP_COMMAND_RELOAD);
			}
		}
		else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		Messenger.send(sender,   "&7&m---|----------------------------------------------|---");

	}
}