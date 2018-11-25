package pl.mirotcz.groupchat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Groups;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;

public class LeaveCommand {
	
	public LeaveCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.user")) {
				if(Players.getPlayerCurrentGroup(pl.getUniqueId()) != null) {
					Group group = Players.getPlayerCurrentGroup(pl.getUniqueId());
					if(!group.getOwner().equals(pl.getUniqueId())) {
						group.removeMember(pl);
						Messenger.send(sender, Messages.INFO_YOU_LEFT_GROUP.replaceAll("<group>", group.getName()));
						group.messageAll(Bukkit.getConsoleSender(), Messages.INFO_PLAYER_LEFT_GROUP.replaceAll("<player>", pl.getName()));
						GroupChat.getStorage().removePlayerCurrentGroupFromStorage(pl.getUniqueId());
						GroupChat.getStorage().updateGroupInStorage(group);
						Groups.loadGroupsChatList();
						Players.loadPlayersCurrrentGroups();
						Players.removePlayerActiveMessagesGroup(pl.getUniqueId(), group);
						GroupChat.getStorage().setPlayerActiveMessagesGroupsFromStorage(pl.getUniqueId(), Players.getPlayerActiveMessagesGroups(pl.getUniqueId()));
					}
					else { Messenger.send(sender, Messages.INFO_OWNER_CANNOT_LEAVE); }
				}
				else { Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER); }
			}
			else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
}