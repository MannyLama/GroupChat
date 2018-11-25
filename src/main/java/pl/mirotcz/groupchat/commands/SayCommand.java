package pl.mirotcz.groupchat.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;

public class SayCommand {

	public SayCommand(CommandSender sender, String[] args) {
		if(sender instanceof Player) {
			Player pl = (Player) sender;
			if(GroupChat.getPermissions().hasPermission(pl, "groupchat.user")) {
				if(Players.getPlayerCurrentGroup(pl.getUniqueId()) != null) {
					StringBuilder message = new StringBuilder();
					for(int i = 1; i < args.length; i++) {
						message.append(args[i]);
						if(i + 1 != args.length) {
							message.append(" ");
						}
					}
					String msg = message.toString();
					Group group = Players.getPlayerCurrentGroup(pl.getUniqueId());
					if(!msg.isEmpty()) {
						if(Players.getPlayerActiveMessagesGroups(pl.getUniqueId()).contains(group)) {
							group.messageAll(pl, Messages.MESSAGE_FORMAT.replaceAll("<player>", sender.getName()).replaceAll("<message>", msg));
						}
						else { Messenger.send(sender, Messages.INFO_GROUP_NOT_ACTIVE.replaceAll("<group>", group.getName())); }
					}
					else { Messenger.send(sender, Messages.INFO_SAY_EMPTY); }
				}
				else { Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER); }
			}
			else { Messenger.send(sender, Messages.INFO_NO_PERMISSION); }
		}
		else { Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER); }
	}
}