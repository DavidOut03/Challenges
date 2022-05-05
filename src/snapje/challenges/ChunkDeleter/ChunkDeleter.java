package snapje.challenges.ChunkDeleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import snapje.challenges.Core.Main;

public class ChunkDeleter {
	
	private static HashMap<Chunk, ChunkDeleter> chunkList = new HashMap<Chunk, ChunkDeleter>();
	private Location loc;
	private Player activator;
	private Chunk c;
	private int run;
	
	public ChunkDeleter(Player p, Location loc) {
		this.activator = p;
		this.loc = loc;
		this.c = loc.getChunk();
		chunkList.put(this.c, this);
	}
	
	public static boolean isDeleted(Location loc) {
		if(getChunk(loc) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public static ChunkDeleter getChunk(Location loc) {
		return chunkList.get(loc.getChunk());
	}
	
	public static Set<Chunk> getDestroyedChunks() {
		return chunkList.keySet();
	}
	
	
	public Chunk getChunk() {
		return this.c;
	}
	public Player getActivator() {
		return this.activator;
	}
	public Location getLocation() {
		return this.loc;
	}
	
	public void start() {
		run = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			int step = 0;
			@Override
			public void run() {
				if(step < 16) {
					for(int y = 1; y < 255; y++) {
						for(int x = 0; x < 16; x++) {
							if(c.getBlock(x, y, step) != null && c.getBlock(x, y, step).getType() != Material.AIR) {
							c.getBlock(x, y, step).setType(Material.AIR);
							}
						}
					}
					step++;
				} else {
					Bukkit.getScheduler().cancelTask(run);
				}
				
			}
		}, 50, 20);
	}
	
	
    private static boolean isEnabled = false;
	
	public static boolean chunkDeleterIsEnabled() {
		return isEnabled;
		}
	
	public static void setEnabled() {
		isEnabled = true;
	}
	
	public static void setDisabled() {
		isEnabled = false;
	}

}
