package snapje.challenge.Challenges;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.FileUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import snapje.challenges.API.GameState;
import snapje.challenges.API.GameType;
import snapje.challenges.API.ItemCreator;
import snapje.challenges.API.VersionAPI;
import snapje.challenges.Core.Main;
import snapje.challenges.Utils.Chat;

public class ManHunt {
	
	private static HashMap<String, ManHunt> games = new HashMap<String, ManHunt>();
	private Game game;
	
	private ArrayList<Player> runners = new ArrayList<Player>();
	private ArrayList<Player> hunters = new ArrayList<Player>();
	
	private ArrayList<Player> runnerSpectator = new ArrayList<Player>();
	
	private ArrayList<Player> playerWhoMustChose;
	
	private int timer;
	private int time;
	
	private boolean huntersReleased = false;

	
	public ManHunt(Game game) {
		game.setGameID("MH-" + game.getPlayingPlayers().size());
		this.playerWhoMustChose = game.getPlayingPlayers();
		this.game = game;
		games.put(game.getGameID(), this);
	}
	
	public void addHunter(Player p) {
		hunters.add(p);
		if(runners.contains(p)) {
			runners.remove(p);
		}
	}
	public void addRunner(Player p) {
		runners.add(p);
		
		if(hunters.contains(p)) {
			hunters.remove(p);
		}
	}
	
	public void setRunnerToSpectator(Player p) {
		runners.remove(p);
		runnerSpectator.add(p);
		p.setGameMode(GameMode.SPECTATOR);
		p.setHealth(20);
		p.setFoodLevel(20);
	}
	
	public ArrayList<Player> getPlayerWhoMustChose() {
		return playerWhoMustChose;
	}
	public ArrayList<Player> getRunnerSpectators() {
		return runnerSpectator;
	}
	public ArrayList<Player> getRunners() {
		return this.runners;
	}
	public ArrayList<Player> getHunters() {
		return this.hunters;
	}
	
	public boolean huntersAreReleased() {
		return huntersReleased;
	}
	
	public void startManHunt() {
		time = 30;
		if(game != null) {
			game.createWorld();
			game.setTimerLevel(30);	
			
			for(Player player : game.getPlayingPlayers()) {
				GUI_ManHunt.sendGUI(player);
				player.getInventory().addItem(getTeamChoser());
				player.setAllowFlight(true);
			}
			
			timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
				
				@Override
				public void run() {
					
			
						
					if(time > 0) {
						if(!runners.isEmpty() && !hunters.isEmpty() && game.getWorld() != null) {
						if(time <= 10) {
							game.broadCastMessage("&6ManHunt &7is starting in &e" + time + " &7second(s).");
						}
						time--;
						game.setTimerLevel(time);
						}
					} else {
						
						if(!runners.isEmpty() && !hunters.isEmpty() && game.getWorld() != null) {
							if(playerWhoMustChose.size() >= 1) {
							for(Player p : getPlayerWhoMustChose()) {
								if(!getRunners().contains(p) && !getHunters().contains(p)) {
								Random random = new Random();
								int number = random.nextInt(2);
								if(number == 0) {
									addHunter(p);
								} else if(number == 1) {
									addRunner(p);
								} else if(number == 2) {
									addHunter(p);
								}
								}
							}
							}
							
						
							
							
						game.setGameState(GameState.STARTED);
						Bukkit.getScheduler().cancelTask(timer);
						game.setTimerLevel(0);
						game.broadCastMessage("&6ManHunt &7started the hunters will be released in 20 seconds be quick!");
						
						try {
							for(Player player : game.getPlayingPlayers()) {
								if(game.getWorld() != null && game.getWorld().getSpawnLocation() != null) {
								player.teleport(game.getWorld().getSpawnLocation());
								player.setAllowFlight(false);
								} else {
									game.broadCastMessage("&cThe world hasn't been loaded yet so the game will start in a minute.");
									time = 30;
									break;
								}
								player.getInventory().clear();
								player.setGameMode(GameMode.SURVIVAL);
								}
							startHunterCooldown();
						} catch (Exception ex) {
							game.broadCastMessage("&cThe world hasn't been loaded yet so the game will start in a minute.");
							time = 30;
						}
						} else {
							if(runners.isEmpty()) {
								game.broadCastMessage("&cThe game can't start because there are not enough runners.");
							} else if(hunters.isEmpty()) {
								game.broadCastMessage("&cThe game can't start because there are not enough hunters.");
							} else {
								game.broadCastMessage("&4ERROR &cCan't start the game.");
							}
						}
					}
				}
			}, 0L, 20L);
		}
	}
	
	public void startHunterCooldown() {
		time = 20;
		timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run() {
				if(time > 0) {
					time--;
					game.setTimerLevel(time);
				} else {
					Bukkit.getScheduler().cancelTask(timer);
					game.broadCastMessage("&6ManHunt &7The hunters have been released.");
					huntersReleased = true;
					for(Player p : getHunters()) {
						p.getInventory().addItem(getCompass());
					}
				}
			}
		}, 0L, 20L);
	}
	
	
	/*
	 * static
	 */
	
	public static Set<String> getManhuntGames() {
		return games.keySet();
	}
	
	public static ManHunt getManHuntGame(String gameID) {
		return games.get(gameID);
	}
	
	public static ItemStack getCompass() {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(Chat.format("&7Right click on with the compass in youre hand to let it point to the nearest runner."));
		ItemStack compass = ItemCreator.createItem(Material.COMPASS, "&ePlayerTracker", lore);
		return compass;
	}
	
	public static ItemStack getTeamChoser() {
		ArrayList<String> lore = new ArrayList<String>();
		ItemStack tc = ItemCreator.createItem(Material.CHEST, "&eTeam Choser", lore);
		return tc;
	}

	

}
