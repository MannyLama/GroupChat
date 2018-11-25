package pl.mirotcz.groupchat;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Permissions {
	

	public void setupPermissions() {
		Bukkit.getPluginManager().addPermission(new Permission("groupchat.admin", PermissionDefault.OP));
		Bukkit.getPluginManager().addPermission(new Permission("groupchat.user", PermissionDefault.TRUE));
	}
	
	public void unloadPermissions() {
		Bukkit.getPluginManager().removePermission("groupchat.admin");
		Bukkit.getPluginManager().removePermission("groupchat.user");
	}
	
	public boolean hasPermission(Permissible object, String permission) {
		return object.hasPermission(permission);
	}

}