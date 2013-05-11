package uk.co.jacekk.bukkit.simpleirc.bot;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;

import uk.co.jacekk.bukkit.simpleirc.ChatColorHelper;
import uk.co.jacekk.bukkit.simpleirc.Config;
import uk.co.jacekk.bukkit.simpleirc.RemotePlayerChatEvent;
import uk.co.jacekk.bukkit.simpleirc.SimpleIRC;

public class SimpleIRCBot extends PircBotX implements Listener {
	
	private SimpleIRC plugin;
	public boolean connected;
	
	public SimpleIRCBot(SimpleIRC plugin){
		this.plugin = plugin;
		
		this.setVerbose(plugin.config.getBoolean(Config.IRC_BOT_VERBOSE));
		this.setAutoNickChange(false);
		this.setAutoReconnect(true);
		this.setName(plugin.config.getString(Config.IRC_BOT_NICK));
		this.setLogin(plugin.config.getString(Config.IRC_BOT_NICK));
		this.setVersion(plugin.getName() + " " + plugin.getDescription().getVersion());
		
		String serverPassword = plugin.config.getString(Config.IRC_SERVER_PASSWORD);
		
		try{
			if (serverPassword.isEmpty()){
				this.connect(plugin.config.getString(Config.IRC_SERVER_ADDRESS), plugin.config.getInt(Config.IRC_SERVER_PORT));
			}else{
				this.connect(plugin.config.getString(Config.IRC_SERVER_ADDRESS), plugin.config.getInt(Config.IRC_SERVER_PORT), serverPassword);
			}
		}catch (NickAlreadyInUseException e){
			plugin.log.fatal("The IRC nick you chose is already in use, it's probably a good idea to pick a unique one and register it with NickServ if the server allows it.");
		}catch (IOException e){
			e.printStackTrace();
		}catch (IrcException e){
			e.printStackTrace();
		}
		
		this.connected = true;
		
		String password = plugin.config.getString(Config.IRC_BOT_PASSWORD);
		
		if (!password.isEmpty()){
			this.identify(password);
		}
		
		for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
			this.joinChannel(channel);
		}
		
		plugin.pluginManager.registerEvents(this, plugin);
	}
	
	public void sendMessage(Collection<Channel> channels, String message){
		for (Channel channel : channels){
			this.sendMessage(channel, message);
		}
	}
	
}
