package uk.co.jacekk.bukkit.simpleirc;

import java.io.File;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.plugin.RegisteredServiceProvider;

import uk.co.jacekk.bukkit.baseplugin.v3.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v3.config.PluginConfig;

public class SimpleIRC extends BasePlugin {
	
	protected SimpleIRCBot bot;
	protected Chat chat;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.values(), this.log);
		
		RegisteredServiceProvider<Chat> chatProvider = this.server.getServicesManager().getRegistration(Chat.class);
		
		if (chatProvider != null){
			this.chat = chatProvider.getProvider();
		}
		
		this.bot = new SimpleIRCBot(this, this.config.getString(Config.IRC_BOT_NICK), this.config.getString(Config.IRC_BOT_PASSWORD), this.config.getBoolean(Config.IRC_BOT_VERBOSE));
	}
	
	public void onDisable(){
		this.bot.disconnect();
	}
	
}
