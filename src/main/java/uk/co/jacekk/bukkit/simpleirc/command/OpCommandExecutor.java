package uk.co.jacekk.bukkit.simpleirc.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.v8.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v8.command.CommandHandler;
import uk.co.jacekk.bukkit.baseplugin.v8.command.CommandTabCompletion;
import uk.co.jacekk.bukkit.simpleirc.Permission;
import uk.co.jacekk.bukkit.simpleirc.SimpleIRC;

public class OpCommandExecutor extends BaseCommandExecutor<SimpleIRC> {
	
	public OpCommandExecutor(SimpleIRC plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"ircop"}, description = "Allowes a player to use commands from IRC", usage = "[player_name]")
	@CommandTabCompletion({"<online_player>"})
	public void ircop(CommandSender sender, String label, String[] args){
		if (!Permission.GIVE_OP.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return;
		}
		
		if (args.length != 1){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <player_name>");
			return;
		}
		
		String playerName = args[0];
		
		if (plugin.ircOps.contains(playerName)){
			sender.sendMessage(ChatColor.RED + playerName + " is already an OP");
			return;
		}
		
		plugin.ircOps.add(playerName);
		plugin.ircOps.save();
	}
	
	@CommandHandler(names = {"ircdeop"}, description = "Stops a player being able to use commands from IRC", usage = "[player_name]")
	@CommandTabCompletion({"<online_player>"})
	public void ircdeop(CommandSender sender, String label, String[] args){
		if (!Permission.TAKE_OP.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return;
		}
		
		if (args.length != 1){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <player_name>");
			return;
		}
		
		String playerName = args[0];
		
		if (!plugin.ircOps.contains(playerName)){
			sender.sendMessage(ChatColor.RED + playerName + " is not an OP");
			return;
		}
		
		plugin.ircOps.remove(playerName);
		plugin.ircOps.save();
	}
	
}
