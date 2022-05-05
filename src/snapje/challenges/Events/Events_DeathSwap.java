package snapje.challenges.Events;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import snapje.challenge.Challenges.DeathSwap;
import snapje.challenge.Challenges.Game;
import snapje.challenge.Challenges.ManHunt;
import snapje.challenges.API.GameState;
import snapje.challenges.API.GameType;
import snapje.challenges.API.Packet;
import snapje.challenges.Utils.Chat;

public class Events_DeathSwap implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		
		Player p = (Player) e.getEntity();
		p.spigot().respawn();
		if(e.getEntity() instanceof Player) {
		
		if(Game.getPlayersGame(p) != null) {
			Game game = Game.getPlayersGame(p);
			if(game != null && game.getGameType() == GameType.DeathSwap) {
				if(game.getGameState() == GameState.STARTED) {
				e.setDeathMessage("");
				p.spigot().respawn();
				game.broadCastMessage("&c" + e.getEntity().getName() + " &7has been eliminated from the game.");
				game.setPlayerToSpectator(p);
				} else {
					p.spigot().respawn();
				}
				
			}
		}
		}
	}
	

	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		if(e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockY() != e.getFrom().getBlockY() || e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
			if(Game.playerIsInGame(p) && Game.getPlayersGame(p) != null) {
				Game game = Game.getPlayersGame(p);
				if(game.getGameState().equals(GameState.STARTING)) {
					p.teleport(e.getFrom());
				} else if(game.getGameState().equals(GameState.STARTED) && game.getGameType() == GameType.ManHunt && ManHunt.getManHuntGame(game.getGameID()).huntersAreReleased() == false) { 
					ManHunt hunt = ManHunt.getManHuntGame(game.getGameID());
					if(hunt.getHunters().contains(p)) {
					p.teleport(e.getFrom());
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if(Game.playerIsInGame(e.getPlayer()) == true && Game.getPlayersGame(e.getPlayer()) != null) {
			Player p = e.getPlayer();
			Game game = Game.getPlayersGame(p);
			
			if(game.getGameType() == GameType.DeathSwap) {
				if(e.getTo().getWorld().getEnvironment().equals(Environment.NETHER)) {
					p.teleport(e.getFrom());
				}
			}
		}
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		if(Game.playerIsInGame(p) && Game.getPlayersGame(p) != null) {
			Game game = Game.getPlayersGame(p);
			game.leaveGame(p);
			game.broadCastMessage("&c" + p.getName() + " &7has been eliminated from the game.");
		}
	}
	
	/*
	
	@EventHandler
	public void damge(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() != null && e.getDamager() instanceof Player) {
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			
			Game pg = Game.getPlayersGame(p);
			Game dg = Game.getPlayersGame(d);
			
			
			if(Game.playerIsInGame(p)) {
				e.setCancelled(true);
				
				if(Game.playerIsInGame(d) && pg == dg && dg.getGameType() == GameType.DeathSwap) {
					if(dg.playerIsSpectator(p)) {
						e.setCancelled(true);
					} else {
					d.sendMessage(Chat.format("&cYou can't hit people in deathSwap."));
					}
				} else {
					if(Game.playerIsInGame(d) && pg == dg && pg.getGameState() == GameState.INLOBBY || pg.getGameState() == GameState.STARTING) {
					d.sendMessage(Chat.format("&cYou can't hit people in the lobby."));
					e.setCancelled(true);
					} else if(Game.playerIsInGame(p)) { 
						d.sendMessage(Chat.format("&cYou can't hite people while their in a game."));
						e.setCancelled(true);
					} else if(Game.playerIsInGame(d)) {
						d.sendMessage(Chat.format("&cYou can't hit people who are not in a game."));
						e.setCancelled(true);
					}
				}
				
			} else if(Game.playerIsInGame(d)) {
				e.setCancelled(true);
				d.sendMessage(Chat.format("&cThis player is not in the same game as you."));
			}
		} else if(e.getEntity() instanceof Player) {
			e.setCancelled(true);
		}
	}

 * 
	 */

}
