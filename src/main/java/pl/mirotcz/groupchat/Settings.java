package pl.mirotcz.groupchat;

public class Settings {
	
	public static int MAX_OWNED_GROUPS;
	public static int MAX_MEMBER_GROUPS;
	
	public static boolean COMMAND_SAY_ALIAS_ENABLED;
	public static String COMMAND_SAY_ALIAS;
	
	public static boolean SOUND_ENABLED;
	public static String SOUND_NAME;
	public static float SOUND_VOL;
	public static float SOUND_PITCH;
	
	public static String STORAGE_TYPE;
	
	public static String DB_HOST;
	public static String DB_NAME;
	public static String DB_USER;
	public static String DB_PASS;
	public static int DB_PORT;
	
	public static void loadSettings() {
		ConfigManager config = GroupChat.getMainConfigManager();
		
		MAX_OWNED_GROUPS = config.getConfig().getInt("MaxOwnedGroups");
		MAX_MEMBER_GROUPS = config.getConfig().getInt("MaxMemberGroups");
		
		COMMAND_SAY_ALIAS_ENABLED = config.getConfig().getBoolean("SayCommandAlias.enabled");
		COMMAND_SAY_ALIAS = config.getConfig().getString("SayCommandAlias.alias");
		
		SOUND_ENABLED = config.getConfig().getBoolean("Sound.enabled");
		SOUND_NAME = config.getConfig().getString("Sound.SoundName");
		SOUND_VOL = (float) config.getConfig().getDouble("Sound.Volume");
		SOUND_PITCH = (float) config.getConfig().getDouble("Sound.Pitch");
		
		STORAGE_TYPE = config.getConfig().getString("Storage.type");
		
		DB_HOST = config.getConfig().getString("Storage.hostname");
		DB_NAME = config.getConfig().getString("Storage.database");
		DB_USER = config.getConfig().getString("Storage.user");
		DB_PASS = config.getConfig().getString("Storage.password");
		DB_PORT = config.getConfig().getInt("Storage.port");
	}
}