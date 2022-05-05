package snapje.challenge.Challenges;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import snapje.challenges.API.GameState;
import snapje.challenges.API.GameType;
import snapje.challenges.Core.Main;
import snapje.challenges.Utils.Chat;

public class Game {

	private static HashMap<Player, Location> playerNeedsToRespawn = new HashMap<Player, Location>();
	private static HashMap<String, Game> games = new HashMap<String, Game>();
	private GameType type;
	private GameState state;
	private String hostName;
	private String gameID;
	private Player winner;
	
	private ArrayList<Player> spectators = new ArrayList<Player>();
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private int timer;
	private int time;
	
	private World w;
	
	private HashMap<Player, Location> locs = new HashMap<Player, Location>();
	
	public Game(Player host, GameType type) {
		this.hostName = host.getName();
		this.type = type;
		this.state = GameState.INLOBBY;
		if(type.equals(GameType.DeathSwap)) {
			this.gameID = "DS-" + games.size() + 1;
		} else if(type.equals(GameType.ManHunt)) {
			this.gameID = "MH-" + games.size() + 1;
		} else {
			this.gameID = games.size() + "";
		}
		joinGame(host);
		host.sendMessage(Chat.format("&9&l &7You successfully started a &b" + type.toString() + " &7game." ));
		this.w = host.getWorld();
	}
	
	public ArrayList<Player> getPlayingPlayers() {
		return this.players;
	}
	public void addPlayer(Player p) {
		players.add(p);
	}
	public void removePlayer(Player p) {
		players.remove(p);
	}
	
	public ArrayList<Player> getSpectators() {
		return spectators;
	}
	public void addSpectator(Player p) {
		spectators.add(p);
	}
	public void removeSpectator(Player p) {
		spectators.remove(p);
	}
	public Player getWinner() {
		return winner;
	}
	public void setWinner(Player p) {
		this.winner = p;
	}

	
	public void setGameState(GameState state) {
		this.state = state;
	}
	public GameState getGameState() {
		return this.state;
	}
	public GameType getGameType() {
		return this.type;
	}
	public String getGameID() {
		return this.gameID;
	}
	public void setGameID(String id) {
		this.gameID = id;
	}
	public String getHost() {
		return this.hostName;
	}
	public World getWorld() {
		return w;
	}
	
	public int getTimer() {
		return timer;
	}
	public int getLeftTime() {
		return time;
				
	}
	
