package snapje.challenges.ChunkDeleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import snapje.challenges.Utils.Chat;

public class CMD_Chunkdeleter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(sender.isOp()) {
		if(args.length == 0) {
		
			if(ChunkDeleter.chunkDeleterIsEnabled() == true) {
				ChunkDeleter.setDisabled();
				sender.sendMessage(Chat.format("&7You toggled chunkdeleter &cOFF&7!"));
			} else {
				ChunkDeleter.setEnabled();
				sender.sendMessage(Chat.format("&7You toggled chunkdeleter &aON&7!"));
			}
			
		} else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("on")) {
				ChunkDeleter.setEnabled();
				sender.sendMessage(Chat.format("&7You toggled chunkdeleter &aON&7!"));
			} else if(args[0].equalsIgnoreCase("off")) {
				ChunkDeleter.setDisabled();
				sender.sendMessage(Chat.format("&7You toggled chunkdeleter &cOFF&7!"));
			}
		}
		} else {
			sender.sendMessage(Chat.format("&CYou don't have permisson for this."));
		}
		return false;
	}

}
