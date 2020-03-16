package pl.mirotcz.groupchat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Groups;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;
import pl.mirotcz.groupchat.Settings;

public class CreateCommand {

	public CreateCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.admin")) {
				if(args.length > 1) {
					String groupName = args[1];
					if(!Groups.groupExists(groupName)) {
						if(Groups.getGroupsOwnedByPlayer(pl.getUniqueId()).size() < Settings.MAX_OWNED_GROUPS) {
							Group group = new Group(pl.getUniqueId(), groupName);
							Groups.addGroup(group);
							GroupChat.getStorage().updateGroupInStorage(group);
							GroupChat.getStorage().setPlayerCurrentGroupInStorage(pl.getUniqueId(), group.getID());
							Players.setPlayerCurrentGroup(pl.getUniqueId(), group);
							Players.addPlayerActiveMessagesGroup(pl.getUniqueId(), group);
							GroupChat.getStorage().setPlayerActiveMessagesGroupsFromStorage(pl.getUniqueId(), Players.getPlayerActiveMessagesGroups(pl.getUniqueId()));
							Messenger.send(sender, Messages.INFO_GROUP_CREATED.replaceAll("<name>", groupName));
							Groups.loadGroupsChatList();
						}
						else { Messenger.send(sender, Messages.INFO_MAX_OWNED_GROUPS); }
					}
					else { Messenger.send(sender, Messages.INFO_GROUP_EXISTS); }
				}
				else {  Messenger.send(sender, Messages.INFO_ENTER_GROUP_NAME); }
			}
			//else {  Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
}