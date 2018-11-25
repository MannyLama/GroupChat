package pl.mirotcz.groupchat;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GroupUtils {
	
	public static void sendGroupInfo(CommandSender sender, Group group) {
		List<String> members = group.getMemberNames();
		StringBuilder membersList = new StringBuilder();
		Iterator<String> it = members.iterator();
		while(it.hasNext()) {
			String member = it.next();
			membersList.append(ChatColor.GREEN + member);
			if(it.hasNext()) { membersList.append(ChatColor.WHITE + ", "); }
		}
		Messenger.send(sender, Messages.INFO_GROUP_NAME.replaceAll("<name>", group.getName()));
		Messenger.send(sender, Messages.INFO_GROUP_TYPE.replaceAll("<type>", group.getTypeFormatted()));
		Messenger.send(sender, Messages.INFO_GROUP_OWNER.replaceAll("<owner>", group.getOwnerName()));
		Messenger.send(sender, Messages.INFO_GROUP_SIZE.replaceAll("<memberscount>", String.valueOf(members.size())));
		Messenger.send(sender, Messages.INFO_GROUP_MEMBERS.replaceAll("<members>", membersList.toString()));
	}
}