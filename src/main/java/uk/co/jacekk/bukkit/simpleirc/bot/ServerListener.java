package uk.co.jacekk.bukkit.simpleirc.bot;

import java.util.concurrent.Callable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.pircbotx.Colors;

import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.simpleirc.ChatColorHelper;
import uk.co.jacekk.bukkit.simpleirc.Config;
import uk.co.jacekk.bukkit.simpleirc.SimpleIRC;

public class ServerListener extends BaseListener<SimpleIRC> {
	
	private SimpleIRCBot bot;
	
	public ServerListener(SimpleIRC plugin, SimpleIRCBot bot){
		super(plugin);
		
		this.bot = bot;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerChat(final AsyncPlayerChatEvent event){
		plugin.scheduler.callSyncMethod(plugin, new Callable<Boolean>(){
			
			public Boolean call() throws Exception{
				String playerName = event.getPlayer().getName();
				
				if (plugin.gameAliases.containsKey(playerName)){
					playerName = plugin.gameAliases.get(playerName);
				}
				
				if (plugin.config.getBoolean(Config.IRC_BOT_PREVENT_PINGS)){
					playerName = playerName.substring(0, 1) + "\u200B" + playerName.substring(1);
				}
				
				bot.sendMessage(bot.getChannels(), ChatColorHelper.convertMCtoIRC(ChatColorHelper.convertMCtoIRC(String.format(event.getFormat(), playerName, event.getMessage()))));
				
				return true;
			}
			
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		String playerName = event.getPlayer().getName();
		String[] parts = event.getMessage().split(" ", 2);
		
		if (parts[0].equalsIgnoreCase("/me")){
			if (plugin.gameAliases.containsKey(playerName)){
				playerName = plugin.gameAliases.get(playerName);
			}
			
			if (plugin.config.getBoolean(Config.IRC_BOT_PREVENT_PINGS)){
				playerName = playerName.substring(0, 1) + "\u200B" + playerName.substring(1);
			}
			
			this.bot.sendMessage(this.bot.getChannels(), "* " + playerName + " " + parts[1]);
		}else if (parts[0].equalsIgnoreCase("/broadcast")){
			this.bot.sendMessage(this.bot.getChannels(), Colors.BLUE + "[Server]: " + parts[1]);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onServerCommand(ServerCommandEvent event){
		String[] parts = event.getCommand().split(" ", 2);
		
		if (parts[0].equalsIgnoreCase("say")){
			this.bot.sendMessage(this.bot.getChannels(), Colors.BLUE + "[Server]: " + parts[1]);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event){
		String playerName = event.getPlayer().getName();
		String message = event.getJoinMessage();
		
		if (message == null){
			return;
		}
		
		if (plugin.gameAliases.containsKey(playerName)){
			message = message.replaceAll(playerName, plugin.gameAliases.get(playerName));
		}
		
		this.bot.sendMessage(this.bot.getChannels(), ChatColorHelper.convertMCtoIRC(message));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event){
		String playerName = event.getPlayer().getName();
		String message = event.getQuitMessage();
		
		if (message == null){
			return;
		}
		
		if (plugin.gameAliases.containsKey(playerName)){
			message = message.replaceAll(playerName, plugin.gameAliases.get(playerName));
		}
		
		this.bot.sendMessage(this.bot.getChannels(), ChatColorHelper.convertMCtoIRC(message));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event){
		String playerName = event.getEntity().getName();
		String message = event.getDeathMessage();
		
		if (message == null){
			return;
		}
		
		if (plugin.gameAliases.containsKey(playerName)){
			message = message.replaceAll(playerName, plugin.gameAliases.get(playerName));
		}
		
		this.bot.sendMessage(this.bot.getChannels(), ChatColorHelper.convertMCtoIRC(message));
	}
	
}
