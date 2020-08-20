package sunst.anotherprizedraw.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import sunst.anotherprizedraw.AnotherPrizeDraw;
import sunst.anotherprizedraw.managers.APDManager;
import sunst.anotherprizedraw.maps.PDOMap;
import sunst.anotherprizedraw.objects.APDGroup;
import sunst.anotherprizedraw.objects.PrizeDrawObject;

import java.util.*;

public class PlayerListener implements Listener {

	private Map<UUID, Long> cooldownMap = new HashMap<>();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK){
			Player p = e.getPlayer();
			PlayerInventory inv = p.getInventory();
			ItemStack handItem = e.getItem();
			
			for(PrizeDrawObject objectPD : PDOMap.allPrizeDrawObjects.values()){
				ItemStack takenItem = objectPD.getTakenItem().clone();
				List<ItemStack> otherTakenItems = new ArrayList<>();

				for(ItemStack cloneItem : objectPD.getOtherTakenItems()){
					otherTakenItems.add(cloneItem.clone());
				}

				if(APDManager.isItemSimilar(handItem, takenItem)){
					e.setCancelled(true);

					if(APDManager.containsItem(inv, otherTakenItems, 1)){
						long passedTime = System.currentTimeMillis() - cooldownMap.getOrDefault(p.getUniqueId(), 0L);
						if (passedTime < objectPD.getCooldown() * 1000L) {
							p.sendMessage("§a抽奖还在冷却中，请稍后重试！ §7(" + (objectPD.getCooldown() - (passedTime / 1000)) + "秒)");
							return;
						}
						cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());

						boolean isContinuous = false;
						boolean isSilence = objectPD.isSilence();
						int conQuantity = objectPD.getContinuousQuantity();
						if(conQuantity != 0 && takenItem.getAmount() != 0
								&& handItem.getAmount() >= takenItem.getAmount() * conQuantity
								&& APDManager.containsItem(inv, otherTakenItems, conQuantity)
						){
							isContinuous = true;
							takenItem.setAmount(takenItem.getAmount() * conQuantity);

							for(ItemStack theOtherItem : otherTakenItems){
								theOtherItem.setAmount(theOtherItem.getAmount() * conQuantity);
							}

							p.sendMessage("§a您触发了§e" + conQuantity + "§a连抽！");
						}

						if(!isSilence)
							p.sendTitle("§a抽奖开始！", "§e祝你好运~", 10, 70, 20);
						
						handItem.setAmount(handItem.getAmount() - takenItem.getAmount());
						APDManager.removeItem(inv, otherTakenItems);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);

						boolean finalIsContinuous = isContinuous;
						Bukkit.getScheduler().runTask(AnotherPrizeDraw.pluginAPD, () -> {
							if(!isSilence){
								try {
									for(int i = 1; i <= 30; i++){
										long sleepTime = 100;

										if(i < 15){
											p.getWorld().playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
										} else if(i < 25){
											sleepTime = 500;
											p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
										} else {
											sleepTime = 800;
											p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1.2f);
										}
										System.out.println("sleep");
										Thread.sleep(sleepTime);
										System.out.println("sleeped");
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}

							class DoPrizeDraw extends BukkitRunnable {
								final Random rand = new Random();
								
								@Override
								public void run() {
									double num = rand.nextDouble() * 100;
									double cnt = 0;

									for(APDGroup apdGroup : objectPD.getGroups().values()){
										boolean isGet = false;
										
										if(objectPD.getMode() == PrizeDrawObject.PDOMode.SHARED){
											if(num >= cnt && num <= cnt+apdGroup.getProbability()){
												for(ItemStack reward : apdGroup.getRewardItems())
													Bukkit.getScheduler().runTask(AnotherPrizeDraw.pluginAPD, () -> {
														if (p.getInventory().firstEmpty() == -1) {
															p.getWorld().dropItem(p.getLocation(), reward);
														} else {
															p.getInventory().addItem(reward);
														}
													});

												isGet = true;
											}
											cnt += apdGroup.getProbability();
										}
										
										else if(objectPD.getMode() == PrizeDrawObject.PDOMode.INDEPENDENT){
											double[] range = getIndependentPro(apdGroup.getProbability());

											if(num >= range[0] && num <= range[1]){
												for(ItemStack reward : apdGroup.getRewardItems()) {
													Bukkit.getScheduler().runTask(AnotherPrizeDraw.pluginAPD, () -> {
														if (p.getInventory().firstEmpty() == -1) {
															p.getWorld().dropItem(p.getLocation(), reward);
														} else {
															p.getInventory().addItem(reward);
														}
													});
												}

												isGet = true;
											}
										}
										
										if(isGet){
											if(!isSilence)
												p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.5f);
											if(apdGroup.hasMessage())
												Bukkit.broadcastMessage(apdGroup.getMessage()
														.replace("{player}", p.getName()));
											if(apdGroup.hasCommands()){
												for(String cmd : apdGroup.getCommands()){
													Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
															cmd.replace("{player}", p.getName()));
												}
											}
											
											if(objectPD.getMode() == PrizeDrawObject.PDOMode.SHARED)
												break;
										}
									}
									
									
								}
							}

							if(finalIsContinuous){
								for(int i=0; i<conQuantity; i++) {
									new DoPrizeDraw().runTask(AnotherPrizeDraw.pluginAPD);
								}
								for (ItemStack bottomItem : objectPD.getBottomRewards()) {
									Bukkit.getScheduler().runTask(AnotherPrizeDraw.pluginAPD, () -> {
										if (p.getInventory().firstEmpty() == -1) {
											p.getWorld().dropItem(p.getLocation(), bottomItem);
										} else {
											p.getInventory().addItem(bottomItem);
										}
									});
								}
							} else {
								new DoPrizeDraw().runTask(AnotherPrizeDraw.pluginAPD);
							}
							
							if(!isSilence)
								p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1.2f);
						});
					}
				}
			}
		}
	}
	
	
	public static double[] getIndependentPro(double probability) {
		double[] result = new double[2];
		
		if(probability >= 100){
			result[0] = 0;
			result[1] = 100;
			return result;
		}
		
		result[0] = new Random().nextDouble() * 100;
		result[1] = result[0] + probability;
		
		if(result[1] > 100)
			return getIndependentPro(probability);
		
		return result;
	}
}
