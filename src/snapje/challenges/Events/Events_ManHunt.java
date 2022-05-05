package snapje.challenges.Events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import snapje.challenge.Challenges.Game;
import snapje.challenge.Challenges.ManHunt;
import snapje.challenges.API.GameState;
import snapje.challenges.API.GameType;
import snapje.challenges.Utils.Chat;

public class Events_ManHunt implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(Game.getPlayersGame(p) != null) {
			
			if(Game.getPlayersGame(p) != null && ManHunt.getManHuntGame(Game.getPlayersGame(p).getGameID()) != null) {
				Game game = Game.getPlayersGame(p);
				if(game.getGameType() == GameType.ManHunt && game.getGameState() == GameState.STARTED && ManHunt.getManHuntGame(game.getGameID()) != null) {
				ManHunt hunt = ManHunt.getManHuntGame(game.getGameID());
				if(hunt.getRunners().contains(p)) {
					hunt.setRunnerToSpectator(p);
				}
				}
			}
		}
	}
	
	@EventHandler
	public void entityDie(EntityDeathEvent e) {
		
		if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
			if(e.getEntity().getType() == EntityType.ENDER_DRAGON) {
				Player p = (Player) e.getEntity();
				
				
				if(Game.getPlayersGame(p) != null && ManHunt.getManHuntGame(Game.getPlayersGame(p).getGameID()) != null) {
					Game game = Game.getPlayersGame(p);
					ManHunt hunt = ManHunt.getManHuntGame(game.getGameID());
					game.broadCastMessage("&6ManHunt &7The &e&lRunners &7won the game.");
					game.endGame();
				}
				
			}
		} 
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		
		
		if(e.getEntity() instanceof Player) {
			
			if(e.getDamager() instanceof Player) {
				Player p = (Player) e.getEntity();
				Player d = (Player) e.getDamager();
				
				if(Game.getPlayersGame(p) != null && Game.getPlayersGame(d) != null && Game.getPlayersGame(p) == Game.getPlayersGame(d)) {
				 Game game = Game.getPlayersGame(p);
				
				 if(game.getGameState() == GameState.STARTED && game.getGameType() == GameType.ManHunt) {
					 ManHunt hunt = ManHunt.getManHuntGame(game.getGameID());
					 if(hunt.getHunters().contains(d) && hunt.getHunters().contains(p)) {
						 e.setCancelled(true);
						 d.sendMessage(Chat.format("&cYou can't hit your teammates"));
					 } else if(hunt.getRunners().contains(d) && hunt.getRunners().contains(p)) {
						 e.setCancelled(true);
						 d.sendMessage(Chat.format("&cYou can't hit your teammates"));
					 } else {
						 e.setCancelled(false);
					 }
				 }
				}
			}
			
		}
	}
	
	@EventHandler
	public void playerInteracktEvent(PlayerInteractEvent e) {
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().getItemInHand() != null) {
			if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().getType() != Material.AIR) {
				if(Game.getPlayersGame(e.getPlayer()) != null && Game.playerIsInGame(e.getPlayer()) == true && Game.getPlayersGame(e.getPlayer()).getGameType() == GameType.ManHunt) {
	
				ItemStack item = e.getPlayer().getItemInHand();
				ItemStack compass = ManHunt.getCompass();
				Player p = e.getPlayer();
				
				if(p.getItemInHand().getType() == Material.COMPASS && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(compass.getItemMeta().getDisplayName())) {
			
			
					
					if(Game.getPlayersGame(p) != null && ManHunt.getManHuntGame(Game.getPlayersGame(p).getGameID()) != null) {
						Game game = Game.getPlayersGame(e.getPlayer());
						ManHunt hunt = ManHunt.getManHuntGame(game.getGameID());
					Player target = null;
					
					if(hunt.getRunners() != null && !hunt.getRunners().isEmpty()) {
					for(Player t : hunt.getRunners()) {
						if(target == null) {
							target = t;
						} else {
							if(p.getLocation().distance(target.getLocation()) > t.getLocation().distance(p.getLocation())) {
								target = t;
							}
						}
					}
					
					if(target.getLocation().getWorld() == p.getLocation().getWorld()) {
					p.setCompassTarget(target.getLocation());
					p.sendMessage(Chat.format("&7Youre compass is now pointing to the nearest player: &b" + target.getName() + "&a."));
					} else {
						p.sendMessage(Chat.format("&cTarget not found."));
					}
					} else {
						p.sendMessage(Chat.format("&cAll the runners died."));
					}
					} else {
						Player target = null;
						
						if(Bukkit.getOnlinePlayers() != null && !Bukkit.getOnlinePlayers().isEmpty()) {
						for(Player t : Bukkit.getOnlinePlayers()) {
							if(target == null) {
								target = t;
							} else {
								if(p.getLocation().distance(target.getLocation()) > t.getLocation().distance(p.getLocation())) {
									target = t;
								}
							}
						}
						
						if(target.getLocation().getWorld() == p.getLocation().getWorld()) {
						p.setCompassTarget(target.getLocation());
						p.sendMessage(Chat.format("&7Youre compass is now pointing to the nearest player: &b" + target.getName() + "&a."));
						} else {
							p.sendMessage(Chat.format("&cTarget not found."));
						}
					}
					
					
				}
				

				}
				}
		}
		}
	}
	

}
