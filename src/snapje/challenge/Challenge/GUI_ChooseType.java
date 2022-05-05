package snapje.challenge.Challenge;

import java.util.ArrayList;

import org.bukkit.Bukkit;
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

public class GUI_ChooseType implements Listener {
	
	@SuppressWarnings("deprecation")
	public static void sendGUI(Player p) {
		Inventory inv = Bukkit.createInventory(p, 27, Chat.format("&eChallenges"));
		
		ArrayList<String> sc = new ArrayList<String>();
		sc.add(Chat.format("&7Click to start a new challenge."));
		
		ArrayList<String> vc = new ArrayList<String>();
		vc.add(Chat.format("&7Click to see the list off started Challenges."));
		
		
		ItemStack deathSwap;
		ItemStack lighpane;
		ItemStack darkgraypane;
		ItemStack manHunt;
		if(VersionAPI.getVersion() >= 1.13) {
		 deathSwap = ItemCreator.createItem(Material.getMaterial("ENDER_PEARL"), "&cDeathSwap");
		 lighpane = ItemCreator.createItem(Material.getMaterial("LIGHT_GRAY_STAINED_GLASS_PANE"), " ");
		 darkgraypane = ItemCreator.createItem(Material.getMaterial("GRAY_STAINED_GLASS_PANE"), " ");
		 manHunt= ItemCreator.createItem(Material.getMaterial("IRON_SWORD"), "&aChallenge list", vc);
		} else {
		deathSwap = ItemCreator.createItem(Material.getMaterial("skull"), "&cDeathSwap", null, 1, 1);
		lighpane = ItemCreator.createItem(Material.getMaterial("STAINED_GLASS_PANE"), " ", 1, 8);
		darkgraypane = ItemCreator.createItem(Material.getMaterial("STAINED_GLASS_PANE"), " ", 1, 7);
		manHunt = ItemCreator.createItem(Material.getMaterial("IRON_SWORD"), "&aChallenge list", vc, 1, 0);
		}
		
		inv.setItem(10, lighpane);
		inv.setItem(11, lighpane);
		inv.setItem(12, deathSwap);
		inv.setItem(13, lighpane);
		inv.setItem(14, manHunt);
		inv.setItem(16, lighpane);
		inv.setItem(15, lighpane);
		
		for(int i = 0; i < 27; i++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) {
			inv.setItem(i, darkgraypane);
		
			}
		}
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getWhoClicked() != null && e.getClickedInventory() != e.getWhoClicked().getInventory()) {
			if(e.getView().getTitle() != null && e.getView().getTitle().equalsIgnoreCase(Chat.format("&eChallenges")) && e.getWhoClicked() instanceof Player) {
				if(e.getCurrentItem() != null) {
					e.setCancelled(true);
					Player p = (Player) e.getWhoClicked();
					if(e.getCurrentItem().getType() == Material.IRON_SWORD) {
						Game game = new Game(p, GameType.ManHunt);
						game.inLobby(45);
						p.closeInventory();
					} else if(VersionAPI.getVersion() >= 1.13 && e.getCurrentItem().getType() == Material.getMaterial("ENDER_PEARL")) {
						Game game = new Game(p, GameType.DeathSwap);
						game.inLobby(45);
						p.closeInventory();
					} else if(VersionAPI.getVersion() < 13 && e.getCurrentItem().getType() == Material.getMaterial("skull")) {
						Game game = new Game(p, GameType.DeathSwap);
						game.inLobby(45);
						p.closeInventory();
					}
				}
			}
		}
	}

}
