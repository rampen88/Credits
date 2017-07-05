package me.rampen88.credits.hooks;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import me.rampen88.credits.Credits;
import me.rampen88.credits.menu.items.ItemAction;
import me.rampen88.credits.menu.items.RedeemMcmmoAction;
import me.rampen88.credits.storage.IStorage;
import org.bukkit.entity.Player;

public class McmmoHook {

	private Credits plugin;

	public McmmoHook(Credits plugin) {
		this.plugin = plugin;
	}

	public void addLevels(Player p, SkillType type, int levels){
		ExperienceAPI.addLevel(p, type.toString(), levels);
	}

	public boolean canAddLevels(Player p, SkillType type, int levels){
		int currLevel = ExperienceAPI.getLevel(p, type.toString());
		int cap = type.getMaxLevel();

		if(currLevel + levels > cap){
			p.sendMessage(plugin.getMessageUtil().getMessage("McMMO.OverLimit").replace("%target%", Integer.toString(currLevel + levels)).replace("%cap%", Integer.toString(cap)));
			return false;
		}
		return true;
	}

	int getLevelCap(String skillName){
		SkillType type = SkillType.getSkill(skillName.toUpperCase());
		if(type == null){
			plugin.getLogger().info("Tried to get level cap for invalid skill: " + skillName);
			return 0;
		}
		return type.getMaxLevel();
	}

	public ItemAction getMcmmoItemAction(String s){
		SkillType type = SkillType.getSkill(s);
		if(type == null) {
			plugin.getLogger().info("Invalid skill type: " + s);
			return null;
		}
		return new RedeemMcmmoAction(type);
	}

}
