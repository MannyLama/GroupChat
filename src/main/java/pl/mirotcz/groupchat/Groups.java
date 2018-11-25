package pl.mirotcz.groupchat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Groups {
	
	private static List<Group> groups = new ArrayList<>();
	private static List<List<String>> groupsListPages = new ArrayList<>();
	
	public static List<Group> getAllGroups() {
		return groups;
	}
	
	public static List<List<String>> getAllGroupsChatList() {
		return groupsListPages;
	}
	
	public static void loadGroups() {
		groups = GroupChat.getStorage().loadGroupsFromStorage();
	}
	
	public static void loadGroupsChatList() {
		groupsListPages = new ArrayList<>();
		List<String> page = new ArrayList<>();
		for(int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			String line = Messages.GROUP_DISPLAY_LIST_FORMAT
			.replaceAll(Messages.PLUGIN_PREFIX, "")
			.replaceAll("<number>", String.valueOf(i+1))
			.replaceAll("<group>", group.getName())
			.replaceAll("<type>", group.getTypeFormatted())
			.replaceAll("<memberscount>", String.valueOf(group.getMembers().size()));
			page.add(line);
			if(((i+1) % 10 == 0) || (i+1 == groups.size())) {
				groupsListPages.add(page);
				page = new ArrayList<>();
			}
		}
	}
	
	public static boolean groupExists(String groupName) {
		return groups.stream().anyMatch(x -> x.getName().equalsIgnoreCase(groupName));
	}
	
	public static Group getGroup(String groupName) {
		for(Group group : groups) {
			if(group.getName().equalsIgnoreCase(groupName)) {
				return group;
			}
		}
		return null;
	}
	
	public static Group getGroup(UUID groupID) {
		for(Group group : groups) {
			if(group.getID().equals(groupID)) {
				return group;
			}
		}
		return null;
	}
	
	public static Group getGroup(int groupNumber) {
		if(groups.size() >= groupNumber) {
			return groups.get(groupNumber - 1);
		}
		return null;
	}
	
	public static boolean addGroup(Group group) {
		return groups.add(group);
	}
	
	public static boolean removeGroup(UUID groupID) {
		Iterator<Group> it = groups.iterator();
		while(it.hasNext()) {
			Group group = it.next();
			if(group.getID() == groupID) {
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	public static List<Group> getGroupsOwnedByPlayer(UUID playerUUID) {
		List<Group> ownedGroups = new ArrayList<Group>();
		for(Group group : groups) {
			if(group.getOwner().equals(playerUUID)) {
				ownedGroups.add(group);
			}
		}
		return ownedGroups;
	}

}
