package uk.co.jacekk.bukkit.simpleirc;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.v8.permissions.PluginPermission;

public class Permission {
	
	public static final PluginPermission GIVE_OP	= new PluginPermission("simpleirc.op.give", PermissionDefault.OP, "Allowed the player to give OP to other players");
	public static final PluginPermission TAKE_OP	= new PluginPermission("simpleirc.op.take", PermissionDefault.OP, "Allowed the player to take OP from other players");
	
}