	public boolean playerIsSpectator(Player p) {
		boolean is = false;
		
		if(spectators.contains(p)) {
			is = true;
		}
		
		return is;
	}
	public boolean playerCanJoin() {
		if(this.state == GameState.INLOBBY) {
			return true;
		} else {
			return false;
		}
	}
	public void joinGame(Player p) {
		broadCastMessage("&b" + p.getName() + " &7joined the game.");
		players.add(p);
		locs.put(p, p.getLocation());
		p.setGameMode(GameMode.ADVENTURE);
		p.getInventory().clear();
		p.setLevel(0);
		p.setFlying(false);
		p.setAllowFlight(false);
		p.setHealth(20);
		p.setFoodLevel(20);
		
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(players.contains(online)) {
				p.showPlayer(online);
				online.showPlayer(p);
			} else {
				p.hidePlayer(online);
				online.hidePlayer(p);
			}
		}
	}
	public void quitGame(Player p) {
		if(!p.isDead()) {
		if(locs.get(p) != null) {
			p.teleport(locs.get(p));
		}
	
		p.setGameMode(GameMode.ADVENTURE);
		p.getInventory().clear();
		p.setLevel(0);
		p.setFlying(false);
		p.setAllowFlight(false);
		p.setHealth(20);
		p.setFoodLevel(20);
		
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(getPlayingPlayers() != null && !getPlayingPlayers().isEmpty() && getPlayingPlayers().contains(online)) {
				p.hidePlayer(online);
			} else if(getSpectators() != null && !getSpectators().isEmpty() && getSpectators().contains(online)) {
				p.hidePlayer(online);
			} else {
				p.showPlayer(online);
			}
		}
		if(getSpectators() != null && !getSpectators().isEmpty() &&getSpectators().contains(p)) {
			removeSpectator(p);
		}
		if(getPlayingPlayers() != null &&!getPlayingPlayers().isEmpty() && getSpectators().contains(p)) {
			removePlayer(p);
		}
		}
	}
	public void leaveGame(Player p) {
		quitGame(p);
		broadCastMessage("&c" + p.getName() + " &7left the game!");
		if(players.size() == 1 && state.equals(GameState.STARTED)) {
			setWinner(players.get(0));
			endGame();
		}
	}
	@SuppressWarnings("deprecation")
	public void setPlayerToSpectator(Player p) {
		removePlayer(p);
		addSpectator(p);
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(true);
		p.setFlying(true);
		p.setLevel(0);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.getActivePotionEffects().clear();
		
		for(Player player : players) {
			player.hidePlayer(p);
		}
		
		if(players.size() == 1) {
			setWinner(players.get(0));
			endGame();
		}
	}
	
	public void createWorld() {
		WorldCreator wc = new WorldCreator(gameID.replace("-", "_"));
		wc.generateStructures(true);
		wc.environment(Environment.NORMAL);
		wc.type(WorldType.NORMAL);
		//this.w = wc.createWorld();
	}
	public void deleteWorld() {
		if(getWorld() != null) {
		
		
			
			
		try {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.getWorld().getName() == getWorld().getName()) {
					for(World world : Bukkit.getWorlds()) {
						if(!world.getName().equals(getWorld().getName())) {
							p.kickPlayer("&cResetting world!");
						}
					}
				}
			}
			
			Bukkit.unloadWorld(w.getName(), false);
			FileUtils.deleteDirectory(new File(w.getName()));
		} catch (IOException ex) {Bukkit.getConsoleSender().sendMessage(Chat.format("&c" + w.getName() + " could not be removed."));}
		}
	}
	public boolean worldIsLoaded() {
		boolean is = false;
		
		for(World w : Bukkit.getWorlds()) {
			w.getName().equalsIgnoreCase(getWorld().getName());
		}
		
		return is;
	}
	

	public void inLobby(int seconds) {
		games.put(gameID, this);
		setGameState(GameState.INLOBBY);
		
		TextComponent c1 = new TextComponent(Chat.format("&9&lGame &3" + hostName + " &7started a &b" + type.toString() + " &7game."));
		TextComponent c2 = new TextComponent(Chat.format(" &b[CLICK]"));
		c1.addExtra(c2);
		c2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/game join " + gameID));
		c2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to join the game.").color(ChatColor.GRAY).create()));
		
		if(Bukkit.getOnlinePlayers() != null) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(playerIsInGame(p) == false && Game.getPlayersGame(p) == null) {
				p.spigot().sendMessage(c1);
			}
		}
		}
		
		
		time = seconds;
		
		timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			int lastPoint = seconds;
			int advertise = 15;
			
			Player host = Bukkit.getPlayer(getHost());
			
			@Override
			public void run() {
				if(players.size() > 1) {
				if(time > 0) {
					if(players.size() != 0) {
					if((lastPoint - time) >= 15) {
						if(Bukkit.getOnlinePlayers() != null) {
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(playerIsInGame(p) == false && Game.getPlayersGame(p) == null) {
								p.spigot().sendMessage(c1);
							}
						}
						lastPoint = time;
						}
					}
					
					setTimerLevel(time);
				
					if(time <= 10) {
						broadCastMessage("&7Game is going to start in &e" + time + " &7seconds.");
					}
					time--;
				} else {
					Bukkit.getScheduler().cancelTask(timer);
				}
				} else {
					if(players.size() > 1) {
					Bukkit.getScheduler().cancelTask(timer);
					setTimerLevel(0);
					broadCastMessage("&7The game started other players can't join anymore.");
					startGame();
					timer = 0;
					time = 0;
					} else {
						time = 45;
						lastPoint = 45;
						broadCastMessage("&cNot enough players to start with.");
					}
				}
				
			} else if(host != null && players.contains(host)) {
				if(advertise > 0) {
					advertise--;
				} else {
					advertise = 15;
					time = 45;
					if(Bukkit.getOnlinePlayers() != null) {
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(playerIsInGame(p) == false && Game.getPlayersGame(p) == null) {
								p.spigot().sendMessage(c1);
							}
						}
						}
				}
			} else {
				Bukkit.getScheduler().cancelTask(timer);
				setGameState(GameState.STOPPED);
				games.remove(gameID);
			}
		}
		}, 0L, 20L);
		
	}
	public void startGame() {
		
		setGameState(GameState.STARTING);
		if(type.equals(GameType.DeathSwap)) {
			createWorld();
			setGameID("DS-" + games.size() + 1);
			DeathSwap swap = new DeathSwap(this);
			swap.startDeathSwap();
		} else if(type.equals(GameType.ManHunt)) {
			createWorld();
			setGameID("MH-" + games.size() + 1);
			ManHunt hunt = new ManHunt(this);
			hunt.startManHunt();

		}
	}
	public void endGame() {
		setGameState(GameState.STOPPING);
		broadCastMessage("QuitWinner1");
		if(winner != null) {
		broadCastMessage("&cDeathSwap &e&l" + winner.getName() + " &7won the game.");
		quitGame(winner);
		broadCastMessage("QuitWinner2");
		} else {
			broadCastMessage("&cDeathSwap &7has been stopped.");
			for(Player p : getPlayingPlayers()) {
				quitGame(p);
				setTimerLevel(0);
			}
		}
		broadCastMessage("QuitSpectators1");
		if(getSpectators() != null && !getSpectators().isEmpty()) {
			
				try {
					for(Player p : getSpectators()) {
						if(getSpectators().contains(p)) {
				quitGame(p);
						}
					}
				} catch (Exception ex) {
				
				}
			}
			
		
		broadCastMessage("QuitSpectators2");
		setGameState(GameState.STOPPED);
		broadCastMessage("trying to delete world.");
		try {
			deleteWorld();
			} catch (Exception ex) {Bukkit.getConsoleSender().sendMessage(Chat.format("&cCouldn't delete the world."));}
		games.remove(gameID);
	}
	
	
	/*
	 *  Player Features
	 */
	
	public void broadCastMessage(String message) {
		for(Player p : players) {
			p.sendMessage(Chat.format(message));
		}
		for(Player p : spectators) {
			p.sendMessage(Chat.format(message));
		}
		
	}
	public void setTimerLevel(int level) {
		for(Player p : players) {
			p.setLevel(level);
		}
		for(Player p : spectators) {
			p.setLevel(level);
		}
	}
	
	
	/*
	 *  static
	 */
	
	public static Set<String> getGames() {
		return games.keySet();
	}
	
	public static Game getGame(String gameID) {
		return games.get(gameID);
	}
	
	public static boolean playerIsInGame(Player p) {
		if(getGames() != null) {
		for(String gameID : getGames()) {
			if(getGame(gameID).getPlayingPlayers().contains(p) || getGame(gameID).getSpectators().contains(p)) {
				return true;
			}
		}
		}
		return false;
	}

	public static Game getPlayersGame(Player p) {
		Game game = null;
		
		if(getGames() != null) {
		for(String gameID : getGames()) {
			if(getGame(gameID).getPlayingPlayers().contains(p)) {
				game = getGame(gameID);
			} else if(getGame(gameID).getSpectators().contains(p)) {
				game = getGame(gameID);
			}
		}
		}
		
		return game;
	}
	
}
