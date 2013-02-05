package uk.co.jacekk.bukkit.simpleirc;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import uk.co.jacekk.bukkit.baseplugin.v9.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v9.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v9.storage.DataStore;
import uk.co.jacekk.bukkit.baseplugin.v9.storage.ListStore;
import uk.co.jacekk.bukkit.simpleirc.bot.SimpleIRCBot;
import uk.co.jacekk.bukkit.simpleirc.command.IRCCommandExecutor;
import uk.co.jacekk.bukkit.simpleirc.command.OpCommandExecutor;

public class SimpleIRC extends BasePlugin {
	
	private DataStore aliasStore;
	
	public HashMap<String, String> ircAliases;
	public HashMap<String, String> gameAliases;
	
	public ListStore ircOps;
	
	public SimpleIRCBot bot;
	
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
		
		this.bot = new SimpleIRCBot(this);
		
		this.permissionManager.registerPermissions(Permission.class);
		
		this.commandManager.registerCommandExecutor(new OpCommandExecutor(this));
		this.commandManager.registerCommandExecutor(new IRCCommandExecutor(this));
	}
	
	public void onDisable(){
		this.bot.quitServer("Plugin disabled");
		this.bot.disconnect();
	}
	
}
