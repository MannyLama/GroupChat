package pl.mirotcz.groupchat;

import java.lang.reflect.Field;

public class Messages {

	public static String PLUGIN_PREFIX;
	public static String GROUP_PREFIX;
	public static String MESSAGE_FORMAT;
	public static String GROUP_DISPLAY_LIST_FORMAT;
	public static String GROUP_TYPE_PUBLIC;
	public static String GROUP_TYPE_PRIVATE;
	
	public static String HELP_COMMAND_CREATE;
	public static String HELP_COMMAND_INVITE;
	public static String HELP_COMMAND_KICK;
	public static String HELP_COMMAND_BAN;
	public static String HELP_COMMAND_DELETE;
	public static String HELP_COMMAND_LEAVE;
	public static String HELP_COMMAND_SAY;
	public static String HELP_COMMAND_JOIN;
	public static String HELP_COMMAND_INFO;
	public static String HELP_COMMAND_RELOAD;
	public static String HELP_COMMAND_LIST;
	public static String HELP_COMMAND_SWITCH;
	public static String HELP_COMMAND_SHOW;
	public static String HELP_COMMAND_MY_GROUPS;
	public static String HELP_COMMAND_SET_OWNER;
	public static String HELP_COMMAND_SET_TYPE;
	
	public static String INFO_ENTER_GROUP_NAME;
	public static String INFO_ENTER_PLAYER_NAME;
	public static String INFO_YOU_ALREADY_MEMBER;
	public static String INFO_PLAYER_ALREADY_OWNER;
	public static String INFO_YOU_ALREADY_MEMBER2;
	public static String INFO_PLAYER_ALREADY_MEMBER;
	public static String INFO_YOU_NOT_MEMBER;
	public static String INFO_YOU_NOT_OWNER;
	public static String INFO_YOU_MEMBER;
	public static String INFO_OWNER_CANNOT_LEAVE;
	public static String INFO_GROUP_CREATED;
	public static String INFO_GROUP_NOT_EXIST;
	public static String INFO_GROUP_EXISTS;
	public static String INFO_GROUP_DELETED;
	public static String INFO_OWNER_DELETED;
	public static String INFO_GROUP_NOT_ACTIVE;
	public static String INFO_PLAYER_NOT_ONLINE;
	public static String INFO_PLAYER_NOT_FOUND;
	public static String INFO_PLAYER_IN_GROUP;
	public static String INFO_PLAYER_NOT_IN_GROUP;
	public static String INFO_PLAYER_INVITED;
	public static String INFO_INVITATION_GET;
	public static String INFO_PLAYER_JOINED;
	public static String INFO_YOU_JOINED;
	public static String INFO_YOU_LEFT_GROUP;
	public static String INFO_PLAYER_LEFT_GROUP;
	public static String INFO_PLAYER_KICKED;
	public static String INFO_YOU_KICKED;
	public static String INFO_YOU_HAVE_BEEN_BANNED;
	public static String INFO_YOU_HAVE_BEEN_UNBANNED;
	public static String INFO_YOU_ARE_BANNED;
	public static String INFO_PLAYER_IS_BANNED;
	public static String INFO_PLAYER_NOT_BANNED;
	public static String INFO_PLAYER_HAS_BEEN_BANNED;
	public static String INFO_PLAYER_HAS_BEEN_UNBANNED;
	public static String INFO_MAX_OWNED_GROUPS;
	public static String INFO_MAX_MEMBER_GROUPS;
	public static String INFO_GROUP_INFO;
	public static String INFO_GROUP_NAME;
	public static String INFO_GROUP_OWNER;
	public static String INFO_GROUP_SIZE;
	public static String INFO_GROUP_TYPE;
	public static String INFO_GROUP_MEMBERS;
	public static String INFO_NO_PERMISSION;
	public static String INFO_YOU_NOT_PLAYER;
	public static String INFO_NOT_INVITED_THIS_GROUP;
	public static String INFO_NOT_INVITED;
	public static String INFO_NO_GROUPS;
	public static String INFO_NO_PAGE;
	public static String INFO_NO_GROUP_NUMBER;
	public static String INFO_ALREADY_CURRENT_GROUP;
	public static String INFO_YOU_GROUP_SWITCHED;
	public static String INFO_YOU_GROUP_SHOW_ON;
	public static String INFO_YOU_GROUP_SHOW_OFF;
	public static String INFO_PLAYER_GROUP_SWITCHED;
	public static String INFO_TYPE_NUMBER;;
	public static String INFO_GROUP_PRIVATE_NEED_INVITATION;
	public static String INFO_OWNER_SET;
	public static String INFO_TYPE_SET;
	public static String INFO_SAY_EMPTY;
	public static String INFO_PLUGIN_RELOADED;
	public static String INFO_ERROR;
	
	public static void loadMessages() {
		ConfigManager lang = GroupChat.getLangConfigManager();
		Field[] fields = Messages.class.getDeclaredFields();
		for(Field field : fields) {
			if(field.getType().equals(String.class)) {
				String name = field.getName();
				try {
					field.set(null, lang.getConfig().getString(name));
				} 
				catch (IllegalArgumentException e) { e.printStackTrace(); }
				catch (IllegalAccessException e) { e.printStackTrace(); }
			}
		}
	}
}