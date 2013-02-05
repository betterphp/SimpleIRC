package uk.co.jacekk.bukkit.simpleirc;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.v9.permissions.PluginPermission;

public class Permission {
	
	public static final PluginPermission GIVE_OP		= new PluginPermission("simpleirc.op.give", PermissionDefault.OP, "Allowes the player to give OP to other players");
	public static final PluginPermission TAKE_OP		= new PluginPermission("simpleirc.op.take", PermissionDefault.OP, "Allowes the player to take OP from other players");
	public static final PluginPermission MANAGE_CHANNEL	= new PluginPermission("simpleirc.channel.manage", PermissionDefault.OP, "Allowes the player to manage the IRC channel using the /irc command");
	
}
