package net.angusbeefgaming.pets.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.djrapitops.antimatter.filters.AntiIP;
import com.djrapitops.antimatter.filters.AntiReplacer;

import net.angusbeefgaming.pets.Config;
import net.angusbeefgaming.pets.manager.PetManager;
import net.md_5.bungee.api.ChatColor;

public class NamePetCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		if(!player.hasPermission(Config.USE_COMMAND)) {
			player.sendMessage(Config.NO_PERM_COLOR + Config.NO_PERM_MESSAGE);
			return false;
		}
		
		if(!PetManager.hasPetSpawned(player)) {
			player.sendMessage(ChatColor.RED + "You do not have a pet spawned!");
			return false;
		}
		
		if(args.length < 1) {
			player.sendMessage(ChatColor.RED + "You need to give your pet a name!");
			return false;
		}
		
        StringBuilder message = new StringBuilder();

        for(int i = 0; i < args.length; ++i) {
        	message = message.append(args[i] + " ");
        }
        
        String petName = message.substring(0, message.length() - 1);
        
        // First check for any kind of ip's or addresses, if so, dont allow it
        
        if(!AntiIP.pass(petName)) {
        	player.sendMessage(ChatColor.RED + "Please do not try to advertise in your pet's name!");
        	return false;
        }
        
        // Then we will check for any swear words based on the api's default rules
        AntiReplacer replacer = new AntiReplacer();
        
        String filteredName = replacer.parseMessage(petName);
		
        if(filteredName.equals("jeb_")) {
        	if(!player.hasPermission(Config.SHEEP_RAINBOW)) {
        		player.sendMessage(ChatColor.RED + "You have not purchased the Rainbow Package Yet!");
        		return false;
        	}
        }
        
        if(filteredName.equals("Dinnerbone")) {
        	if(!player.hasPermission(Config.PET_UPSIDEDOWN)) {
        		player.sendMessage(ChatColor.RED + "You have not purchased the Upsidedown Package Yet!");
        		return false;
        	}
        }
        
		PetManager.namePet(player, filteredName);
		player.sendMessage(ChatColor.GREEN + "You have named your pet: " + filteredName);
		return true;
	}
}
