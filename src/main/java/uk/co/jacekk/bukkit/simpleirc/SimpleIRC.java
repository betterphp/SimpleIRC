package uk.co.jacekk.bukkit.simpleirc;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import uk.co.jacekk.bukkit.baseplugin.v8.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v8.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v8.storage.DataStore;
import uk.co.jacekk.bukkit.baseplugin.v8.storage.ListStore;

public class SimpleIRC extends BasePlugin {
	
	private DataStore aliasStore;
	
	protected HashMap<String, String> ircAliases;
	protected HashMap<String, String> gameAliases;
	
	protected ListStore ircOps;
	
	protected SimpleIRCBot bot;
	protected IRCCommandSender commandSender;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.class, this.log);
		this.aliasStore = new DataStore(new File(this.baseDirPath + File.separator + "aliases.txt"), true);
		this.aliasStore.load();
		
		this.ircAliases = new HashMap<String, String>(this.aliasStore.size());
		this.gameAliases = new HashMap<String, String>(this.aliasStore.size());
		
		this.ircOps = new ListStore(new File(this.baseDirPath + File.separator + "irc-ops.txt"), false);
		this.ircOps.load();
		
		for (Entry<String, String> entry : this.aliasStore.getAll()){
			this.ircAliases.put(entry.getKey().toLowerCase(), entry.getValue());
			this.gameAliases.put(entry.getValue().toLowerCase(), entry.getKey());
		}
		
		this.bot = new SimpleIRCBot(this, this.config.getString(Config.IRC_BOT_NICK), this.config.getString(Config.IRC_BOT_PASSWORD), this.config.getBoolean(Config.IRC_BOT_VERBOSE));
		this.commandSender = new IRCCommandSender(this, this.bot);
		
		this.permissionManager.registerPermissions(Permission.class);
		this.commandManager.registerCommandExecutor(new OpCommandExecutor(this));
	}
	
	public void onDisable(){
		this.bot.quitServer("Plugin disabled");
		this.bot.disconnect();
	}
	
}
