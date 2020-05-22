package sunst.anotherprizedraw.objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import sunst.anotherprizedraw.managers.APDManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrizeDrawObject implements ConfigurationSerializable {
	private ItemStack takenItem;
	private List<ItemStack> otherTakenItems = new ArrayList<>();
	private PDOMode mode;
	private int continuousQuantity;
	private boolean silence;

	private Map<String, APDGroup> groups = new HashMap<>();
	private List<ItemStack> bottomRewards = new ArrayList<>();
	
	
	public PrizeDrawObject(ItemStack takenItem, PDOMode mode, int continuousQuantity, boolean silence) {
		this.takenItem = takenItem.clone();
		this.mode = mode;
		this.continuousQuantity = continuousQuantity;
		this.silence = silence;
	}
	
	public PrizeDrawObject(Map<String, Object> map) {
		this((ItemStack) map.get("takenItem"),
				PDOMode.getByName((String) map.get("mode")),
				(int) map.get("continuousQuantity"),
				map.containsKey("silence") && (boolean) map.get("silence"));
		
		this.otherTakenItems = APDManager.castList(map.get("otherTakenItems"), ItemStack.class);
		this.groups = APDManager.castMap(map.get("groups"), String.class, APDGroup.class);
		this.bottomRewards = APDManager.castList(map.get("bottomRewards"), ItemStack.class);
	}


	public void setTakenItem(ItemStack takenItem) {
		this.takenItem = takenItem.clone();
	}
	
	public void addOtherTakenItem(ItemStack otherTakenItem) {
		this.otherTakenItems.add(otherTakenItem.clone());
	}

	public void setMode(PDOMode mode) {
		this.mode = mode;
	}
	
	public void setMode(String mode) {
		PDOMode pdoMode = PDOMode.getByName(mode);
		if(pdoMode!=null)
			setMode(pdoMode);
	}

	public void setContinuousQuantity(int continuousQuantity) {
		this.continuousQuantity = continuousQuantity;
	}

	public void setSilence(boolean silence) {
		this.silence = silence;
	}

	public void addGroup(String name, double probability) {
		this.groups.put(name, new APDGroup(probability));
	}
	
	public void addBottomReward(ItemStack bottomReward) {
		this.bottomRewards.add(bottomReward.clone());
	}
	
	
	public ItemStack getTakenItem() {
		return takenItem.clone();
	}

	public List<ItemStack> getOtherTakenItems() {
		return otherTakenItems;
	}

	public PDOMode getMode() {
		return mode;
	}

	public int getContinuousQuantity() {
		return continuousQuantity;
	}

	public boolean isSilence() {
		return silence;
	}

	public Map<String, APDGroup> getGroups() {
		return groups;
	}

	public List<ItemStack> getBottomRewards() {
		return bottomRewards;
	}
	
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();

		map.put("takenItem", takenItem);
		map.put("otherTakenItems", otherTakenItems);
		map.put("mode", mode.getName());
		map.put("continuousQuantity", continuousQuantity);
		map.put("groups", groups);
		map.put("bottomRewards", bottomRewards);
		map.put("silence", silence);

		return map;
	}


	public enum PDOMode {
		SHARED("shared"),
		INDEPENDENT("independent");
		
		private final String name;
		private static final Map<String, PDOMode> BY_NAME = new HashMap<>();
		
		static {
			for(PDOMode pdoMode : PDOMode.values()){
				BY_NAME.put(pdoMode.getName(), pdoMode);
			}
		}
		
		PDOMode(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
		public static PDOMode getByName(String name) {
			if(BY_NAME.containsKey(name))
				return BY_NAME.get(name);
			return null;
		}
	}
	
}
