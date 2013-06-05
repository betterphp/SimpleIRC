package uk.co.jacekk.bukkit.simpleirc;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import uk.co.jacekk.bukkit.baseplugin.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.storage.DataStore;
import uk.co.jacekk.bukkit.simpleirc.bot.ServerListener;
import uk.co.jacekk.bukkit.simpleirc.bot.SimpleIRCBot;
import uk.co.jacekk.bukkit.simpleirc.command.IRCCommandExecutor;

public class SimpleIRC extends BasePlugin {
	
	private DataStore aliasStore;
	
	public HashMap<String, String> ircAliases;
	public HashMap<String, String> gameAliases;
	
	public SimpleIRCBot bot;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.class, this.log);
		this.aliasStore = new DataStore(new File(this.baseDirPath + File.separator + "aliases.txt"), true);
		this.aliasStore.load();
		
		this.ircAliases = new HashMap<String, String>(this.aliasStore.size());
		this.gameAliases = new HashMap<String, String>(this.aliasStore.size());
		
		for (Entry<String, String> entry : this.aliasStore.getAll()){
			this.ircAliases.put(entry.getKey().toLowerCase(), entry.getValue());
			this.gameAliases.put(entry.getValue().toLowerCase(), entry.getKey());
		}
		
		this.bot = new SimpleIRCBot(this);
		this.bot.connect();
		
		this.permissionManager.registerPermissions(Permission.class);
		
		this.commandManager.registerCommandExecutor(new IRCCommandExecutor(this));
		
		this.pluginManager.registerEvents(new ServerListener(this, this.bot), this);
	}
	
	public void onDisable(){
		this.bot.quitServer("Plugin disabled");
		this.bot.disconnect();
	}
	
}
