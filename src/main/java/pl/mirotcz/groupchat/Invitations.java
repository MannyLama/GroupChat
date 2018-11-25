package pl.mirotcz.groupchat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Invitations {
	
	private static Map<Player, UUID> invitations = new HashMap<>();
	
	public static UUID getGroupInvitation(Player player) {
		if(invitations.containsKey(player)) {
			return invitations.get(player);
		}
		return null;
	}
	
	public static void addInvitation(Player player, UUID invitingGroup) {
		invitations.put(player, invitingGroup);
	}
}