package snapje.challenge.Challenges;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import snapje.challenges.API.GameState;
import snapje.challenges.API.Packet;
import snapje.challenges.Core.Main;
import snapje.challenges.Utils.Chat;


public class DeathSwap {
	
	private static HashMap<String, DeathSwap> games = new HashMap<String, DeathSwap>();
	private Game game;
	
	private int timer;
	private int time;

	
	public DeathSwap(Game game) {
		game.setGameID("DS-" + game.getPlayingPlayers().size());
		
		this.game = game;
		games.put(game.getGameID(), this);
	}
	
	public void startDeathSwap() {
		
		time = 20;
		if(game != null) {
			timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
				
				@Override
				public void run() {
					game.setTimerLevel(time);
					if(time > 0) {
						if(time <= 10) {
							game.broadCastMessage("&CDeathSwap &7is starting in &e" + time + " &7second(s).");
						}
						time--;
					} else {
						game.setGameState(GameState.STARTED);
						Bukkit.getScheduler().cancelTask(timer);
						game.setTimerLevel(0);
						game.broadCastMessage("&cDeathSwap &7started be quick the first swap will be in 5 minutes.");
						startSwappingTimer();
					}
					
				}
			}, 0L, 20L);
		}
	}
	
	public void startSwappingTimer() {
		if(game != null) {
			time = 300;
			timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
				
				@Override
				public void run() {
					if(game.getWorld() != null) {
					if(game.getGameState() == GameState.STARTED) {
						game.setTimerLevel(time);
					if(time > 0) {
						if(time <= 10) {
							game.broadCastMessage("&7Swapping positions in &e" + time + " &7second(s).");
						}
						time--;
	
						
					} else {
						time = 300;
						try {
						swapPlayers();
						} catch (Exception ex) {}
						game.broadCastMessage("&cDeathSwap &7The next swap will be in 5 minutes..");
					}
					} else {
						Bukkit.getScheduler().cancelTask(timer);
					}
				} else {
					game.broadCastMessage("&cSorry, but the world hasn't been generated.");
					try {
						game.createWorld();
					} catch (Exception ex) {
						game.broadCastMessage("&cThere was an error while generating the world.");
						game.endGame();
					}
					
				}
				} 
			}, 0L, 20L);
			
			for(Player p : game.getPlayingPlayers()) {
				
					try {
				    p.teleport(game.getWorld().getSpawnLocation());
				    p.setGameMode(GameMode.SURVIVAL);
					} catch (Exception ex) {
						game.broadCastMessage("&cGame ended, because world could not be loaded.");
						Bukkit.getScheduler().cancelTask(timer);
						game.endGame();
					}
				
			}
		}
	}
	
	public void swapPlayers() {
		if(game != null && game.getPlayingPlayers() != null) {
			ArrayList<Player> tpList = new ArrayList<Player>();
			for(Player p : game.getPlayingPlayers()) {
				tpList.add(p);
			}
			
			for(Player p : tpList) {
				Random random = new Random();
				int number = random.nextInt(Bukkit.getOnlinePlayers().size());
				Player target = tpList.get(number);
				
				Location locp = p.getLocation().clone();
				Location loct = target.getLocation().clone();
				
				if(target != null) {
					p.teleport(loct);
					target.teleport(locp);
					
					tpList.remove(p);
					tpList.remove(target);
				}
			}
		}
	}
	
	/*
	 * static
	 */
	
	public static Set<String> getDeathSwapGames() {
		return games.keySet();
	}
	public static DeathSwap getDeathSwapGame(String gameID) {
		return games.get(gameID);
	}
	

	
	
	
	
	

}
