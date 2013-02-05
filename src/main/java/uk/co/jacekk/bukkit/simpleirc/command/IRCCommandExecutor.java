package uk.co.jacekk.bukkit.simpleirc.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.v9.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v9.command.CommandHandler;
import uk.co.jacekk.bukkit.baseplugin.v9.command.CommandTabCompletion;
import uk.co.jacekk.bukkit.simpleirc.Permission;
import uk.co.jacekk.bukkit.simpleirc.SimpleIRC;

public class IRCCommandExecutor extends BaseCommandExecutor<SimpleIRC> {
	
	public IRCCommandExecutor(SimpleIRC plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"irc"}, description = "Allowes a player to manage the IRC channel", usage = "[action] <args>")
	@CommandTabCompletion({"kick|ban|op|deop|voice|devoice"})
	public void ircop(CommandSender sender, String label, String[] args){
		if (!Permission.MANAGE_CHANNEL.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return;
		}
		
		if (args.length == 0){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <action> <args>");
			sender.sendMessage(ChatColor.RED + "Actions:");
			sender.sendMessage(ChatColor.RED + "  kick - Kicks a user from a channel");
			sender.sendMessage(ChatColor.RED + "  ban - Bans a hostmask from a channel");
			sender.sendMessage(ChatColor.RED + "  op - Gives a user OP");
			sender.sendMessage(ChatColor.RED + "  deop - Takes OP from a user");
			sender.sendMessage(ChatColor.RED + "  voice - Gives voice to a user");
			sender.sendMessage(ChatColor.RED + "  devoice - Takes voice from a user");
			return;
		}
		
		String action = args[0].toLowerCase();
		
		if (action.equals("kick")){
			if (args.length < 3){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " kick <channel> <nick> <reason>");
				return;
			}
			
			if (args.length > 3){
				String reason = args[3];
				
				for (int i = 4; i < args.length; ++i){
					reason += " " + args[i];
				}
				
				plugin.bot.kick(args[1], args[2], reason);
			}else{
				plugin.bot.kick(args[1], args[2]);
			}
		}else if (action.equals("ban")){
			if (args.length < 3){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " ban <channel> <hostmask>");
				return;
			}
			
			plugin.bot.ban(args[1], args[2]);
		}else if (action.equals("op")){
			if (args.length < 3){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " op <channel> <nick>");
				return;
			}
			
			plugin.bot.op(args[1], args[2]);
		}else if (action.equals("deop")){
			if (args.length < 3){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " deop <channel> <nick>");
				return;
			}
			
			plugin.bot.deOp(args[1], args[2]);
		}else if (action.equals("voice")){
			if (args.length < 3){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " voice <channel> <nick>");
				return;
			}
			
			plugin.bot.voice(args[1], args[2]);
		}else if (action.equals("devoice")){
			if (args.length < 3){
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + " devoice <channel> <nick>");
				return;
			}
			
			plugin.bot.deVoice(args[1], args[2]);
		}else{
			sender.sendMessage(ChatColor.RED + "Invalid action, try /" + label + " for a list of actions");
			return;
		}
		
		sender.sendMessage(ChatColor.GREEN + "IRC command sent");
	}
	
}
