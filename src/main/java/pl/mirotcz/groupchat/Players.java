package pl.mirotcz.groupchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Players {
	
	private static Map<UUID, Group> playersCurrentGroups = new HashMap<>();
	private static Map<UUID, List<Group>> playersActiveMessagesGroups = new HashMap<>();
	
	public static void loadPlayersCurrrentGroups() {
		playersCurrentGroups = GroupChat.getStorage().getPlayersCurrentGroupsFromStorage();
	}
	
	public static void loadPlayersActiveMessagesGroups() {
		playersActiveMessagesGroups = GroupChat.getStorage().getPlayersActiveMessagesGroupsFromStorage();
	}
	
	public static Group getPlayerCurrentGroup(UUID playerID) {
		if(playersCurrentGroups.containsKey(playerID)) {
			return playersCurrentGroups.get(playerID);
		}
		return null;
	}
	
	public static List<Group> getPlayerActiveMessagesGroups(UUID playerID) {
		if(playersActiveMessagesGroups.containsKey(playerID)) {
			return playersActiveMessagesGroups.get(playerID);
		}
		return new ArrayList<>();
	}
	
	public static void setPlayerCurrentGroup(UUID playerID, Group group) {
		playersCurrentGroups.put(playerID, group);
	}
	
	public static void setPlayerActiveMessagesGroups(UUID playerID, List<Group> groups) {
		playersActiveMessagesGroups.put(playerID, groups);
	}
	
	public static void addPlayerActiveMessagesGroup(UUID playerID, Group group) {
		List<Group> groups;
		if(!getPlayerActiveMessagesGroups(playerID).isEmpty()) {
			groups = getPlayerActiveMessagesGroups(playerID);
			if(!groups.contains(group)) {
				groups.add(group);
			}
		}
		else {
			groups = new ArrayList<>();
			groups.add(group);
		}
		setPlayerActiveMessagesGroups(playerID, groups);
	}
	
	public static void removePlayerActiveMessagesGroup(UUID playerID, Group group) {
		List<Group> groups = new ArrayList<>();
		if(getPlayerActiveMessagesGroups(playerID) != null) {
			groups = getPlayerActiveMessagesGroups(playerID);
			if(groups.contains(group)) {
				groups.remove(group);
			}
		}
		setPlayerActiveMessagesGroups(playerID, groups);
	}
	
	public static void removePlayerCurrentGroup(UUID playerID) {
		playersCurrentGroups.remove(playerID);
	}
	
	public static List<Group> getAllPlayerGroups(UUID playerID) {
		List<Group> groups = new ArrayList<>();
		for(Group group : Groups.getAllGroups()) {
			if(group.isMember(playerID)) {
				groups.add(group);
			}
		}
		return groups;
	}
}
