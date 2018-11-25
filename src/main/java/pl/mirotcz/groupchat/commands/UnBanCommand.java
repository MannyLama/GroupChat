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
import pl.mirotcz.groupchat.Utils;

public class UnBanCommand {
	
	public UnBanCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.user")) {
				if(Players.getPlayerCurrentGroup(pl.getUniqueId()) != null) {
					Group group = Players.getPlayerCurrentGroup(pl.getUniqueId());
					if(group.getOwner().equals(pl.getUniqueId())) {
						if(args.length == 2) {
							if(!pl.getName().equalsIgnoreCase(args[1])) {
								UUID unbanned = Utils.getPlayerId(args[1]);
								if(unbanned != null) {
									if(group.unBan(unbanned)) {
										if(Bukkit.getPlayer(unbanned) != null) {
											Messenger.send(Bukkit.getPlayer(unbanned), Messages.INFO_YOU_HAVE_BEEN_UNBANNED.replaceAll("<group>", group.getName()));
										}
										group.messageAll(Bukkit.getConsoleSender(), Messages.INFO_PLAYER_HAS_BEEN_UNBANNED.replaceAll("<player>", args[1]));
										GroupChat.getStorage().updateGroupInStorage(group);
										Groups.loadGroupsChatList();
									}
									else { Messenger.send(sender, Messages.INFO_PLAYER_NOT_BANNED.replaceAll("<player>", args[1])); }
								}
								else { Messenger.send(sender, Messages.INFO_PLAYER_NOT_FOUND); }
							}
							else { Messenger.send(sender, Messages.INFO_OWNER_CANNOT_LEAVE); }
						}
						else { Messenger.send(sender, Messages.INFO_ENTER_PLAYER_NAME); }
					}
					else { Messenger.send(sender, Messages.INFO_YOU_NOT_OWNER); }
				}
				else { Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER); }
			}
			else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
}