package pl.mirotcz.groupchat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.GroupType;
import pl.mirotcz.groupchat.Groups;
import pl.mirotcz.groupchat.Invitations;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;
import pl.mirotcz.groupchat.Settings;
import pl.mirotcz.groupchat.Utils;

public class JoinCommand {

	public JoinCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Group group;
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.user")) {
				if(args.length == 2) {
					if(Utils.isInteger(args[1])) {
						int number = Integer.valueOf(args[1]);
						if(Groups.getGroup(number) != null) {
							group = Groups.getGroup(number);
							if(group.getType() == GroupType.PUBLIC) {
								if(!group.isMember(pl)) {
									if(!group.isBanned(pl)) {
										processJoin(pl, group);
									}
									else { Messenger.send(sender, Messages.INFO_YOU_ARE_BANNED); }
								}
								else { Messenger.send(sender, Messages.INFO_YOU_MEMBER); }
							}
							else { Messenger.send(sender, Messages.INFO_GROUP_PRIVATE_NEED_INVITATION); }
						}
						else { Messenger.send(sender, Messages.INFO_GROUP_NOT_EXIST); }
					}
					else { Messenger.send(sender, Messages.INFO_TYPE_NUMBER); }
				}
				else {
					if(Invitations.getGroupInvitation(pl) != null) {
						if(Groups.getGroup(Invitations.getGroupInvitation(pl)) != null) {
							group = Groups.getGroup(Invitations.getGroupInvitation(pl));
							if(!group.isMember(pl)) {
								if(!group.isBanned(pl)) {
									processJoin(pl, group);
								}
								else { Messenger.send(sender, Messages.INFO_YOU_ARE_BANNED); }
							}
							else { Messenger.send(sender, Messages.INFO_YOU_MEMBER); }
						}
						else { Messenger.send(sender, Messages.INFO_GROUP_NOT_EXIST); }
					}
					else { Messenger.send(sender, Messages.INFO_NOT_INVITED); }
				}
			}
			else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
	
	private void processJoin(Player pl, Group group) {
		if(Players.getAllPlayerGroups(pl.getUniqueId()).size() == Settings.MAX_MEMBER_GROUPS) {
			Messenger.send(pl, Messages.INFO_MAX_MEMBER_GROUPS.replaceAll("<n>", String.valueOf(Settings.MAX_MEMBER_GROUPS)));
			return;
		}
		group.messageAll(Bukkit.getConsoleSender(), Messages.INFO_PLAYER_JOINED.replaceAll("<player>", pl.getName()));
		Messenger.send(pl, Messages.INFO_YOU_JOINED.replaceAll("<group>", group.getName()));
		group.addMember(pl);
		GroupChat.getStorage().setPlayerCurrentGroupInStorage(pl.getUniqueId(), group.getID());
		GroupChat.getStorage().updateGroupInStorage(group);
		Groups.loadGroupsChatList();
		Players.setPlayerCurrentGroup(pl.getUniqueId(), group);
		Players.addPlayerActiveMessagesGroup(pl.getUniqueId(), group);
		GroupChat.getStorage().setPlayerActiveMessagesGroupsFromStorage(pl.getUniqueId(), Players.getPlayerActiveMessagesGroups(pl.getUniqueId()));
	}
}