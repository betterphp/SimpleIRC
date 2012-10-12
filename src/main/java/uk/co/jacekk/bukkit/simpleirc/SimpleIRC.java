package uk.co.jacekk.bukkit.simpleirc;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import uk.co.jacekk.bukkit.baseplugin.v3.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v3.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v3.storage.DataStore;

public class SimpleIRC extends BasePlugin {
	
	protected SimpleIRCBot bot;
	
	private DataStore aliasStore;
	
	protected HashMap<String, String> ircAliases;
	protected HashMap<String, String> gameAliases;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.values(), this.log);
		this.aliasStore = new DataStore(new File(this.baseDirPath + File.separator + "aliases.txt"), true);
		this.aliasStore.load();
		
		this.ircAliases = new HashMap<String, String>(this.aliasStore.size());
		this.gameAliases = new HashMap<String, String>(this.aliasStore.size());
		
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
