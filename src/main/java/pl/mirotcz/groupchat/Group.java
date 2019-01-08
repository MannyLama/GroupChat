package pl.mirotcz.groupchat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.GroupType;

public class Group {
	
	private String groupName;
	private String groupDescription = "";
	private Set<UUID> groupMembers;
	private Set<UUID> groupBannedPlayers;
	private UUID groupOwner, groupID;
	private GroupType groupType; 
	
	public Group(UUID creator, String groupName) {
		this.groupOwner = creator;
		this.groupName = groupName;
		this.groupMembers = new HashSet<>();
		this.groupBannedPlayers = new HashSet<>();
		this.groupMembers.add(creator);
		this.groupType = GroupType.PUBLIC;
		do {
			this.groupID = UUID.randomUUID();
		}
		while(GroupChat.getStorage().groupExistsInStorage(this.groupID));
	}
	
	public UUID getID() {
		return this.groupID;
	}
	
	public String getName() {
		return this.groupName;
	}
	
	
	public String getDescription() {
		return this.groupDescription;
	}
	
	public UUID getOwner() {
		return this.groupOwner;
	}
	
	public String getOwnerName() {
		if(Bukkit.getOfflinePlayer(this.groupOwner) != null) {
			OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(this.groupOwner);
			if(offPlayer.getName() != null) {
				return offPlayer.getName();
			}
		}
		return "unknown";
	}
	
	public Set<UUID> getMembers() {
		return this.groupMembers;
	}
	
	public Set<UUID> getBannedPlayers() {
		return this.groupBannedPlayers;
	}
	
	public List<String> getMemberNames() {
		List<String> memberNames = new ArrayList<>();
		for(UUID member : this.groupMembers) {
			if(Bukkit.getOfflinePlayer(member) != null) {
				OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(member);
				if(offPlayer.getName() != null) {
					memberNames.add(offPlayer.getName());
				}
			}
		}
		return memberNames;
	}
	
	public GroupType getType() {
		return groupType;
	}
	
	public String getTypeFormatted() {
		switch(this.getType()) {
			case PUBLIC:
				return Messages.GROUP_TYPE_PUBLIC;
			case PRIVATE:
				return Messages.GROUP_TYPE_PRIVATE;
			default:
				return Messages.INFO_ERROR;
		}
	}
	
	public void setMembers(Set<UUID> members) {
		this.groupMembers = members;
	}
	
	public void setBannedPlayers(Set<UUID> banned) {
		this.groupBannedPlayers = banned;
	}
	
	public void setName(String newName) {
		this.groupName = newName;
	}
	
	public void setDescription(String newDescription) {
		this.groupDescription = newDescription;
	}
	
	public void setID(UUID newId) {
		this.groupID = newId;
	}
	
	public void setType(GroupType type) {
		this.groupType = type;
	}
	
	public boolean isMember(Player player) {
		return this.groupMembers.contains(player.getUniqueId());
	}
	
	public boolean isBanned(Player player) {
		return this.groupBannedPlayers.contains(player.getUniqueId());
	}
	
	
	public boolean isMember(UUID id) {
		return this.groupMembers.contains(id);
	}
	
	public UUID getMember(String playerName) {
		for(UUID groupMember : this.groupMembers) {
			if(Bukkit.getPlayer(groupMember) != null) {
				if(Bukkit.getPlayer(groupMember).getName().equalsIgnoreCase(playerName)) {
					return groupMember;
				}
			}
			else if(Bukkit.getOfflinePlayer(groupMember) != null) {
				if(Bukkit.getOfflinePlayer(groupMember).getName().equalsIgnoreCase(playerName)) {
					return groupMember;
				}
			}
		}
		return null;
	}
	
	public boolean addMember(Player player) {
		return this.groupMembers.add(player.getUniqueId());
	}
	
	public boolean ban(UUID id) {
		return this.groupBannedPlayers.add(id);
	}
	
	public boolean unBan(UUID id) {
		return this.groupBannedPlayers.remove(id);
	}
	
	public boolean removeMember(Player player) {
		return this.groupMembers.remove(player.getUniqueId());
	}
	
	public boolean removeMember(UUID id) {
		return this.groupMembers.remove(id);
	}
	
	public void setOwner(UUID id) {
		this.groupOwner = id;
	}
	
	public boolean removeMember(String playerName) {
		for(UUID groupMember : this.groupMembers) {
			if(Bukkit.getPlayer(groupMember) != null) {
				if(Bukkit.getPlayer(groupMember).getName().equalsIgnoreCase(playerName)) {
					return this.groupMembers.remove(groupMember);
				}
			}
			else if(Bukkit.getOfflinePlayer(groupMember) != null) {
				if(Bukkit.getOfflinePlayer(groupMember).getName().equalsIgnoreCase(playerName)) {
					return this.groupMembers.remove(groupMember);
				}
			}
		}
		return false;
	}
	
	public void messageAll(CommandSender sender, String message) {
		for(UUID groupMember : this.groupMembers) {
			if(Players.getPlayerActiveMessagesGroups(groupMember).contains(this)) {
				if(Bukkit.getPlayer(groupMember) != null) {
					Player pl = Bukkit.getPlayer(groupMember);
					Messenger.sendCustomPrefix(pl, Messages.GROUP_PREFIX.replaceAll("<group>", this.getName()), message);
					if(pl != sender) {
						if(Settings.SOUND_ENABLED) {
							Messenger.sendConsole("Sound name: " + Settings.SOUND_NAME);
							pl.playSound(pl.getLocation(), Sound.valueOf(Settings.SOUND_NAME), SoundCategory.valueOf("PLAYERS"), Settings.SOUND_VOL, Settings.SOUND_PITCH);
						}
					}
				}
			}
		}
	}

}
