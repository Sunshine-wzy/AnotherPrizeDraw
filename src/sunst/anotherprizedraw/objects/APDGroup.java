package sunst.anotherprizedraw.objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import sunst.anotherprizedraw.managers.APDManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APDGroup implements ConfigurationSerializable {
	private List<ItemStack> rewardItems = new ArrayList<>();
	private double probability;
	private String message;
	private List<String> commands = new ArrayList<>();
	
	
	public APDGroup(double probability) {
		this.probability = probability;
	}
	
	public APDGroup(Map<String, Object> map) {
		this.probability = (double) map.get("probability");
		this.rewardItems = APDManager.castList(map.get("rewardItems"), ItemStack.class);
		this.message = (String) map.get("message");
		this.commands = APDManager.castList(map.get("commands"), String.class);
	}


	public void addReward(ItemStack item) {
		this.rewardItems.add(item.clone());
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}

	public void setMessage(String message) {
		this.message = message.replace('&', '¡ì');
	}

	public void addCommand(String command) {
		this.commands.add(command.replace('&', '¡ì'));
	}

	
	public List<ItemStack> getRewardItems() {
		return rewardItems;
	}

	public double getProbability() {
		return probability;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getCommands() {
		return commands;
	}
	
	
	public boolean hasMessage() {
		return message!=null;
	}
	
	public boolean hasCommands() {
		return commands!=null && !commands.isEmpty();
	}
	
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		
		map.put("rewardItems", rewardItems);
		map.put("probability", probability);
		map.put("message", message);
		map.put("commands", commands);
		
		return map;
	}
}
