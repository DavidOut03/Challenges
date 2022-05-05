package snapje.challenge.Challenges;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import snapje.challenges.API.GameType;
import snapje.challenges.API.ItemCreator;
import snapje.challenges.API.VersionAPI;
import snapje.challenges.Utils.Chat;

public class GUI_ManHunt implements Listener {
	
	public static void sendGUI(Player p) {
		Inventory inv = Bukkit.createInventory(p, 27, Chat.format("&6Choose a team."));
		
		ArrayList<String> rl = new ArrayList<String>();
		rl.add(Chat.format("&7Click to become a runner."));
		
		ArrayList<String> hl = new ArrayList<String>();
		hl.add(Chat.format("&7Click to become a hunter."));
		
		
		ItemStack runner;
		ItemStack lighpane;
		ItemStack darkgraypane;
		ItemStack hunter;
		if(VersionAPI.getVersion() >= 1.13) {
		 runner = ItemCreator.createItem(Material.getMaterial("LEATHER_BOOTS"), "&eRunner");
		 lighpane = ItemCreator.createItem(Material.getMaterial("LIGHT_GRAY_STAINED_GLASS_PANE"), " ");
		 darkgraypane = ItemCreator.createItem(Material.getMaterial("GRAY_STAINED_GLASS_PANE"), " ");
		 hunter= ItemCreator.createItem(Material.getMaterial("DIAMOND_SWORD"), "&cHunter", hl);
		} else {
			runner = ItemCreator.createItem(Material.getMaterial("LEATHER_BOOTS"), "&eRunner", null, 1, 1);
		lighpane = ItemCreator.createItem(Material.getMaterial("STAINED_GLASS_PANE"), " ", 1, 8);
		darkgraypane = ItemCreator.createItem(Material.getMaterial("STAINED_GLASS_PANE"), " ", 1, 7);
		hunter = ItemCreator.createItem(Material.getMaterial("DIAMOND_SWORD"), "&cHunter", hl, 1, 0);
		}
		
		inv.setItem(10, lighpane);
		inv.setItem(11, lighpane);
		inv.setItem(12, runner);
		inv.setItem(13, lighpane);
		inv.setItem(14, hunter);
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
	public void onClick(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().equals(ManHunt.getTeamChoser())) {
				sendGUI(e.getPlayer());
			}
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(e.getItemDrop().getItemStack().equals(ManHunt.getCompass())||e.getItemDrop().getItemStack().equals(ManHunt.getTeamChoser())) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getWhoClicked() != null && e.getClickedInventory() != e.getWhoClicked().getInventory()) {
			if(e.getView().getTitle() != null && e.getView().getTitle().equalsIgnoreCase(Chat.format("&6Choose a team.")) && e.getWhoClicked() instanceof Player) {
				if(e.getCurrentItem() != null) {
					e.setCancelled(true);
					Player p = (Player) e.getWhoClicked();
					if(e.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
						Game game = Game.getPlayersGame(p);
						ManHunt mh = ManHunt.getManHuntGame(game.getGameID());;
						
						if(game != null && mh != null) {
							mh.addHunter(p);
							game.broadCastMessage("&b" + p.getName() + " &7joined the Runners team. (" + mh.getHunters().size() + ")");
						}
						p.sendMessage(Chat.format("&6ManHunt &7You chose to become a hunter."));
						e.setCancelled(true);
						p.closeInventory();
					} else if(e.getCurrentItem().getType() == Material.getMaterial("LEATHER_BOOTS")) {
						Game game = Game.getPlayersGame(p);
						ManHunt mh = ManHunt.getManHuntGame(game.getGameID());;
						
						if(game != null && mh != null) {
							mh.addRunner(p);
							game.broadCastMessage("&b" + p.getName() + " &7joined the Runners team. (" + mh.getRunners().size() + ")");
						}
						p.sendMessage(Chat.format("&6ManHunt &7You chose to become a runner."));
						p.closeInventory();
						e.setCancelled(true);
					} 
				}
			}
		}
	}

}
