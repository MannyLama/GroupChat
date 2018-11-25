package pl.mirotcz.groupchat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.GroupUtils;
import pl.mirotcz.groupchat.Groups;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;
import pl.mirotcz.groupchat.Utils;

public class InfoCommand {

	public InfoCommand(CommandSender sender, String[] args) {
		if(GroupChat.getPermissions().hasPermission(sender, "groupchat.user")) {
			Group group;
			if(args.length == 1) {
				if(sender instanceof Player) {
					Player pl = (Player) sender;
					if(Players.getPlayerCurrentGroup(pl.getUniqueId()) != null) {
						group = Players.getPlayerCurrentGroup(pl.getUniqueId());
						GroupUtils.sendGroupInfo(sender, group);
					}
					else { Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER); }
				}
				else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
			}
			else if(args.length == 2) {
				if(Utils.isInteger(args[1])) {
					int number = Integer.valueOf(args[1]);
					if(Groups.getGroup(number) != null) {
						group = Groups.getGroup(number);
						GroupUtils.sendGroupInfo(sender, group);
					}
					else { Messenger.send(sender, Messages.INFO_GROUP_NOT_EXIST); }
				}
			}
			else { new HelpCommand(sender); }
		}
		else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
	}
}