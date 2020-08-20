package sunst.anotherprizedraw.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sunst.anotherprizedraw.managers.APDManager;
import sunst.anotherprizedraw.objects.APDGroup;
import sunst.anotherprizedraw.objects.APDInventoryHolder;
import sunst.anotherprizedraw.objects.APDItem;
import sunst.anotherprizedraw.objects.PrizeDrawObject;

import java.util.*;

import static sunst.anotherprizedraw.managers.APDManager.cooToOrder;

public class InventoryListener implements Listener {
	public static Map<UUID, ItemStack> addingRecipePlayer = new HashMap<>();
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory inv = e.getClickedInventory();
		int slot = e.getRawSlot();
		if(slot < 0 || inv == null)
			return;
		Player p = (Player) e.getWhoClicked();
		
		APDInventoryHolder<?> holder = APDInventoryHolder.getHolder(inv);
		if(holder != null){
			Map<String, Object> dataMap = (Map<String, Object>) holder.getData();
			
			if(dataMap.containsKey("APDGroup") && dataMap.containsKey("firstOrder")){
				e.setCancelled(true);

				APDGroup apdGroup = (APDGroup) dataMap.get("APDGroup");
				int firstOrder = (int) dataMap.get("firstOrder");
				int nextOrder = firstOrder + 27;
				int lastOrder = firstOrder - 27;

				List<ItemStack> rewardItems = apdGroup.getRewardItems();
				
				if(slot >= 9 && slot <= 35){
					p.getInventory().addItem(inv.getItem(slot));
					rewardItems.remove(firstOrder + slot - 9);
					p.sendMessage("§a奖励物品移除成功！");
					p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1.2f);
					p.closeInventory();
					return;
				}
				
				if(nextOrder < rewardItems.size()){
					if(slot == cooToOrder(7, 5)){
						dataMap.put("firstOrder", nextOrder);
						inv.clear();
						APDManager.createEdge(inv, new ItemStack(Material.STAINED_GLASS_PANE));

						for(int i=nextOrder; i<rewardItems.size(); i++){
							if(i >= nextOrder+27){
								inv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
										"§a下一页 §e\u21E8"));
								break;
							}
							inv.setItem(9+i-nextOrder, rewardItems.get(i));
						}
						
						if(nextOrder >= 27){
							inv.setItem(cooToOrder(3, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"§e\u21E6 §a上一页"));
						}

						p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
						return;
					}
				}

				if(firstOrder >= 27){
					if(slot == cooToOrder(3, 5)){
						dataMap.put("firstOrder", lastOrder);
						inv.clear();
						APDManager.createEdge(inv, new ItemStack(Material.STAINED_GLASS_PANE));

						for(int i=lastOrder; i<rewardItems.size(); i++){
							if(i >= lastOrder+27){
								inv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
										"§a下一页 §e\u21E8"));
								break;
							}
							inv.setItem(9+i-lastOrder, rewardItems.get(i));
						}

						if(lastOrder >= 27){
							inv.setItem(cooToOrder(3, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"§e\u21E6 §a上一页"));
						}

						p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
					}
				}
			}
			
			else if(dataMap.containsKey("BomPDO") && dataMap.containsKey("firstOrder")){
				e.setCancelled(true);

				PrizeDrawObject bomPDO = (PrizeDrawObject) dataMap.get("BomPDO");
				int firstOrder = (int) dataMap.get("firstOrder");
				int nextOrder = firstOrder + 27;
				int lastOrder = firstOrder - 27;

				List<ItemStack> bottomItems = bomPDO.getBottomRewards();

				if(slot >= 9 && slot <= 35){
					p.getInventory().addItem(inv.getItem(slot));
					bottomItems.remove(firstOrder + slot - 9);
					p.sendMessage("§a保底物品移除成功！");
					p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1.2f);
					p.closeInventory();
					return;
				}

				if(nextOrder < bottomItems.size()){
					if(slot == cooToOrder(7, 5)){
						dataMap.put("firstOrder", nextOrder);
						inv.clear();
						APDManager.createEdge(inv, new ItemStack(Material.STAINED_GLASS_PANE));

						for(int i=nextOrder; i<bottomItems.size(); i++){
							if(i >= nextOrder+27){
								inv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
										"§a下一页 §e\u21E8"));
								break;
							}
							inv.setItem(9+i-nextOrder, bottomItems.get(i));
						}

						if(nextOrder >= 27){
							inv.setItem(cooToOrder(3, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"§e\u21E6 §a上一页"));
						}

						p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
						return;
					}
				}

				if(firstOrder >= 27){
					if(slot == cooToOrder(3, 5)){
						dataMap.put("firstOrder", lastOrder);
						inv.clear();
						APDManager.createEdge(inv, new ItemStack(Material.STAINED_GLASS_PANE));

						for(int i=lastOrder; i<bottomItems.size(); i++){
							if(i >= lastOrder+27){
								inv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
										"§a下一页 §e\u21E8"));
								break;
							}
							inv.setItem(9+i-lastOrder, bottomItems.get(i));
						}

						if(lastOrder >= 27){
							inv.setItem(cooToOrder(3, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"§e\u21E6 §a上一页"));
						}

						p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
					}
				}

			}

			else if(dataMap.containsKey("TakenPDO") && dataMap.containsKey("firstOrder")){
				e.setCancelled(true);

				PrizeDrawObject takenPDO = (PrizeDrawObject) dataMap.get("TakenPDO");
				int firstOrder = (int) dataMap.get("firstOrder");
				int nextOrder = firstOrder + 27;
				int lastOrder = firstOrder - 27;

				List<ItemStack> takenItems = takenPDO.getOtherTakenItems();

				if(slot >= 9 && slot <= 35){
					p.getInventory().addItem(inv.getItem(slot));
					takenItems.remove(firstOrder + slot - 9);
					p.sendMessage("§a消耗物品移除成功！");
					p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1.2f);
					p.closeInventory();
					return;
				}

				if(nextOrder < takenItems.size()){
					if(slot == cooToOrder(7, 5)){
						dataMap.put("firstOrder", nextOrder);
						inv.clear();
						APDManager.createEdge(inv, new ItemStack(Material.STAINED_GLASS_PANE));

						for(int i=nextOrder; i<takenItems.size(); i++){
							if(i >= nextOrder+27){
								inv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
										"§a下一页 §e\u21E8"));
								break;
							}
							inv.setItem(9+i-nextOrder, takenItems.get(i));
						}

						if(nextOrder >= 27){
							inv.setItem(cooToOrder(3, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"§e\u21E6 §a上一页"));
						}

						p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
						return;
					}
				}

				if(firstOrder >= 27){
					if(slot == cooToOrder(3, 5)){
						dataMap.put("firstOrder", lastOrder);
						inv.clear();
						APDManager.createEdge(inv, new ItemStack(Material.STAINED_GLASS_PANE));

						for(int i=lastOrder; i<takenItems.size(); i++){
							if(i >= lastOrder+27){
								inv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
										"§a下一页 §e\u21E8"));
								break;
							}
							inv.setItem(9+i-lastOrder, takenItems.get(i));
						}

						if(lastOrder >= 27){
							inv.setItem(cooToOrder(3, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"§e\u21E6 §a上一页"));
						}

						p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
					}
				}

			}
			
		}
		
