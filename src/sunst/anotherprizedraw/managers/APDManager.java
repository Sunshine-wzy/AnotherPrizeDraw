package sunst.anotherprizedraw.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sunst.anotherprizedraw.objects.APDItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APDManager {
	// 判断两物品是否相同
	public static boolean isItemSimilar(ItemStack item, ItemStack theItem, boolean lore) {
		if (item == null)
			return theItem == null;
		if (theItem == null) {
			return false;
		}
		if ((item.getType() == theItem.getType()) && (item.getAmount() >= theItem.getAmount())) {
			if ((item.hasItemMeta()) && (theItem.hasItemMeta())) {
				if ((item.getItemMeta().hasDisplayName()) && (theItem.getItemMeta().hasDisplayName())) {
					if (item.getItemMeta().getDisplayName().equals(theItem.getItemMeta().getDisplayName())) {
						if (lore) {
							if ((item.getItemMeta().hasLore()) && (theItem.getItemMeta().hasLore())) {
								return item.getItemMeta().getLore().toString()
										.equals(theItem.getItemMeta().getLore().toString());
							}
							return (!item.getItemMeta().hasLore()) && (!theItem.getItemMeta().hasLore());
						}
						return true;
					}
					return false;
				}
				if ((!item.getItemMeta().hasDisplayName()) && (!theItem.getItemMeta().hasDisplayName())) {
					if (lore) {
						if ((item.getItemMeta().hasLore()) && (theItem.getItemMeta().hasLore())) {
							return item.getItemMeta().getLore().toString()
									.equals(theItem.getItemMeta().getLore().toString());
						}
						return (!item.getItemMeta().hasLore()) && (!theItem.getItemMeta().hasLore());
					}
					return true;
				}
				return false;
			}
			return (!item.hasItemMeta()) && (!theItem.hasItemMeta());
		}
		return false;
	}

	public static boolean isItemSimilar(ItemStack item, ItemStack theItem) {
		return isItemSimilar(item, theItem, true);
	}
	
	
	// 判断背包中是否含有一定数量的物品ItemStack
	public static boolean containsItem(Inventory inv, ItemStack item, int amount) {
		if (item == null) {
			return false;
		}
		if (amount <= 0)
			return true;

		ItemStack theItem = item.clone();
		theItem.setAmount(1);
		ItemStack[] arrayOfItemStack = inv.getStorageContents();
		for(ItemStack itemStack : arrayOfItemStack){
			if(isItemSimilar(itemStack, theItem, true)){
				amount -= itemStack.getAmount();
				if(amount <= 0)
					return true;
			}
		}
		return false;
	}
	
	public static boolean containsItem(Inventory inv, ItemStack item) {
		return containsItem(inv, item, item.getAmount());
	}
	
	public static boolean containsItem(Inventory inv, List<ItemStack> items, int amount) {
		if(items==null || items.isEmpty())
			return true;
		
		for(ItemStack item : items){
			if(!containsItem(inv, item, item.getAmount()*amount))
				return false;
		}
		
		return true;
	}
	
	public static void removeItem(Inventory inv, ItemStack item, int amount) {
		if(inv==null || item==null)
			return;

		ItemStack theItem = item.clone();
		theItem.setAmount(1);
		ItemStack[] arrayOfItemStack = inv.getStorageContents();
		for(ItemStack itemStack : arrayOfItemStack){
			if(isItemSimilar(itemStack, theItem, true)){
				int oldAmount = itemStack.getAmount();
				itemStack.setAmount(oldAmount - amount);
				
				amount -= oldAmount;
				if(amount <= 0)
					return;
			}
		}
	}
	
	public static void removeItem(Inventory inv, ItemStack item) {
		removeItem(inv, item, item.getAmount());
	}
	
	public static void removeItem(Inventory inv, List<ItemStack> items) {
		for(ItemStack item : items){
			removeItem(inv, item);
		}
	}
	
	// Object转List
	public static <T> List<T> castList(Object obj, Class<T> clazz) {
		List<T> result = new ArrayList<>();
		if (obj instanceof List<?>) {
			for (Object o : (List<?>) obj) {
				result.add(clazz.cast(o));
			}
		}
		return result;
	}

	// Object转Map
	public static <K, V> Map<K, V> castMap(Object obj, Class<K> kClazz, Class<V> vClazz) {
		Map<K, V> result = new HashMap<>();
		if(obj instanceof Map<?, ?>){
			for(Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()){
				result.put(kClazz.cast(entry.getKey()), vClazz.cast(entry.getValue()));
			}
		}
		return result;
	}
	
	public static void sendRepeatMsg(CommandSender sender, String str) {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<40; i++)
			builder.append(str);
		sender.sendMessage(builder.toString());
	}
	
	public static void sendSeparator(CommandSender sender) {
		sendRepeatMsg(sender, "=");
	}
	
	public static boolean isItemArrayEquals(ItemStack[] items1, ItemStack[] items2) {
		if(items1.length != items2.length)
			return false;
		
		for(int i=0; i<items1.length; i++){
			if(items1[i]==null && items2[i]!=null)
				return false;
			if(items1[i]!=null && items2[i]==null)
				return false;
			if(items1[i]==null && items2[i]==null)
				continue;
			
			if(!items1[i].equals(items2[i])){
				return false;
			}
		}
		
		return true;
	}
	
	public static int cooToOrder(int x, int y) {
		int ans;
		ans = (y-1) * 9;
		ans += x-1;

		return ans;
	}

	//快速创建边框
	public static void createEdge(Inventory inv, ItemStack item_bk) {
		APDItem item = new APDItem(item_bk, "§f边框");

		for(int i = 0; i < 9; i++) {
			inv.setItem(i, item);
			inv.setItem(i+36, item);
		}
	}
}
