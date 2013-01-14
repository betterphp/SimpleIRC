package uk.co.jacekk.bukkit.simpleirc;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Represents a chat message sent from a remote location.
 */
public class RemotePlayerChatEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private String playerName;
	private String message;
	private String format;
	private HashSet<Player> recipients;
	
	private boolean isCancelled;
	
	public RemotePlayerChatEvent(String playerName, String message, HashSet<Player> recipients){
		this.playerName = playerName;
		this.message = message;
		this.format = "<%s> %s";
		this.recipients = recipients;
		
		this.isCancelled = false;
	}
	
	/**
	 * Gets the name of the player sending the message.
	 * 
	 * This will be their name on the server if an alias is in use.
	 * 
	 * @return The player name.
	 */
	public String getPlayerName(){
		return this.playerName;
	}
	
	/**
	 * Sets the player name that will be displayed in game.
	 * 
	 * @param playerName The name.
	 */
	public void setPlayerName(String playerName){
		this.playerName = playerName;
	}
	
	/**
	 * Gets the message that was sent.
	 * 
	 * @return The message.
	 */
	public String getMessage(){
		return this.message;
	}
	
	/**
	 * Sets the message that will appear in game.
	 * 
	 * @param message The message.
	 */
	public void setMessage(String message){
		this.message = message;
	}
	
	/**
	 * Gets the format of the message.
	 * 
	 * @return The format.
	 */
	public String getFormat(){
		return this.format;
	}
	
	/**
	 * Sets the format of the message.
	 * 
	 * @param format The format.
	 */
	public void setFormat(String format){
		this.format = format;
	}
	
	/**
	 * Gets the players that would receive this message.
	 * 
	 * @return The set of players.
	 */
	public Set<Player> getRecipients(){
		return this.recipients;
	}
	
	@Override
	public boolean isCancelled(){
		return this.isCancelled;
	}
	
	@Override
	public void setCancelled(boolean cancelled){
		this.isCancelled = cancelled;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
}
