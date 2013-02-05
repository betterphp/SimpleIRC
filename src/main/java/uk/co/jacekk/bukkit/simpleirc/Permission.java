package uk.co.jacekk.bukkit.simpleirc;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.v9.permissions.PluginPermission;

public class Permission {
	
	public static final PluginPermission GIVE_OP	= new PluginPermission("simpleirc.op.give", PermissionDefault.OP, "Allowes the player to give OP to other players");
	public static final PluginPermission TAKE_OP	= new PluginPermission("simpleirc.op.take", PermissionDefault.OP, "Allowes the player to take OP from other players");
	
	public static final PluginPermission KICK		= new PluginPermission("simpleirc.channel.kick", PermissionDefault.OP, "Allowes the player to kick IRC users");
	public static final PluginPermission BAN		= new PluginPermission("simpleirc.channel.ban", PermissionDefault.OP, "Allowes the player to ban IRC users");
	public static final PluginPermission OP			= new PluginPermission("simpleirc.channel.op", PermissionDefault.OP, "Allowes the player to op IRC users");
	public static final PluginPermission VOICE		= new PluginPermission("simpleirc.channel.voice", PermissionDefault.OP, "Allowes the player to deop IRC users");
	
}
