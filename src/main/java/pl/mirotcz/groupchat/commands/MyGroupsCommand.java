package pl.mirotcz.groupchat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;

public class MyGroupsCommand {

	public MyGroupsCommand(CommandSender sender) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.admin")) {
				if(Players.getAllPlayerGroups(pl.getUniqueId()).size() > 0) {
					int number = 1;
					for(Group group : Players.getAllPlayerGroups(pl.getUniqueId())) {
						Messenger.send(sender, Messages.GROUP_DISPLAY_LIST_FORMAT
						.replaceAll(Messages.PLUGIN_PREFIX, "")
						.replaceAll("<number>", String.valueOf(number))
						.replaceAll("<group>", group.getName())
						.replaceAll("<type>", group.getTypeFormatted()
						.replaceAll("<memberscount>", String.valueOf(group.getMembers().size()))));
						number++;
					}
				}
				else { Messenger.send(sender, Messages.INFO_NO_GROUPS); }
			}
			//else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
}