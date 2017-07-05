package me.rampen88.credits.player;

import com.gmail.nossr50.datatypes.skills.SkillType;
import me.rampen88.credits.Credits;
import me.rampen88.credits.hooks.McmmoHook;
import me.rampen88.credits.storage.IStorage;
import org.bukkit.entity.Player;

public class McmmoWatchedPlayer implements IWatchedPlayer{

	private SkillType type;
	private Player p;

	public McmmoWatchedPlayer(Player p, SkillType type){
		this.type = type;
		this.p = p;
	}

	@Override
	public void doTheThing(int input, Credits plugin) {
		McmmoHook hook = plugin.getMcmmoHook();
		if(hook != null){

			IStorage storage = plugin.getStorage();

			// check that player has enough credits
			int credits = storage.getCredits(p.getUniqueId());
			if(credits < input){
				p.sendMessage(plugin.getMessageUtil().getMessage("MissingCredits"));
				return;
			}

			// Check that levels can be added. The method sends a message to the player if its above the cap, so just return if it fails.
			if(!hook.canAddLevels(p, type, input)) return;

			// take credits, then try to add the levels.
			if(storage.takeCredits(p.getUniqueId(), input)){

				// Add levels and inform player.
				hook.addLevels(p, type, input);
				sendDoneMessage(plugin, Integer.toString(input), type.getName());

			}else{
				p.sendMessage(plugin.getMessageUtil().getMessage("McMMO.Error"));
			}
		}else{
			// Log to console if McMMOHook is null. Should never happen though.
			plugin.getLogger().info("Tried to '" + input + "' McMMO levels to player '" + p.getName() + "' when McMMO hook is null?");
			plugin.getLogger().info("Is McMMO installed and working?");
		}
	}

	@Override
	public void sendInfoMessage(Credits plugin) {
		p.sendMessage(plugin.getMessageUtil().getMessage("McMMO.EnterAmount"));
	}

	private void sendDoneMessage(Credits plugin, String amount, String skill){
		p.sendMessage(plugin.getMessageUtil().getMessage("McMMO.Redeemed").replace("%amount%", amount).replace("%skill%", skill));
	}

}
