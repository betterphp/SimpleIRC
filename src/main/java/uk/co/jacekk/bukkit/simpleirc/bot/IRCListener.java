package uk.co.jacekk.bukkit.simpleirc.bot;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.UserSnapshot;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;

import uk.co.jacekk.bukkit.simpleirc.ChatColorHelper;
import uk.co.jacekk.bukkit.simpleirc.Config;
import uk.co.jacekk.bukkit.simpleirc.RemotePlayerChatEvent;
import uk.co.jacekk.bukkit.simpleirc.SimpleIRC;

public class IRCListener extends ListenerAdapter<SimpleIRCBot> implements Listener<SimpleIRCBot> {
	
	private SimpleIRC plugin;
	private SimpleIRCBot bot;
	private IRCCommandSender commandSender;
	
	public IRCListener(SimpleIRC plugin, SimpleIRCBot bot){
		this.plugin = plugin;
		this.bot = bot;
		this.commandSender = new IRCCommandSender(plugin, this.bot);
	}
	
	@Override
	public void onMessage(MessageEvent<SimpleIRCBot> event){
		User user = event.getUser();
		String sender = user.getNick();
		
		if (sender.equals(event.getBot().getNick())){
			return;
		}
		
		String message = event.getMessage();
		String senderLower = sender.toLowerCase();
		String playerName = (plugin.ircAliases.containsKey(senderLower)) ? plugin.ircAliases.get(senderLower) : sender;  
		
		if (message.startsWith("!") && plugin.ircOps.contains(playerName) && !plugin.gameAliases.containsKey(senderLower)){
			String command = message.substring(1);
			
			this.commandSender.setMessageTarget(event.getChannel().getName());
			this.commandSender.setName(playerName);
			plugin.server.dispatchCommand(this.commandSender, command);
			this.commandSender.setMessageTarget(null);
			this.commandSender.setName(null);
		}else{
			RemotePlayerChatEvent chatEvent = new RemotePlayerChatEvent(playerName, message, new HashSet<Player>(Arrays.asList(plugin.server.getOnlinePlayers())));
			
			plugin.pluginManager.callEvent(chatEvent);
			
			if (!chatEvent.isCancelled()){
				String chatMessage = String.format(chatEvent.getFormat(), playerName, ChatColorHelper.convertIRCtoMC(message));
				
				for (Player recipient : chatEvent.getRecipients()){
					recipient.sendMessage(chatMessage);
				}
				
				plugin.server.getConsoleSender().sendMessage(chatMessage);
			}
		}
	}
	
	@Override
	public void onPrivateMessage(PrivateMessageEvent<SimpleIRCBot> event){
		String sender = event.getUser().getNick();
		String message = event.getMessage();
		String senderLower = sender.toLowerCase();
		String playerName = (plugin.ircAliases.containsKey(senderLower)) ? plugin.ircAliases.get(senderLower) : sender;  
		
		if (plugin.ircOps.contains(playerName) && !plugin.gameAliases.containsKey(senderLower)){
			this.commandSender.setMessageTarget(sender);
			plugin.server.dispatchCommand(this.commandSender, message);
			this.commandSender.setMessageTarget(null);
			this.commandSender.setName(null);
		}
	}
	
	@Override
	public void onAction(ActionEvent<SimpleIRCBot> event){
		User user = event.getUser();
		String sender = user.getNick();
		
		if (sender.equals(event.getBot().getNick())){
			return;
		}
		
		String senderLower = sender.toLowerCase();
		String playerName = (plugin.ircAliases.containsKey(senderLower)) ? plugin.ircAliases.get(senderLower) : sender;  
		
		plugin.server.broadcastMessage("* " + playerName + " " + event.getAction());
	}
	
	@Override
	public void onNickChange(NickChangeEvent<SimpleIRCBot> event){
		User user = event.getUser();
		String nick = event.getNewNick();
		
		if (plugin.gameAliases.containsKey(nick.toLowerCase())){
			for (Channel channel : user.getChannels()){
				this.bot.kick(channel, user, "Attempting to impersonate another player");
			}
		}
	}
	
	@Override
	public void onJoin(JoinEvent<SimpleIRCBot> event){
		User user = event.getUser();
		String sender = user.getNick();
		
		if (sender.equals(event.getBot().getNick())){
			return;
		}
		
		if (plugin.gameAliases.containsKey(sender.toLowerCase())){
			this.bot.kick(event.getChannel(), user, "Attempting to impersonate another player");
		}
		
		String playerName = (plugin.ircAliases.containsKey(sender)) ? plugin.ircAliases.get(sender) : sender;
		
		plugin.server.broadcastMessage(ChatColor.YELLOW + playerName + " has joined the chat");
	}
	
	@Override
	public void onPart(PartEvent<SimpleIRCBot> event){
		User user = event.getUser();
		String sender = user.getNick();
		
		if (sender.equals(event.getBot().getNick())){
			return;
		}
		
		String senderLower = sender.toLowerCase();
		String playerName = (plugin.ircAliases.containsKey(senderLower)) ? plugin.ircAliases.get(senderLower) : sender; 
		
		plugin.server.broadcastMessage(ChatColor.YELLOW + playerName + " has left the chat");
	}
	
	@Override
	public void onQuit(QuitEvent<SimpleIRCBot> event){
		UserSnapshot user = event.getUser();
		String sender = user.getNick();
		
		if (sender.equals(event.getBot().getNick())){
			return;
		}
		
		String senderLower = sender.toLowerCase();
		String playerName = (plugin.ircAliases.containsKey(senderLower)) ? plugin.ircAliases.get(senderLower) : sender; 
		
		plugin.server.broadcastMessage(ChatColor.YELLOW + playerName + " has left the chat");
	}
	
	@Override
	public void onKick(KickEvent<SimpleIRCBot> event){
		User user = event.getRecipient();
		String sender = user.getNick();
		
		if (sender.equals(event.getBot().getNick())){
			return;
		}
		
		String senderLower = sender.toLowerCase();
		String playerName = (plugin.ircAliases.containsKey(senderLower)) ? plugin.ircAliases.get(senderLower) : sender; 
		
		plugin.server.broadcastMessage(ChatColor.YELLOW + playerName + " has left the chat");
	}
	
}
