package snapje.challenges.API;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import snapje.challenges.Utils.Chat;




public class Packet {

	public static void sendPacket(Player p, Object packet) {
		 try {
             Object handle = p.getClass().getMethod("getHandle").invoke(p);
             Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
             playerConnection.getClass().getMethod("sendPacket", VersionAPI.getNMSClass("Packet")).invoke(playerConnection, packet);
     } catch (Exception ex) {}
		
	}
	
	public static void sendTitle(Player p, String message, int fadeInTicks, int displayTicks, int fadeOutTicks) {
		try {
		Object enumTitle = VersionAPI.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
		 Object chat = VersionAPI.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + Chat.format(message) +     "\"}");
			
	    Constructor<?> titleConstructor =VersionAPI.getNMSClass("PacketPlayOutTitle").getConstructor(VersionAPI.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], VersionAPI.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
        Object packet = titleConstructor.newInstance(enumTitle, chat, fadeInTicks, displayTicks, fadeOutTicks);
		sendPacket(p, packet);
		} catch (Exception ex) {}

	
		
	}
	
	public static void sendTitleAndSubTitle(Player p, String title,String subtitle, int fadeInTicks, int displayTicks, int fadeOutTicks) {
		try {
			Object enumTitle = VersionAPI.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
			Object chat = VersionAPI.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + Chat.format(title) + "\"}");
				
		    Constructor<?> titleConstructor = VersionAPI.getNMSClass("PacketPlayOutTitle").getConstructor(VersionAPI.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], VersionAPI.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
	        Object packet = titleConstructor.newInstance(enumTitle, chat, fadeInTicks, displayTicks, fadeOutTicks);
			sendPacket(p, packet);
			} catch (Exception ex) {}
		
		try {
			Object enumSubTitle = VersionAPI.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
			Object chat = VersionAPI.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + Chat.format(subtitle) +     "\"}");
				
		    Constructor<?> titleConstructor = VersionAPI.getNMSClass("PacketPlayOutTitle").getConstructor(VersionAPI.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], VersionAPI.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
	        Object packet = titleConstructor.newInstance(enumSubTitle, chat, fadeInTicks, displayTicks, fadeOutTicks);
			sendPacket(p, packet);
			} catch (Exception ex) {}
	}
	
	public static void sendActionBar(Player p, String message) {
	
		try {
			Object chatSerializer = VersionAPI.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + Chat.format(message) + "\"}");
			Constructor<?> barConstructor = VersionAPI.getNMSClass("PacketPlayOutChat").getConstructor(VersionAPI.getNMSClass("IChatBaseComponent"), byte.class);
			Object packet = barConstructor.newInstance(chatSerializer, (byte) 2);
			sendPacket(p, packet);
		} catch (Exception ex) {
			Bukkit.getConsoleSender().sendMessage(Chat.format("&6Version: &e" + VersionAPI.getNMSVersion() + " | " + VersionAPI.getVersion()));
			Bukkit.getConsoleSender().sendMessage(Chat.format("&cCouldn't send actionbar to "  + p.getName()));
		}
	}
	
	public static void instantRespawn(Player p) {
		try {
			
			Object enumRespawn = VersionAPI.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
				
		    Constructor<?> titleConstructor = VersionAPI.getNMSClass("PacketPlayOutTitle").getConstructor(VersionAPI.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0]);
	        Object packet = titleConstructor.newInstance(enumRespawn);
			sendPacket(p, packet);
		} catch (Exception ex) {
			p.sendMessage(Chat.format("&cSorry but we couldn't let you respawn."));
		}
	}
	
	

}
