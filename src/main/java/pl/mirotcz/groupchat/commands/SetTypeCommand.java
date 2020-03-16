package pl.mirotcz.groupchat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.GroupType;
import pl.mirotcz.groupchat.Groups;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;
import pl.mirotcz.groupchat.Utils;

public class SetTypeCommand {
	
	public SetTypeCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.admin")) {
				if(args.length == 2) {
					if(Players.getPlayerCurrentGroup(pl.getUniqueId()) != null) {
						Group group = Players.getPlayerCurrentGroup(pl.getUniqueId());
						if(group.getOwner().equals(pl.getUniqueId())) {
							if(Utils.isInteger(args[1])) {
								int number = Integer.valueOf(args[1]);
								if(number == 0) {
									group.setType(GroupType.PUBLIC);
									group.messageAll(Bukkit.getConsoleSender(), Messages.INFO_TYPE_SET.replaceAll("<type>", Messages.GROUP_TYPE_PUBLIC));
								}
								else if(number == 1) {
									group.setType(GroupType.PRIVATE);
									group.messageAll(Bukkit.getConsoleSender(), Messages.INFO_TYPE_SET.replaceAll("<type>", Messages.GROUP_TYPE_PRIVATE));
								}
								else { new HelpCommand(sender); return; }
								GroupChat.getStorage().updateGroupInStorage(group);
								Groups.loadGroupsChatList();
							}
							else { Messenger.send(sender, Messages.INFO_TYPE_NUMBER); }
						}
						else { Messenger.send(sender, Messages.INFO_YOU_NOT_OWNER); }
					}
					else { Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER); }
				}
				else { Messenger.send(sender, Messages.INFO_TYPE_NUMBER); }
			}
			//else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
}