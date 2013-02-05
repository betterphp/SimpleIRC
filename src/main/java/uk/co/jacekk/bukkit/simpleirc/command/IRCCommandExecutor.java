package uk.co.jacekk.bukkit.simpleirc.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.v9.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v9.command.CommandHandler;
import uk.co.jacekk.bukkit.baseplugin.v9.command.CommandTabCompletion;
import uk.co.jacekk.bukkit.baseplugin.v9.command.SubCommandHandler;
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
	}
	
	@SubCommandHandler(parent = "irc", name = "kick")
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
	
}
