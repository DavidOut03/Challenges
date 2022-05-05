package snapje.challenges.ChunkDeleter;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import snapje.challenges.Core.Main;
import snapje.challenges.Utils.Chat;

public class Event_ChunkDeleter implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
		if(ChunkDeleter.chunkDeleterIsEnabled() == true) {
			if(!ChunkDeleter.isDeleted(e.getPlayer().getLocation()) && e.getPlayer().getLocation().getChunk() != e.getPlayer().getWorld().getSpawnLocation().getChunk()) {
				Location loc = e.getPlayer().getLocation();
				e.getPlayer().sendMessage(Chat.format("&6" + e.getPlayer().getName() + " &7walked into a new chunk, this chunk will be destroyed!"));
				ChunkDeleter cd = new ChunkDeleter(e.getPlayer(), loc);
				cd.start();
		}
		}
	}

}
