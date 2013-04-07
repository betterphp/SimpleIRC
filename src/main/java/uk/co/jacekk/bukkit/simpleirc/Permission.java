package uk.co.jacekk.bukkit.simpleirc;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.permissions.PluginPermission;

public class Permission {
	
	public static final PluginPermission GIVE_OP	= new PluginPermission("simpleirc.op.give", PermissionDefault.OP, "Allowes the player to give OP to other players");
	public static final PluginPermission TAKE_OP	= new PluginPermission("simpleirc.op.take", PermissionDefault.OP, "Allowes the player to take OP from other players");
	
	public static final PluginPermission KICK		= new PluginPermission("simpleirc.channel.kick", PermissionDefault.OP, "Allows the player to kick IRC users");
	public static final PluginPermission BAN		= new PluginPermission("simpleirc.channel.ban", PermissionDefault.OP, "Allows the player to ban IRC users");
	public static final PluginPermission OP			= new PluginPermission("simpleirc.channel.op", PermissionDefault.OP, "Allows the player to op IRC users");
	public static final PluginPermission VOICE		= new PluginPermission("simpleirc.channel.voice", PermissionDefault.OP, "Allows the player to deop IRC users");
	
	public static final PluginPermission LEAVE		= new PluginPermission("simpleirc.bot.leave", PermissionDefault.OP, "Allows the player have the bot leave channels");
	public static final PluginPermission JOIN		= new PluginPermission("simpleirc.bot.join", PermissionDefault.OP, "Allows the player have the bot join channels");
	public static final PluginPermission DISCONNECT	= new PluginPermission("simpleirc.bot.disconnect", PermissionDefault.OP, "Allows the player have the bot disconnect");
	public static final PluginPermission CONNECT	= new PluginPermission("simpleirc.bot.connect", PermissionDefault.OP, "Allows the player have the bot connect");
	
}
