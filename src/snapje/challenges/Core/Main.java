package snapje.challenges.Core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import snapje.challenge.Challenge.CMD_Game;
import snapje.challenge.Challenge.GUI_Challenge;
import snapje.challenge.Challenge.GUI_ChooseType;
import snapje.challenge.Challenge.GUI_List;
import snapje.challenge.Challenges.GUI_ManHunt;
import snapje.challenges.Events.Events;
import snapje.challenges.Events.Events_DeathSwap;
import snapje.challenges.Events.Events_ManHunt;

public class Main extends JavaPlugin {

	
	public void onEnable() {
		registerCommands();
		registerEvents();
		registerGUI();
	}
	
	public void registerCommands() {
		getCommand("game").setExecutor(new CMD_Game());
	}
	
	public void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Events(), this);
		pm.registerEvents(new Events_DeathSwap(), this);
		pm.registerEvents(new Events_ManHunt(), this);
	}
	
	public void registerGUI() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new GUI_Challenge(), this);
		pm.registerEvents(new GUI_List(), this);
		pm.registerEvents(new GUI_ChooseType(), this);
		pm.registerEvents(new GUI_ManHunt(), this);
	}
}
