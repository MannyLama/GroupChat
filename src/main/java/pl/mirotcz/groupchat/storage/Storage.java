package pl.mirotcz.groupchat.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pl.mirotcz.groupchat.Group;

public interface Storage {
	
	void setupTables();
	
	void updateGroupInStorage(Group group);
	
	Group getGroupFromStorage(UUID groupID);
	
	boolean groupExistsInStorage(UUID groupID);
	
	void setPlayerCurrentGroupInStorage(UUID playerID, UUID groupID);
	
	UUID getPlayerCurrentGroupFromStorage(UUID playerID);
	
	List<UUID> getPlayerActiveMessagesGroupsFromStorage(UUID playerID);
	
	void setPlayerActiveMessagesGroupsFromStorage(UUID playerID, List<Group> groupsList);
	
	Map<UUID, Group> getPlayersCurrentGroupsFromStorage();
	
	Map<UUID, List<Group>> getPlayersActiveMessagesGroupsFromStorage();
	
	List<Group> loadGroupsFromStorage();
	
	void removeGroupFromStorage(UUID groupID);
	
	void removePlayerCurrentGroupFromStorage(UUID playerID);
	
	void close(Connection conn, PreparedStatement ps, ResultSet res);
	
	void closePool();

}
