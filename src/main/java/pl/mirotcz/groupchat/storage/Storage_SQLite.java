package pl.mirotcz.groupchat.storage;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Set;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupType;
import pl.mirotcz.groupchat.Groups;
import pl.mirotcz.groupchat.Messenger;

public class Storage_SQLite implements Storage{
	
	HikariConfig config;
	HikariDataSource ds;

	String url;
	volatile Connection conn;
	String sql = "CREATE TABLE IF NOT EXISTS groupchat_groups (\n"
			+ " id integer PRIMARY KEY,\n"
			+ " group_id text NOT NULL,\n"
            + "	group_name text NOT NULL,\n"
			+ " group_type text NOT NULL,\n"
            + " group_description text,\n"
            + "	group_owner text NOT NULL,\n"
            + " group_members text NOT NULL,\n"
            + " banned_players text\n"
            + ");";
	String sql2 = "CREATE TABLE IF NOT EXISTS groupchat_players (\n"
			+ " player_id text NOT NULL,\n"
            + "	current_group_id text,\n"
			+ " active_messages_groups TEXT\n"
            + ");";
	public Storage_SQLite() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			Messenger.sendConsole(e1.getMessage());
		}
		
		
		config = new HikariConfig();
		config.setPoolName("GroupChatSQLitePool");
		
		
		config.setDriverClassName("org.sqlite.JDBC");
		config.setJdbcUrl("jdbc:sqlite:plugins/GroupChat/Storage.db");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", true);
		config.addDataSourceProperty("characterEncoding","utf8");
		config.addDataSourceProperty("useUnicode", true);
		config.setLeakDetectionThreshold(10 * 1000);
		ds = new HikariDataSource(config);
		
		setupTables();
	}
	
	public void setupTables() {
		Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps = conn.prepareStatement(sql2);
            ps.executeUpdate();
            close(conn, ps, null);
        } catch (SQLException e) {
        	close(conn, ps, null);
            e.printStackTrace();
        }
	}
	
	public void updateGroupInStorage(Group group) {
		String sql;
		if(groupExistsInStorage(group.getID())) {
			sql = "UPDATE groupchat_groups SET group_name = ?, group_owner = ?, group_members = ?, group_description = ?, group_type = ?, banned_players = ? WHERE group_id = ?";
		}
		else {
			sql = "INSERT INTO groupchat_groups (group_name, group_owner, group_members, group_description, group_type, banned_players, group_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
		}
		Connection conn = null;
        PreparedStatement ps = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, group.getName());
        	ps.setString(2, group.getOwner().toString());
        	ps.setString(3, group.getMembers().toString());
        	ps.setString(4, group.getDescription());
        	ps.setString(5, group.getType().toString());
        	ps.setString(6, group.getBannedPlayers().toString());
        	ps.setString(7, group.getID().toString());
        	ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, null);
        }
	}
	
	public Group getGroupFromStorage(UUID groupID) {
		Group group = null;
		String sql = "SELECT * FROM groupchat_groups WHERE group_id = ?";
		Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // Group data
        String groupName = "";
        String groupDescription = "";
        UUID groupOwner;
        GroupType groupType;
        Set<UUID> groupMembers = new HashSet<>();
        Set<UUID> groupBannedPlayers = new HashSet<>();
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, groupID.toString());
        	rs = ps.executeQuery();
        	if(rs.isBeforeFirst()) {
        		rs.next();
        		groupName = rs.getString("group_name");
        		groupDescription = rs.getString("group_description");
        		groupOwner = UUID.fromString(rs.getString("group_owner"));
        		groupType = GroupType.valueOf(rs.getString("group_type"));
        		String[] membersList = rs.getString("group_members").replace("[", "").replace("]", "").split(", ");
        		String[] bannedList = rs.getString("banned_players").replace("[", "").replace("]", "").split(", ");
        		for(String member : membersList) {
        			if(!member.isEmpty()) {
        				groupMembers.add(UUID.fromString(member));
        			}
        		}
        		for(String banned : bannedList) {
        			if(!banned.isEmpty()) {
        				groupBannedPlayers.add(UUID.fromString(banned));
        			}
        		}
        		
        		group = new Group(groupOwner, groupName);
        		group.setDescription(groupDescription);
        		group.setID(groupID);
        		group.setMembers(groupMembers);
        		group.setBannedPlayers(groupBannedPlayers);
        		group.setType(groupType);
        	}
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, rs);
        }
		
		return group;
	}
	
	public boolean groupExistsInStorage(UUID groupID) {
		String sql = "SELECT * FROM groupchat_groups WHERE group_id = ?";
		Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, groupID.toString());
        	rs = ps.executeQuery();
        	return rs.isBeforeFirst();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
        	close(conn, ps, rs);
        }
	}
	
	public void setPlayerCurrentGroupInStorage(UUID playerID, UUID groupID) {
		String sql;
		if(getPlayerCurrentGroupFromStorage(playerID) != null) {
			sql = "UPDATE groupchat_players SET current_group_id = ? WHERE player_id = ?";
		}
		else {
			sql = "INSERT INTO groupchat_players (current_group_id, player_id) VALUES (?, ?)";
		}
		Connection conn = null;
        PreparedStatement ps = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, groupID.toString());
        	ps.setString(2, playerID.toString());
        	ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, null);
        }
	}
	
	public UUID getPlayerCurrentGroupFromStorage(UUID playerID) {
		String sql = "SELECT * FROM groupchat_players WHERE player_id = ?";
		Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UUID groupID = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, playerID.toString());
        	rs = ps.executeQuery();
        	if(rs.isBeforeFirst()) {
        		rs.next();
        		groupID = UUID.fromString(rs.getString("current_group_id"));
        	}
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, rs);
        }
        return groupID;
	}
	
	public Map<UUID, Group> getPlayersCurrentGroupsFromStorage() {
		Map<UUID, Group> playersCurrentGroups = new HashMap<>();
		String sql = "SELECT * FROM groupchat_players";
		Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	rs = ps.executeQuery();
        	while(rs.next()) {
        		String player = rs.getString("player_id");
        		String group = rs.getString("current_group_id");
        		if(Groups.getGroup(UUID.fromString(group)) != null) {
        			playersCurrentGroups.put(UUID.fromString(player), Groups.getGroup(UUID.fromString(group)));
        		}
        		else {
        			System.out.println("Player: " + player + " Group: " + group);
        		}
        	}
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, rs);
        }
        return playersCurrentGroups;
	}
	
	public List<UUID> getPlayerActiveMessagesGroupsFromStorage(UUID playerID) {
		List<UUID> playerActiveMessagesGroups = new ArrayList<>();
		String sql = "SELECT * FROM groupchat_players WHERE player_id = ?";
		Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, playerID.toString());
        	rs = ps.executeQuery();
        	if(rs.isBeforeFirst()) {
        		rs.next();
        		if(rs.getString("active_messages_groups") != null) {
            		String[] groups = rs.getString("active_messages_groups").replace("[", "").replace("]", "").split(", ");
            		for(String groupID : groups) {
            			if(!groupID.isEmpty()) {
            				playerActiveMessagesGroups.add(UUID.fromString(groupID));
            			}
            		}
        		}
        	}
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, rs);
        }
        return playerActiveMessagesGroups;
	}
	
	public void setPlayerActiveMessagesGroupsFromStorage(UUID playerID, List<Group> groupsList) {
		String sql;
		List<UUID> groupsID = new ArrayList<>();
		groupsList.forEach( group -> { groupsID.add(group.getID()); });
		if(getPlayerActiveMessagesGroupsFromStorage(playerID) != null) {
			sql = "UPDATE groupchat_players SET active_messages_groups = ? WHERE player_id = ?";
		}
		else {
			sql = "INSERT INTO groupchat_players (active_messages_groups, player_id) VALUES (?, ?)";
		}
		Connection conn = null;
        PreparedStatement ps = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, groupsID.toString());
        	ps.setString(2, playerID.toString());
        	ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, null);
        }
	}

	public Map<UUID, List<Group>> getPlayersActiveMessagesGroupsFromStorage() {
		Map<UUID, List<Group>> playersActiveMessagesGroups = new HashMap<>();
		String sql = "SELECT * FROM groupchat_players";
		Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	rs = ps.executeQuery();
        	while(rs.next()) {
        		String player = rs.getString("player_id");
        		if(rs.getString("active_messages_groups") != null) {
            		String[] groups = rs.getString("active_messages_groups").replace("[", "").replace("]", "").split(", ");
            		List<Group> groupList = new ArrayList<>();
            		for(String groupID : groups) {
            			if(!groupID.isEmpty()) {
            				if(Groups.getGroup(UUID.fromString(groupID)) != null) {
            					groupList.add(Groups.getGroup(UUID.fromString(groupID)));
            				}
            			}
            		}
            		playersActiveMessagesGroups.put(UUID.fromString(player), groupList);
        		}
        	}
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, rs);
        }
        return playersActiveMessagesGroups;
	}
	
	public List<Group> loadGroupsFromStorage() {
		List<Group> groups = new ArrayList<>();
		String sql = "SELECT * FROM groupchat_groups ORDER BY id ASC";
		Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
        	while(rs.next()) {
        		Group group = getGroupFromStorage(UUID.fromString(rs.getString("group_id")));
        		groups.add(group);
        	}
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, null);
        }
        return groups;
	}
	
	public void removeGroupFromStorage(UUID groupID) {
		String sql = "DELETE FROM groupchat_groups WHERE group_id = ?";
		Connection conn = null;
        PreparedStatement ps = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, groupID.toString());
        	ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, null);
        }
	}
	
	public void removePlayerCurrentGroupFromStorage(UUID playerID) {
		String sql = "DELETE FROM groupchat_players WHERE player_id = ?";
		Connection conn = null;
        PreparedStatement ps = null;
        try {
        	conn = ds.getConnection();
        	ps = conn.prepareStatement(sql);
        	ps.setString(1, playerID.toString());
        	ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        	close(conn, ps, null);
        }
	}
	
	
	
    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }
    
	
    public void closePool() {
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }


	
}
