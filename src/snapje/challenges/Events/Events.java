package snapje.challenges.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import snapje.challenge.Challenges.Game;
import snapje.challenges.API.GameState;
import snapje.challenges.API.GameType;
import snapje.challenges.Utils.Chat;

public class Events implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		if(Bukkit.getOnlinePlayers() != null) {
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(Game.playerIsInGame(online)) {
					e.getPlayer().hidePlayer(online);
					online.hidePlayer(e.getPlayer());
				}
			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(Game.getPlayersGame(e.getPlayer()) != null) {
			Player p = (Player) e.getPlayer();
			Game game = Game.getPlayersGame(e.getPlayer());
			
			if(game != null) {
				if(game.getSpectators().contains(p)) {
					p.sendMessage(Chat.format("&cYou can't speak while spectating."));
				} else {
					game.broadCastMessage("&9&lGame &7" + e.getPlayer().getName() + "&7: &f" + e.getMessage());
				}
			}
			
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if(e.getEntity() instanceof Player) {
         Player p = (Player) e.getEntity();
			
			if(Game.getPlayersGame(p) != null) {
				Game game = Game.getPlayersGame(p);
				
				if(game != null && game.getGameState() != GameState.STARTED) {
					e.setFoodLevel(20);
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void playerDamageEvent(EntityDamageEvent e) {
		
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			if(Game.getPlayersGame(p) != null) {
				Game game = Game.getPlayersGame(p);
				
				if(game != null && game.getGameState() != GameState.STARTED) {
					p.setHealth(20);
					e.setCancelled(true);
				}
			}
		}
	}

}
