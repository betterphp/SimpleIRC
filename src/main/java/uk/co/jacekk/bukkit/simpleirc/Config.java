package uk.co.jacekk.bukkit.simpleirc;

import java.util.Arrays;

import uk.co.jacekk.bukkit.baseplugin.v3.config.PluginConfigKey;

public enum Config implements PluginConfigKey {
	
	IRC_SERVER_ADDRESS(		"irc.server.address",		"irc.esper.net"),
	IRC_SERVER_PORT(		"irc.server.port",			6667),
	IRC_SERVER_PASSWORD(	"irc.server.password",		""),
	IRC_BOT_NICK(			"irc.bot.nick",				"MCBot"),
	IRC_BOT_PASSWORD(		"irc.bot.password",			"nickserv_password"),
	IRC_BOT_CHANNELS(		"irc.bot.channels",			Arrays.asList("#something", "#somethingelse")),
	IRC_BOT_VERBOSE(		"irc.bot.verbose",			false),
	;
	
	private String key;
	private Object defaultValue;
	
	private Config(String key, Object defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public Object getDefault(){
		return this.defaultValue;
	}
	
}
