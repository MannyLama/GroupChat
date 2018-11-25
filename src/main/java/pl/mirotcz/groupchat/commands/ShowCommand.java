package pl.mirotcz.groupchat.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;
import pl.mirotcz.groupchat.Utils;

public class ShowCommand {

	public ShowCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.user")) {
				if(args.length == 2) {
					if(Utils.isInteger(args[1])) {
						int number = Integer.valueOf(args[1]);
							List<Group> playerGroups = Players.getAllPlayerGroups(pl.getUniqueId());
							List<Group> activeMessagesGroups = Players.getPlayerActiveMessagesGroups(pl.getUniqueId());
							if(playerGroups.size() > 0) {
								if(playerGroups.size() >= number) {
									Group selectedGroup = playerGroups.get(number - 1);
									if(activeMessagesGroups.contains(selectedGroup)) {
										Players.removePlayerActiveMessagesGroup(pl.getUniqueId(), selectedGroup);
										Messenger.send(sender, Messages.INFO_YOU_GROUP_SHOW_OFF.replaceAll("<group>", selectedGroup.getName()));
									}
									else {
										Players.addPlayerActiveMessagesGroup(pl.getUniqueId(), selectedGroup);
										Messenger.send(sender, Messages.INFO_YOU_GROUP_SHOW_ON.replaceAll("<group>", selectedGroup.getName()));
									}
									GroupChat.getStorage().setPlayerActiveMessagesGroupsFromStorage(pl.getUniqueId(), Players.getPlayerActiveMessagesGroups(pl.getUniqueId()));
								}
								else { Messenger.send(sender, Messages.INFO_NO_GROUP_NUMBER); }
							}
							else { Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER); }
					}
					else { Messenger.send(sender, Messages.INFO_TYPE_NUMBER); }
				}
				else { Messenger.send(sender, Messages.INFO_TYPE_NUMBER); }
			}
			else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
}