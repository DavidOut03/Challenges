package snapje.challenge.Challenge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import snapje.challenge.Challenges.DeathSwap;
import snapje.challenge.Challenges.Game;
import snapje.challenge.Challenges.ManHunt;
import snapje.challenges.API.GameState;
import snapje.challenges.API.GameType;
import snapje.challenges.API.VersionAPI;
import snapje.challenges.Utils.Chat;

public class CMD_Game implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("leave")) {
					if(Game.playerIsInGame(p) == true  && Game.getPlayersGame(p) != null) {
						Game game = Game.getPlayersGame(p);
						if(game.getHost() == p.getName() && game.getGameState().equals(GameState.INLOBBY)) {
							game.broadCastMessage("&cThe host left the game.");
							game.endGame();
						} else {
						game.leaveGame(p);
						p.sendMessage(Chat.format("&7You successfully left the game."));
						}
					} else {
						p.sendMessage(Chat.format("&cYoure not in a game right now."));
					}
				} else if(args[0].equalsIgnoreCase("forcestart")) {
					
				} else {
					p.sendMessage(Chat.format("&CUse: &7/game leave"));
				}
			} else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("join")) {
					String gameID = args[1];
					
					if(Game.playerIsInGame(p) == false) {
					if(Game.getGame(gameID) != null) {
						Game game = Game.getGame(gameID);
						if(game.playerCanJoin() == true) {
							game.joinGame(p);
							p.sendMessage(Chat.format("&7You successfully joined &E" + gameID + "&7."));
						} else {
							p.sendMessage(Chat.format("&cThe game already started but you can spectate the game."));
							game.joinGame(p);
							game.setPlayerToSpectator(p);
						}
					} else {
						p.sendMessage(Chat.format("&cGame not found."));
					}
					} else {
						p.sendMessage(Chat.format("&cYoure already in a game."));
					}
				} else {
					p.sendMessage(Chat.format("&CUse: &7/game join [gameID]"));
				}
			} else {
				GUI_Challenge.sendGUI(p);
			}
			
		}
		return false;
	}

}
