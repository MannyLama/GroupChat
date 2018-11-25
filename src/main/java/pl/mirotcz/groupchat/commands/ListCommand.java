package pl.mirotcz.groupchat.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Groups;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Utils;

public class ListCommand {

	public ListCommand(CommandSender sender, String[] args) {
		if(GroupChat.getPermissions().hasPermission(sender, "groupchat.user")) {
			List<List<String>> groups = Groups.getAllGroupsChatList();
			int number = 1;
			if(args.length > 1) {
				if(Utils.isInteger(args[1])) {
					number = Integer.valueOf(args[1]);
				}
				else { Messenger.send(sender, Messages.INFO_TYPE_NUMBER); return; }
			}
			if(groups.size() > 0) {
				if(groups.size() >= number) {
					for(String line : groups.get(number-1)) {
						Messenger.send(sender, line.replaceAll(Messages.PLUGIN_PREFIX, ""));
					}
				}
				else { Messenger.send(sender, Messages.INFO_NO_PAGE); }
			}
			else { Messenger.send(sender, Messages.INFO_NO_GROUPS); }
		}
		else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
	}
}