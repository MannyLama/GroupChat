package pl.mirotcz.groupchat.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Groups;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;

public class DeleteCommand {

	public DeleteCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.user")) {
				if(Players.getPlayerCurrentGroup(pl.getUniqueId()) != null) {
					Group group = Players.getPlayerCurrentGroup(pl.getUniqueId());
					if(group.getOwner().equals(pl.getUniqueId()) || GroupChat.getPermissions().hasPermission(pl, "groupchat.admin")) {
						processDelete(pl, group);
					}
					else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
				}
				else { Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER); }
			}
			else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
	
	private void processDelete(CommandSender executor, Group group) {
		group.getMembers().forEach(member -> {
			if(Players.getPlayerCurrentGroup(member) != null) {
				if(Players.getPlayerCurrentGroup(member).getID() == group.getID()) {
					GroupChat.getStorage().removePlayerCurrentGroupFromStorage(member);
				}
			}
		});
		Messenger.send(executor, Messages.INFO_GROUP_DELETED);
		group.messageAll(Bukkit.getConsoleSender(), Messages.INFO_OWNER_DELETED.replaceAll("<name>", group.getName()));
		for(UUID member : group.getMembers()) {
			Players.removePlayerActiveMessagesGroup(member, group);
			GroupChat.getStorage().setPlayerActiveMessagesGroupsFromStorage(member, Players.getPlayerActiveMessagesGroups(member));
		}
		GroupChat.getStorage().removeGroupFromStorage(group.getID());
		Groups.removeGroup(group.getID());
		Groups.loadGroupsChatList();
		Players.loadPlayersCurrrentGroups();
	}
}