package uk.co.jacekk.bukkit.simpleirc;

import java.io.IOException;
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
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class SimpleIRCBot extends PircBot implements Listener {
	
	private SimpleIRC plugin;
	
	public SimpleIRCBot(SimpleIRC plugin, String nick, String password, boolean verbose){
		this.plugin = plugin;
		
		this.setVerbose(verbose);
		this.setAutoNickChange(false);
		this.setName(nick);
		
		String serverPassword = plugin.config.getString(Config.IRC_SERVER_PASSWORD);
		
		try{
			if (serverPassword.isEmpty()){
				this.connect(plugin.config.getString(Config.IRC_SERVER_ADDRESS), plugin.config.getInt(Config.IRC_SERVER_PORT));
			}else{
				this.connect(plugin.config.getString(Config.IRC_SERVER_ADDRESS), plugin.config.getInt(Config.IRC_SERVER_PORT), serverPassword);
			}
		}catch (NickAlreadyInUseException e){
			plugin.log.fatal("The IRC nick you chose is already in use.");
		}catch (IOException e){
			e.printStackTrace();
		}catch (IrcException e){
			e.printStackTrace();
		}
		
		if (!password.isEmpty()){
			this.identify(password);
		}
		
		for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
			this.joinChannel(channel);
		}
		
		plugin.pluginManager.registerEvents(this, plugin);
	}
	
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		if (!sender.equals(this.getNick())){
			Player player = plugin.server.getPlayer(sender);
			String worldName = (player == null) ? plugin.server.getWorlds().get(0).getName() : player.getWorld().getName();
			
			String prefix = (plugin.chat == null) ? ChatColorHelper.DEFAULT_PREFIX : plugin.chat.getPlayerPrefix(worldName, sender);
			String suffix = (plugin.chat == null) ? ChatColorHelper.DEFAULT_SUFFIX : plugin.chat.getPlayerSuffix(worldName, sender);
			
			plugin.server.broadcastMessage(ChatColor.AQUA + "[IRC]" + ChatColor.RESET + prefix + sender + suffix + ChatColorHelper.convertIRCtoMC(message));
		}
	}
	
	@Override
	public void onAction(String sender, String login, String hostname, String target, String action){
		if (!sender.equals(this.getNick())){
			plugin.server.broadcastMessage(ChatColor.AQUA + "[IRC] " + ChatColor.RESET + "* " + sender + " " + action);
		}
	}
	
	@Override
	public void onJoin(String channel, String sender, String login, String hostname){
		if (!sender.equals(this.getNick())){
			plugin.server.broadcastMessage(ChatColor.AQUA + "[IRC] " + ChatColor.YELLOW + sender + " has joined the chat");
		}
	}
	
	@Override
	public void onPart(String channel, String sender, String login, String hostname){
		if (!sender.equals(this.getNick())){
			plugin.server.broadcastMessage(ChatColor.AQUA + "[IRC] " + ChatColor.YELLOW + sender + " has left the chat");
		}
	}
	
	@Override
	public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason){
		if (!sourceNick.equals(this.getNick())){
			plugin.server.broadcastMessage(ChatColor.AQUA + "[IRC] " + ChatColor.YELLOW + sourceNick + " has left the chat");
		}
	}
	
	@Override
	public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason){
		if (recipientNick.equals(this.getNick())){
			this.joinChannel(channel);
		}else{
			plugin.server.broadcastMessage(ChatColor.AQUA + "[IRC] " + ChatColor.YELLOW + recipientNick + " has left the chat");
		}
	}
	
	@Override
	public void onDisconnect(){
		plugin.log.warn("Disconnected from IRC, will reconnect in 5 seconds.");
		
		plugin.scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){
			
			public void run(){
				try{
					SimpleIRCBot.this.reconnect();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
		}, 100L);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerChat(final AsyncPlayerChatEvent event){
		plugin.scheduler.callSyncMethod(plugin, new Callable<Boolean>(){
			
			public Boolean call() throws Exception{
				Player player = event.getPlayer();
				String playerName = player.getName();
				String worldName = player.getWorld().getName();
				String message = ChatColorHelper.convertMCtoIRC(event.getMessage());
				
				String prefix = (plugin.chat == null) ? ChatColorHelper.DEFAULT_PREFIX : plugin.chat.getPlayerPrefix(worldName, playerName);
				String suffix = (plugin.chat == null) ? ChatColorHelper.DEFAULT_SUFFIX : plugin.chat.getPlayerSuffix(worldName, playerName);
				
				for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
					SimpleIRCBot.this.sendMessage(channel, prefix + playerName + suffix + message);
				}
				
				return true;
			}
			
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		String playerName = event.getPlayer().getName();
		String[] parts = event.getMessage().split(" ", 2);
		
		if (parts[0].equalsIgnoreCase("/me")){
			for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
				SimpleIRCBot.this.sendMessage(channel, "* " + playerName + " " + parts[1]);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event){
		String message = event.getJoinMessage();
		
		for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
			SimpleIRCBot.this.sendMessage(channel, ChatColorHelper.convertMCtoIRC(message));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event){
		String message = event.getQuitMessage();
		
		for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
			SimpleIRCBot.this.sendMessage(channel, ChatColorHelper.convertMCtoIRC(message));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event){
		String message = event.getDeathMessage();
		
		for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
			SimpleIRCBot.this.sendMessage(channel, ChatColorHelper.convertMCtoIRC(ChatColor.GRAY + message));
		}
	}
	
}
