package pl.mirotcz.groupchat.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Groups;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;
import pl.mirotcz.groupchat.Utils;

public class SwitchCommand {

	public SwitchCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.admin")) {
				if(args.length == 2) {
					if(Utils.isInteger(args[1])) {
						int number = Integer.valueOf(args[1]);
							List<Group> playerGroups = Players.getAllPlayerGroups(pl.getUniqueId());
							if(playerGroups.size() > 0) {
								Group currentGroup = Players.getPlayerCurrentGroup(pl.getUniqueId());
								if(playerGroups.size() >= number) {
									if(!playerGroups.get(number-1).equals(currentGroup)) {
										Group newGroup = playerGroups.get(number-1);
										Players.setPlayerCurrentGroup(pl.getUniqueId(), newGroup);
										GroupChat.getStorage().setPlayerCurrentGroupInStorage(pl.getUniqueId(), newGroup.getID());
										Messenger.send(sender, Messages.INFO_YOU_GROUP_SWITCHED.replaceAll("<group>", newGroup.getName()));
										if(currentGroup != null) {
											currentGroup.messageAll(Bukkit.getConsoleSender(), Messages.INFO_PLAYER_GROUP_SWITCHED.replaceAll("<player>", pl.getName()).replaceAll("<group>", newGroup.getName()));
										}
										newGroup.messageAll(Bukkit.getConsoleSender(), Messages.INFO_PLAYER_GROUP_SWITCHED.replaceAll("<player>", pl.getName()).replaceAll("<group>", newGroup.getName()));
										Groups.loadGroupsChatList();
										
									}
									else { Messenger.send(sender, Messages.INFO_ALREADY_CURRENT_GROUP); }
								}
								else { Messenger.send(sender, Messages.INFO_NO_GROUP_NUMBER); }
							}
							else { Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER); }
					}
					else { Messenger.send(sender, Messages.INFO_TYPE_NUMBER); }
				}
				else { Messenger.send(sender, Messages.INFO_TYPE_NUMBER); }
			}
			//else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
}