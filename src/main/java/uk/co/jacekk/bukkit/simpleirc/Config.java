package uk.co.jacekk.bukkit.simpleirc;

import java.util.Arrays;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfigKey;

public class Config {
	
	public static final PluginConfigKey IRC_SERVER_ADDRESS		= new PluginConfigKey("irc.server.address",		"irc.esper.net");
	public static final PluginConfigKey IRC_SERVER_PORT			= new PluginConfigKey("irc.server.port",		6667);
	public static final PluginConfigKey IRC_SERVER_PASSWORD		= new PluginConfigKey("irc.server.password",	"");
	public static final PluginConfigKey IRC_BOT_NICK			= new PluginConfigKey("irc.bot.nick",			"MCBot");
	public static final PluginConfigKey IRC_BOT_PASSWORD		= new PluginConfigKey("irc.bot.password",		"nickserv_password");
	public static final PluginConfigKey IRC_BOT_CHANNELS		= new PluginConfigKey("irc.bot.channels",		Arrays.asList("#something", "#somethingelse"));
	public static final PluginConfigKey IRC_BOT_VERBOSE			= new PluginConfigKey("irc.bot.verbose",		false);
	public static final PluginConfigKey IRC_BOT_PREVENT_PINGS	= new PluginConfigKey("irc.bot.prevent-pings",	true);
	
}
