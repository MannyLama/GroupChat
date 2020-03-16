package pl.mirotcz.groupchat.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;
import pl.mirotcz.groupchat.Utils;

public class SetOwnerCommand {
	
	public SetOwnerCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.admin")) {
				if(args.length == 2) {
					if(Players.getPlayerCurrentGroup(pl.getUniqueId()) != null) {
						Group group = Players.getPlayerCurrentGroup(pl.getUniqueId());
						if(group.getOwner().equals(pl.getUniqueId())) {
							String newOwner = args[1];
							UUID newOwnerId = Utils.getPlayerId(newOwner);
							if(newOwnerId != null) {
								if(group.isMember(newOwnerId)) {
									if(!newOwnerId.equals(group.getOwner())) {
										group.setOwner(newOwnerId);
										GroupChat.getStorage().updateGroupInStorage(group);
										group.messageAll(Bukkit.getConsoleSender(), Messages.INFO_OWNER_SET.replaceAll("<player>", newOwner));
									}
									else { Messenger.send(sender, Messages.INFO_PLAYER_ALREADY_OWNER); }
								}
								else { Messenger.send(sender, Messages.INFO_PLAYER_NOT_IN_GROUP); }
							}
							else { Messenger.send(sender, Messages.INFO_PLAYER_NOT_FOUND); }
						}
						else { Messenger.send(sender, Messages.INFO_YOU_NOT_OWNER); }
					}
					else { Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER); }
				}
				else { Messenger.send(sender, Messages.INFO_ENTER_PLAYER_NAME); }
			}
			//else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
}