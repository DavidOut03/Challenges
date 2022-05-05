package snapje.challenge.Challenge;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import snapje.challenge.Challenges.DeathSwap;
import snapje.challenge.Challenges.Game;
import snapje.challenge.Challenges.ManHunt;
import snapje.challenges.API.GameType;
import snapje.challenges.API.ItemCreator;
import snapje.challenges.API.VersionAPI;
import snapje.challenges.Utils.Chat;

public class GUI_List implements Listener {
	
	public static void sendGUI(Player p) {
		Inventory inv = Bukkit.createInventory(p, 27, Chat.format("&eGames")); 
		
		for(String gameID : Game.getGames()) {
			Game game = Game.getGame(gameID);
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(Chat.format("&7GameID: &E" + gameID));
			lore.add(Chat.format("&7GameType: &e" + game.getGameType()));
			lore.add(Chat.format("&7State: &e" + game.getGameState()));
			lore.add(Chat.format("&7Players: &e" + Game.getGame(gameID).getPlayingPlayers().size()));
			
			if(Game.getGame(gameID).getGameType() == GameType.DeathSwap) {
			ItemStack deathSwap;
			if(VersionAPI.getVersion() >= 1.13) {
		     deathSwap = ItemCreator.createItem(Material.getMaterial("ENDER_PEARL"), "&cDeathSwap", lore);
			} else {
			deathSwap = ItemCreator.createItem(Material.getMaterial("skull"), "&cDeathSwap", lore, 1, 1);
			}
			inv.addItem(deathSwap);
			} else if(game.getGameType() == GameType.ManHunt) {
			ItemStack manHunt;
			if(VersionAPI.getVersion() >= 1.13) {
			     manHunt = ItemCreator.createItem(Material.getMaterial("IRON_SWORD"), "&aManHunt", lore);
				} else {
				manHunt = ItemCreator.createItem(Material.getMaterial("IRON_SWORD"), "&aManHunt", lore, 1, 1);
				}
				inv.addItem(manHunt);
			}
		}
		
		
		
	
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getWhoClicked() != null && e.getClickedInventory() != e.getWhoClicked().getInventory()) {
			if(e.getView().getTitle() != null && e.getView().getTitle().equalsIgnoreCase(Chat.format("&eGames")) &&e.getWhoClicked() instanceof Player) {
				if(e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null) {
					Player p = (Player) e.getWhoClicked();
					String line = e.getCurrentItem().getItemMeta().getLore().get(0);
					String[] split = line.split(" ");
					String id = ChatColor.stripColor(split[1]);
					
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Chat.format("&cDeathSwap"))) {
						p.performCommand("game join " + id);
					
						e.setCancelled(true);
						p.closeInventory();
					} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Chat.format("&aManHunt"))) {
						p.performCommand("game join " + id);
						
						e.setCancelled(true);
						p.closeInventory();
					}
				} else {
					e.setCancelled(true);
				}
			}
		}
	}

}
