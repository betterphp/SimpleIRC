package uk.co.jacekk.bukkit.simpleirc.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.User;

import uk.co.jacekk.bukkit.baseplugin.v9.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v9.command.CommandHandler;
import uk.co.jacekk.bukkit.baseplugin.v9.command.CommandTabCompletion;
import uk.co.jacekk.bukkit.baseplugin.v9.command.SubCommandHandler;
import uk.co.jacekk.bukkit.simpleirc.Config;
import uk.co.jacekk.bukkit.simpleirc.Permission;
import uk.co.jacekk.bukkit.simpleirc.SimpleIRC;

public class IRCCommandExecutor extends BaseCommandExecutor<SimpleIRC> {
	
	public IRCCommandExecutor(SimpleIRC plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"irc"}, description = "Allowes a player to manage the IRC channel", usage = "[action] <args>")
	@CommandTabCompletion({"kick|ban|op|deop|voice|devoice"})
	public void ircop(CommandSender sender, String label, String[] args){
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <action> <args>");
		sender.sendMessage(ChatColor.RED + "Actions:");
		
		if (Permission.KICK.has(sender)){
			sender.sendMessage(ChatColor.RED + "  kick - Kicks a user from a channel");
		}
		
		if (Permission.BAN.has(sender)){
			sender.sendMessage(ChatColor.RED + "  ban - Bans a hostmask from a channel");
		}
		
		if (Permission.OP.has(sender)){
			sender.sendMessage(ChatColor.RED + "  op - Gives a user OP");
			sender.sendMessage(ChatColor.RED + "  deop - Takes OP from a user");
		}
		
		if (Permission.VOICE.has(sender)){
			sender.sendMessage(ChatColor.RED + "  voice - Gives voice to a user");
			sender.sendMessage(ChatColor.RED + "  devoice - Takes voice from a user");
		}
		
		if (Permission.LEAVE.has(sender)){
			sender.sendMessage(ChatColor.RED + "  leave - Leaves all channels");
		}
		
		if (Permission.JOIN.has(sender)){
			sender.sendMessage(ChatColor.RED + "  join - Joins the configured channels");
		}
		
		if (Permission.DISCONNECT.has(sender)){
			sender.sendMessage(ChatColor.RED + "  disconnect - Disconnects from the server");
		}
		
		if (Permission.CONNECT.has(sender)){
			sender.sendMessage(ChatColor.RED + "  connect - Connects to the server");
		}
	}
	
	public List<String> getChannelList(CommandSender sender, String[] args){
		return plugin.config.getStringList(Config.IRC_BOT_CHANNELS);
	}
	
	public List<String> getNickList(CommandSender sender, String[] args){
		ArrayList<String> nicks = new ArrayList<String>();
		
		for (User user : plugin.bot.getUsers(args[0])){
			nicks.add(user.getNick());
		}
		
		return nicks;
	}
	
	@SubCommandHandler(parent = "irc", name = "kick")
	@CommandTabCompletion({"[getChannelList]", "[getNickList]"})
	public void ircKick(CommandSender sender, String label, String[] args){
		if (!Permission.KICK.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length < 2){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " kick <channel> <nick> <reason>");
			return;
		}
		
		if (args.length > 2){
			String reason = args[2];
			
			for (int i = 3; i < args.length; ++i){
				reason += " " + args[i];
			}
			
			plugin.bot.kick(args[0], args[1], reason);
		}else{
			plugin.bot.kick(args[0], args[1]);
		}
		
		sender.sendMessage(ChatColor.GREEN + args[1] + " has been kicked from " + args[0]);
	}
	
	@SubCommandHandler(parent = "irc", name = "ban")
	@CommandTabCompletion({"[getChannelList]"})
	public void ircBan(CommandSender sender, String label, String[] args){
		if (!Permission.BAN.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length != 2){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " ban <channel> <hostmask>");
			return;
		}
		
		plugin.bot.ban(args[0], args[1]);
		
		sender.sendMessage(ChatColor.GREEN + args[1] + " has been banned from " + args[0]);
	}
	
	@SubCommandHandler(parent = "irc", name = "op")
	@CommandTabCompletion({"[getChannelList]", "[getNickList]"})
	public void ircOp(CommandSender sender, String label, String[] args){
		if (!Permission.OP.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length < 3){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " op <channel> <nick>");
			return;
		}
		
		plugin.bot.op(args[0], args[1]);
		
		sender.sendMessage(ChatColor.GREEN + args[1] + " has been oped in " + args[0]);
	}
	
	@SubCommandHandler(parent = "irc", name = "deop")
	@CommandTabCompletion({"[getChannelList]", "[getNickList]"})
	public void ircDeop(CommandSender sender, String label, String[] args){
		if (!Permission.OP.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length < 3){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " deop <channel> <nick>");
			return;
		}
		
		plugin.bot.deOp(args[0], args[1]);
		
		sender.sendMessage(ChatColor.GREEN + args[1] + " has been deoped in " + args[0]);
	}
	
	@SubCommandHandler(parent = "irc", name = "voice")
	@CommandTabCompletion({"[getChannelList]", "[getNickList]"})
	public void ircVoice(CommandSender sender, String label, String[] args){
		if (!Permission.VOICE.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length != 2){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " voice <channel> <nick>");
			return;
		}
		
		plugin.bot.voice(args[0], args[1]);
		
		sender.sendMessage(ChatColor.GREEN + args[1] + " has been given voice in " + args[0]);
	}
	
	@SubCommandHandler(parent = "irc", name = "devoice")
	@CommandTabCompletion({"[getChannelList]", "[getNickList]"})
	public void ircDevoice(CommandSender sender, String label, String[] args){
		if (!Permission.VOICE.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length != 2){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " devoice <channel> <nick>");
			return;
		}
		
		plugin.bot.deVoice(args[0], args[1]);
		
		sender.sendMessage(ChatColor.GREEN + args[1] + " no longer has voice in " + args[0]);
	}
	
	@SubCommandHandler(parent = "irc", name = "leave")
	public void ircLeave(CommandSender sender, String label, String[] args){
		if (!Permission.LEAVE.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
			plugin.bot.partChannel(channel);
		}
		
		sender.sendMessage(ChatColor.GREEN + "Left all channels");
	}
	
	@SubCommandHandler(parent = "irc", name = "join")
	public void ircJoin(CommandSender sender, String label, String[] args){
		if (!Permission.JOIN.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
			plugin.bot.joinChannel(channel);
		}
		
		sender.sendMessage(ChatColor.GREEN + "Joined all channels");
	}
	
	@SubCommandHandler(parent = "irc", name = "disconnect")
	public void ircDisconnect(CommandSender sender, String label, String[] args){
		if (!Permission.DISCONNECT.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		plugin.bot.connected = false;
		plugin.bot.disconnect();
		
		sender.sendMessage(ChatColor.GREEN + "Disconnected from IRC server");
	}
	
	@SubCommandHandler(parent = "irc", name = "connect")
	public void ircConnect(CommandSender sender, String label, String[] args){
		if (!Permission.CONNECT.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		String serverPassword = plugin.config.getString(Config.IRC_SERVER_PASSWORD);
		
		try{
			if (serverPassword.isEmpty()){
				plugin.bot.connect(plugin.config.getString(Config.IRC_SERVER_ADDRESS), plugin.config.getInt(Config.IRC_SERVER_PORT));
			}else{
				plugin.bot.connect(plugin.config.getString(Config.IRC_SERVER_ADDRESS), plugin.config.getInt(Config.IRC_SERVER_PORT), serverPassword);
			}
		}catch (NickAlreadyInUseException e){
			plugin.log.fatal("The IRC nick you chose is already in use, it's probably a good idea to pick a unique one and register it with NickServ if the server allows it.");
		}catch (IOException e){
			e.printStackTrace();
		}catch (IrcException e){
			e.printStackTrace();
		}
		
		plugin.bot.connected = true;
		
		String password = plugin.config.getString(Config.IRC_BOT_PASSWORD);
		
		if (!password.isEmpty()){
			plugin.bot.identify(password);
		}
		
		for (String channel : plugin.config.getStringList(Config.IRC_BOT_CHANNELS)){
			plugin.bot.joinChannel(channel);
		}
		
		sender.sendMessage(ChatColor.GREEN + "Connected to IRC server");
	}
	
}
