package uk.co.jacekk.bukkit.simpleirc;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import uk.co.jacekk.bukkit.baseplugin.v6.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.storage.DataStore;
import uk.co.jacekk.bukkit.baseplugin.v6.storage.ListStore;

public class SimpleIRC extends BasePlugin {
	
	protected SimpleIRCBot bot;
	
	private DataStore aliasStore;
	
	protected HashMap<String, String> ircAliases;
	protected HashMap<String, String> gameAliases;
	
	protected ListStore enabledCommands;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.class, this.log);
		this.aliasStore = new DataStore(new File(this.baseDirPath + File.separator + "aliases.txt"), true);
		this.aliasStore.load();
		
		this.ircAliases = new HashMap<String, String>(this.aliasStore.size());
		this.gameAliases = new HashMap<String, String>(this.aliasStore.size());
		
		this.enabledCommands = new ListStore(new File(this.baseDirPath + File.separator + "commands.txt"), false);
		this.enabledCommands.load();
		
		for (Entry<String, String> entry : this.aliasStore.getAll()){
			this.ircAliases.put(entry.getKey(), entry.getValue());
			this.gameAliases.put(entry.getValue(), entry.getKey());
		}
		
		this.bot = new SimpleIRCBot(this, this.config.getString(Config.IRC_BOT_NICK), this.config.getString(Config.IRC_BOT_PASSWORD), this.config.getBoolean(Config.IRC_BOT_VERBOSE));
	}
	
	public void onDisable(){
		this.bot.disconnect();
	}
	
}