//		if(topInv instanceof CraftingInventory){
//			CraftingInventory craftInv = (CraftingInventory) topInv;
//
//			if(addingRecipePlayer.containsKey(p.getUniqueId())){
//				craftInv.setResult(addingRecipePlayer.get(p.getUniqueId()));
//				return;
//			}
//			
//			ItemStack[] matrix = craftInv.getMatrix();
//			if(APDRecipe.allAPDRecipe.containsKey(new APDRecipe(matrix))){
//				craftInv.setResult(APDRecipe.allAPDRecipe.get(new APDRecipe(matrix)));
//			}
//		}
	}
	
//	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Inventory topInv = e.getView().getTopInventory();
		Player p = (Player) e.getPlayer();

		
		
//		if(topInv instanceof CraftingInventory){
//			CraftingInventory craftInv = (CraftingInventory) topInv;
//
//			if(addingRecipePlayer.containsKey(p.getUniqueId())){
//				ItemStack[] matrix = craftInv.getMatrix();
//				int cnt = 0;
//				for(ItemStack itemMatrix : matrix)
//					if(itemMatrix==null)
//						cnt++;
//				if(cnt >= 9){
//					addingRecipePlayer.remove(p.getUniqueId());
//					p.sendMessage("§c合成配方不能为空！");
//					return;
//				}
//
//				ItemStack result = addingRecipePlayer.get(p.getUniqueId());
//				APDRecipe.allAPDRecipe.put(new APDRecipe(matrix, result), result);
//				addingRecipePlayer.remove(p.getUniqueId());
//				p.sendMessage("§a合成配方添加成功！");
//			}
//		}
	}
}
