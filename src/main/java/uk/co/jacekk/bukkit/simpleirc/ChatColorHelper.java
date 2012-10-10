package uk.co.jacekk.bukkit.simpleirc;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.jibble.pircbot.Colors;

public class ChatColorHelper {
	
	public static final String DEFAULT_PREFIX = ChatColor.RED + "[E]" + ChatColor.RESET + "[";
	public static final String DEFAULT_SUFFIX = "]: ";
	
	private static HashMap<String, String> mcColourCodeMap;
	private static HashMap<String, String> IRCColourCodeMap;
	
	static{
		mcColourCodeMap = new HashMap<String, String>(17);
		IRCColourCodeMap = new HashMap<String, String>(17);
		
		mcColourCodeMap.put(ChatColor.BOLD.toString(), Colors.BOLD);
		mcColourCodeMap.put(ChatColor.UNDERLINE.toString(), Colors.UNDERLINE);
		mcColourCodeMap.put(ChatColor.RESET.toString(), Colors.NORMAL);
		mcColourCodeMap.put(ChatColor.BLACK.toString(), Colors.BLACK);
		mcColourCodeMap.put(ChatColor.DARK_BLUE.toString(), Colors.DARK_BLUE);
		mcColourCodeMap.put(ChatColor.DARK_GREEN.toString(), Colors.DARK_GREEN);
		mcColourCodeMap.put(ChatColor.DARK_AQUA.toString(), Colors.CYAN);
		mcColourCodeMap.put(ChatColor.DARK_RED.toString(), Colors.RED);
		mcColourCodeMap.put(ChatColor.DARK_PURPLE.toString(), Colors.PURPLE);
		mcColourCodeMap.put(ChatColor.GOLD.toString(), Colors.OLIVE);
		mcColourCodeMap.put(ChatColor.GRAY.toString(), Colors.LIGHT_GRAY);
		mcColourCodeMap.put(ChatColor.DARK_GRAY.toString(), Colors.DARK_GRAY);
		mcColourCodeMap.put(ChatColor.BLUE.toString(), Colors.BLUE);
		mcColourCodeMap.put(ChatColor.GREEN.toString(), Colors.GREEN);
		mcColourCodeMap.put(ChatColor.AQUA.toString(), Colors.CYAN);
		mcColourCodeMap.put(ChatColor.RED.toString(), Colors.RED);
		mcColourCodeMap.put(ChatColor.LIGHT_PURPLE.toString(), Colors.PURPLE);
		mcColourCodeMap.put(ChatColor.YELLOW.toString(), Colors.YELLOW);
		mcColourCodeMap.put(ChatColor.WHITE.toString(), Colors.BLACK); // This is deliberate !
		
		IRCColourCodeMap.put(Colors.BOLD, ChatColor.BOLD.toString());
		IRCColourCodeMap.put(Colors.UNDERLINE, ChatColor.UNDERLINE.toString());
		IRCColourCodeMap.put(Colors.NORMAL, ChatColor.RESET.toString());
		IRCColourCodeMap.put(Colors.BLACK, ChatColor.BLACK.toString());
		IRCColourCodeMap.put(Colors.DARK_BLUE, ChatColor.DARK_BLUE.toString());
		IRCColourCodeMap.put(Colors.DARK_GREEN, ChatColor.DARK_GREEN.toString());
		IRCColourCodeMap.put(Colors.CYAN, ChatColor.DARK_AQUA.toString());
		IRCColourCodeMap.put(Colors.RED, ChatColor.DARK_RED.toString());
		IRCColourCodeMap.put(Colors.PURPLE, ChatColor.DARK_PURPLE.toString());
		IRCColourCodeMap.put(Colors.OLIVE, ChatColor.GOLD.toString());
		IRCColourCodeMap.put(Colors.LIGHT_GRAY, ChatColor.GRAY.toString());
		IRCColourCodeMap.put(Colors.DARK_GRAY, ChatColor.DARK_GRAY.toString());
		IRCColourCodeMap.put(Colors.BLUE, ChatColor.BLUE.toString());
		IRCColourCodeMap.put(Colors.GREEN, ChatColor.GREEN.toString());
		IRCColourCodeMap.put(Colors.CYAN, ChatColor.AQUA.toString());
		IRCColourCodeMap.put(Colors.RED, ChatColor.RED.toString());
		IRCColourCodeMap.put(Colors.PURPLE, ChatColor.LIGHT_PURPLE.toString());
		IRCColourCodeMap.put(Colors.YELLOW, ChatColor.YELLOW.toString());
		IRCColourCodeMap.put(Colors.WHITE, ChatColor.WHITE.toString());
	}
	
	public static String convertIRCtoMC(String message){
		for (Entry<String, String> color : IRCColourCodeMap.entrySet()){
			message = message.replaceAll(color.getKey(), color.getValue());
		}
		
		return Colors.removeColors(message);
	}
	
	public static String convertMCtoIRC(String message){
		for (Entry<String, String> color : mcColourCodeMap.entrySet()){
			message = message.replaceAll(color.getKey(), color.getValue());
		}
		
		return ChatColor.stripColor(message);
	}
	
}