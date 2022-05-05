package snapje.challenges.API;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;


public class VersionAPI {
	
	public static double getVersion() {
		String string = Bukkit.getServer().getVersion();
		if(string != null) {
		if(string.contains("1.7") || string.contains("1_7")) {
			return 1.7;
		}  else if(string.contains("1.8") || string.contains("1_8")) {
			return 1.8;
		} else if(string.contains("1.9") || string.contains("1_9")) {
			return 1.9;
		} else if(string.contains("1.10") || string.contains("1_10")) {
			return 1.10;
		} else if(string.contains("1.11") || string.contains("1_11")) {
			return 1.11;
		} else if(string.contains("1.12") || string.contains("1_12")) {
			return 1.12;
		} else if(string.contains("1.13") || string.contains("1_13")) {
			return 1.13;
		} else if(string.contains("1.14") || string.contains("1_14")) {
			return 1.14;
		} else if(string.contains("1.15") || string.contains("1_15")) {
			return 1.15;
		} else if(string.contains("1.16") || string.contains("1_16")) {
			return 1.16;
		} else if(string.contains("1.17") || string.contains("1_17")) {
			return 1.17;
		} else if(string.contains("1.18") || string.contains("1_18")) {
			return 1.18;
		}
		}
		return 0;
	}
 	
	public static String getNMSVersion() {
		// net.mincecraft.server.v1_8_R3.****;
		// org.bukkit.server.v1_8_R3.****;
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		return version;
	}
	
	public static Class<?> getNMSClass(String name) {
		// net.mincecraft.server.v1_8_R3.****;
		// org.bukkit.server.v1_8_R3.****;
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
		return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException ex) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cClass not found!"));
			return null;
		}
	}

}
