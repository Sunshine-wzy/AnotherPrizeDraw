package sunst.anotherprizedraw.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sunst.anotherprizedraw.AnotherPrizeDraw;
import sunst.anotherprizedraw.managers.APDManager;
import sunst.anotherprizedraw.objects.APDGroup;
import sunst.anotherprizedraw.objects.APDInventoryHolder;
import sunst.anotherprizedraw.objects.APDItem;
import sunst.anotherprizedraw.objects.PrizeDrawObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sunst.anotherprizedraw.managers.APDManager.*;
import static sunst.anotherprizedraw.maps.PDOMap.allPrizeDrawObjects;

public class APDCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.isOp()){
			sender.sendMessage("§c您不是op，不能查看或使用AnotherPrizeDraw的指令！");
			return false;
		}
		
		
		if(args==null || args.length <= 0){
			help(sender, 1);
		}
		
		else {
			int length = args.length;

			if(length == 1){
				if(args[0].matches("\\d+")){
					help(sender, Integer.parseInt(args[0]));
					return true;
				}
				
				if(args[0].equalsIgnoreCase("save")){
					AnotherPrizeDraw.saveResConfig();
					sender.sendMessage("§a配置文件保存成功！");
					return true;
				}

				if(args[0].equalsIgnoreCase("load")){
					AnotherPrizeDraw.loadResConfig();
					sender.sendMessage("§a配置文件加载成功！");
					return true;
				}
			}
			
			if(!(sender instanceof Player)){
				sender.sendMessage("§c这个指令需要玩家执行！");
				return false;
			}
			Player p = (Player) sender;
			ItemStack handItem = p.getInventory().getItemInMainHand();
			
			
			String str1 = args[0].toLowerCase();
			String str2 = null;
			String str3 = null;
			String str4 = null;
			String str5 = null;
			if(length>=2)
				str2 = args[1];
			if(length>=3)
				str3 = args[2];
			if(length>=4)
				str4 = args[3];
			if(length>=5)
				str5 = args[4];
			
			switch (str1) {
				case "create":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在create后输入需要创建的抽奖项目名称");
						break;
					}
					
					if(allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您已创建过名为§e"+str2+"§c的抽奖项目了，请勿重复创建！");
						break;
					}
					
					if(handItem==null || handItem.getType()==Material.AIR){
						p.sendMessage("§c您没有拿着抽奖消耗主物品！");
						break;
					}
					
					if(str3==null || str3.isEmpty()){
						p.sendMessage("§c请在抽奖项目名称后输入概率模式(shared/independent)");
						p.sendMessage("§b[shared为共享概率，independent为独立概率](可使用Tab自动补全)");
						break;
					}
					
					str3 = str3.toLowerCase();
					if(!str3.equalsIgnoreCase("shared") && !str3.equalsIgnoreCase("independent")){
						p.sendMessage("§c请输入shared或independent!");
						break;
					}
					
					if(str4==null || str4.isEmpty()){
						p.sendMessage("§c请在概率模式后输入触发连抽物品数量");
						break;
					}
					
					if(!str4.matches("\\d+") || Integer.parseInt(str4)<0){
						p.sendMessage("§c请输入一个非负整数！");
						break;
					}
					
					boolean isSilence = false;
					if(str5!=null){
						if(str5.equalsIgnoreCase("true"))
							isSilence = true;
						else if(!str5.equalsIgnoreCase("false")){
							p.sendMessage("§c请输入true/false以设置是否开启静默模式(抽奖过程无声音和标题)");
							break;
						}
					}
					
					int conQuantity = Integer.parseInt(str4);
					allPrizeDrawObjects.put(str2, new PrizeDrawObject(handItem, PrizeDrawObject.PDOMode.getByName(str3), conQuantity, isSilence, 0));
					
					p.sendMessage("§a已为您创建名为§e"+str2+"§a的抽奖项目~");
					sendSeparator(p);
					p.sendMessage("§a该项目抽奖消耗主物品为["+getItemName(handItem)+"§a]");
					p.sendMessage("§a该项目概率模式为§d"+str3);
					if(conQuantity==0)
						p.sendMessage("§a该项目不会触发连抽");
					else p.sendMessage("§a该项目触发连抽物品数量为§b"+conQuantity);
					sendSeparator(p);
					
					break;
					
				case "remove":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在remove后输入需要移除的抽奖项目名称");
						break;
					}
					
					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还未创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}
					
					if(str3==null || str3.isEmpty()){
						p.sendMessage("§c请在抽奖项目名称后输入accept以确认删除！");
						break;
					}
					
					str3 = str3.toLowerCase();
					if(!str3.equalsIgnoreCase("accept")){
						p.sendMessage("§c请输入accept以确认删除(请务必谨慎删除)!");
						break;
					}
					
					allPrizeDrawObjects.remove(str2);
					p.sendMessage("§a成功将名为§e"+str2+"§a的抽奖项目移除！");
					break;
					
				case "group":
					if(str2==null || str2.isEmpty()){
						sendSeparator(sender);
						sender.sendMessage("§6§lAnotherPrizeDraw §bGroup奖励物品组操作帮助 §d第1页");
						sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] §a>>查看一个奖励物品组的信息");
						sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] create 抽中该组的概率 §a>>创建一个奖励物品组");
						sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] remove §a>>移除一个奖励物品组");
						sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] add §a>>为组添加一个奖励物品 §c(请手持奖励物品输入)");
						sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] edit §a>>可视化编辑一个组(GUI界面)");
						sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] setmsg 信息内容 §a>>设置抽中该组后向全服发送的信息");
						sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] addcmd 指令内容(无需/) §a>>设置抽中该组后执行的指令");
						sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] delcmd 指令序号 §a>>删除抽中该组后执行某一指令" +
								"§b(指令序号输入/apd group [抽奖项目名称] [组名称] 来查看)");
						sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] setpro 抽中该组的概率 §a>>设置抽中该组的概率(0-100之间的实数)");
						sender.sendMessage("§e/apd g2 §a>>查看Group奖励物品组操作帮助第2页");
						sendSeparator(sender);
						break;
					}
					
					if(str4!=null){
						str4 = str4.toLowerCase();
						
						if(str4.equals("create")){
							if(str3==null || str3.isEmpty()){
								p.sendMessage("§c请在create前输入需要创建的组名称");
								break;
							}

							if(allPrizeDrawObjects.get(str2).getGroups().containsKey(str3)){
								p.sendMessage("§c您已在§e"+str2+"§c中创建过名为§6"+str3+"§c的组了，请勿重复创建！");
								break;
							}

							if(str5==null || str5.isEmpty()){
								p.sendMessage("§c请在create后输入抽中该组的概率(一个0-100之间的实数，为百分比)");
								break;
							}

							double probability;
							try {
								probability = Double.parseDouble(str5);
							} catch (NumberFormatException ex) {
								p.sendMessage("§c您输入的概率不正确！");
								break;
							}
							allPrizeDrawObjects.get(str2).addGroup(str3, probability);

							p.sendMessage("§a成功在§e"+str2+"§a中创建组§6"+str3);
							p.sendMessage("§a抽中该组的概率为§b"+probability+"§d%");
							break;
						}
					}
					
					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目，请用/apd create创建！");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("§c请在抽奖项目名后输入组名");
						p.sendMessage("§b项目§e"+str2+"§b所有组：");
						for(String groupName : allPrizeDrawObjects.get(str2).getGroups().keySet())
							p.sendMessage(groupName);
						break;
					}

					Map<String, APDGroup> groupMap = allPrizeDrawObjects.get(str2).getGroups();
					if(!groupMap.containsKey(str3)){
						p.sendMessage("§c您还没在§e"+str2+"§c中创建过名为§6"+str3+"§c的组，请用§a/apd group [项目名] [组名] create 抽中该组的概率 §c来创建！");
						break;
					}

					APDGroup infoGroup = groupMap.get(str3);
					if(str4==null || str4.isEmpty()){
						sendSeparator(p);
						p.sendMessage("§a项目§e"+str2+"§a中的奖励物品组名称：§6"+str3);
						p.sendMessage("§a抽中该组的概率为§b"+infoGroup.getProbability()+"§d%");
						if(infoGroup.getMessage()!=null){
							p.sendMessage("§a抽中该组后向全服发送的信息为：");
							p.sendMessage(infoGroup.getMessage());
						}
						if(infoGroup.getCommands()!=null && !infoGroup.getCommands().isEmpty()){
							p.sendMessage("§a抽中该组后执行的命令为：");
							for(int i=0; i<infoGroup.getCommands().size(); i++){
								p.sendMessage("§a"+i+": §f/"+infoGroup.getCommands().get(i));
							}
						}
						sendSeparator(p);
						break;
					}
					
					switch (str4) {
						case "add":
							if(handItem==null || handItem.getType()==Material.AIR){
								p.sendMessage("§c您没有拿着奖励物品！");
								break;
							}

							infoGroup.addReward(handItem);
							p.sendMessage("§a成功为§e"+str2+"§a中的组§6"+str3+"§a添加奖励物品["+getItemName(handItem)+"§a]");
							break;
							
						case "remove":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("§c请在组名称后输入accept以确认删除！");
								break;
							}

							str5 = str5.toLowerCase();
							if(!str5.equalsIgnoreCase("accept")){
								p.sendMessage("§c请输入accept以确认删除(请务必谨慎删除)!");
								break;
							}
							
							groupMap.remove(str3);
							p.sendMessage("§a成功将§e"+str2+"§a中的§6"+str3+"§a组移除！");
							break;
							
						case "edit":
							if(infoGroup.getRewardItems()==null || infoGroup.getRewardItems().isEmpty()){
								p.sendMessage("§a项目§e"+str2+"§a中的组§6"+str3+"§a没有任何奖励物品!");
								break;
							}
							
							Map<String, Object> dataMap = new HashMap<>();
							dataMap.put("APDGroup", infoGroup);
							dataMap.put("firstOrder", 0);
							Inventory editInv = new APDInventoryHolder<>(dataMap, 5 * 9, "§a项目§e" + str2 + "§a中的组§6" + str3).getInventory();
							APDManager.createEdge(editInv, new ItemStack(Material.STAINED_GLASS_PANE));
							
							for(int i=0; i<infoGroup.getRewardItems().size(); i++){
								if(i>=27){
									editInv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
											"§a下一页 §e\u21E8"));
									break;
								}
								editInv.setItem(9+i, infoGroup.getRewardItems().get(i));
							}
							
							p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
							p.openInventory(editInv);
							break;
							
						case "setmsg":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("§c请在setmsg后输入抽中该组后向全服发送的信息内容");
								p.sendMessage("§b可使用&作为颜色代码");
								break;
							}

							StringBuilder newMsg = new StringBuilder();
							for(int i=4; i<args.length; i++)
								newMsg.append(args[i]).append(" ");
							
							infoGroup.setMessage(newMsg.toString());
							p.sendMessage("§a成功将§e"+str2+"§a中的组§6"+str3+"§a被抽中后向全服发送的信息设置为：");
							p.sendMessage(infoGroup.getMessage());
							break;
							
						case "addcmd":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("§c请在addcmd后输入抽中该组后执行的指令(无需输入/)");
								p.sendMessage("§b请在指令中用{player}代替玩家，可使用&作为颜色代码");
								break;
							}

							StringBuilder newCommand = new StringBuilder();
							for(int i=4; i<args.length; i++)
								newCommand.append(args[i]).append(" ");

							infoGroup.addCommand(newCommand.toString());
							p.sendMessage("§a成功为§e"+str2+"§a中的组§6"+str3+"§a添加了被抽中后执行的指令：");
							p.sendMessage("/"+newCommand.toString().replace('&', '§'));
							break;
							
						case "delcmd":
							if(str5==null || !str5.matches("\\d+") || Integer.parseInt(str5)<0){
								p.sendMessage("§c请在delcmd后输入一个为非负整数的指令序号§b(指令序号输入/apd group [抽奖项目名称] [组名称] 来查看)");
								break;
							}
							
							int commandOrder = Integer.parseInt(str5);
							if(commandOrder>=infoGroup.getCommands().size()){
								p.sendMessage("§c你输入指令序号不存在！");
								break;
							}
							
							p.sendMessage("§a成功移除§e"+str2+"§a中的组§6"+str3+"§a中的第"+commandOrder+"条指令：");
							p.sendMessage("/"+infoGroup.getCommands().get(commandOrder));
							infoGroup.getCommands().remove(commandOrder);
							break;
							
						case "setpro":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("§c请在setpro后输入抽中该组的概率(一个0-100之间的实数，为百分比)");
								break;
							}

							double probability;
							try {
								probability = Double.parseDouble(str5);
							} catch (NumberFormatException ex) {
								p.sendMessage("§c您输入的概率不正确！");
								break;
							}
							
							infoGroup.setProbability(probability);
							p.sendMessage("§a成功将§e"+str2+"§a中的组§6"+str3+"§a的抽中概率设置为：§b"+probability+"§d%");
							break;
							
						case "rename":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("§c请在rename后输入该组的新名称");
								break;
							}
							
							if(groupMap.containsKey(str5)){
								p.sendMessage("§c该组名称已存在，请换一个！");
								break;
							}
							
							groupMap.put(str5, infoGroup);
							groupMap.remove(str3);

							p.sendMessage("§a已将§d"+str3+"§a重命名为§6"+str5);
							break;
							
						case "get":
							List<ItemStack> groupRewardItems = infoGroup.getRewardItems();
							if(groupRewardItems==null || groupRewardItems.isEmpty()){
								p.sendMessage("§c该组没有奖励物品！");
								break;
							}
							
							for(ItemStack groupItem : groupRewardItems)
								p.getWorld().dropItem(p.getLocation(), groupItem);
							break;
					}
					
					break;
					
				case "g2":
					sendSeparator(sender);
					sender.sendMessage("§6§lAnotherPrizeDraw §bGroup奖励物品组操作帮助 §d第2页");
					sender.sendMessage("§e/apd group [抽奖项目名称] [旧的组名称] rename [新的组名称] §a>>重命名一个奖励物品组");
					sender.sendMessage("§e/apd group [抽奖项目名称] [组名称] get §a>>获得该奖励物品组的所有奖励物品");
					sendSeparator(sender);
					break;
					
					
				case "addbottom":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在addbottom后输入已有的抽奖项目名称（若没有，请用/apd create创建）");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目，请用/apd create创建！");
						break;
					}

					if(handItem==null || handItem.getType()==Material.AIR){
						p.sendMessage("§c您没有拿着保底奖励物品！");
						break;
					}
					
					allPrizeDrawObjects.get(str2).addBottomReward(handItem);

					
					p.sendMessage("§a成功为§e"+str2+"§a添加保底奖励物品["+getItemName(handItem)+"§a]");
					
					break;
					
				case "addtaken":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在addtaken后输入已有的抽奖项目名称（若没有，请用/apd create创建）");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目，请用/apd create创建！");
						break;
					}

					if(handItem==null || handItem.getType()==Material.AIR){
						p.sendMessage("§c您没有拿着抽奖消耗物品！");
						break;
					}
					
					allPrizeDrawObjects.get(str2).addOtherTakenItem(handItem);
					
					p.sendMessage("§a成功为§e"+str2+"§a添加消耗物品["+getItemName(handItem)+"§a]");
					
					break;
					
				case "list":
					if(allPrizeDrawObjects.isEmpty()){
						p.sendMessage("§c当前还没有任何抽奖项目！快输入/apd create创建一个吧~");
						break;
					}
					
					p.sendMessage("§a当前所有抽奖项目名称：");
					for(Map.Entry<String, PrizeDrawObject> entry : allPrizeDrawObjects.entrySet()){
						p.sendMessage(entry.getKey());
					}
					
					break;
					
				case "info":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在info后输入已有的抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}

					PrizeDrawObject infoPDO = allPrizeDrawObjects.get(str2);
					sendSeparator(p);
					p.sendMessage("§a抽奖项目名称：§e"+str2);
					p.sendMessage("§a该项目抽奖消耗主物品为["+getItemName(infoPDO.getTakenItem())+"§a]");
					p.sendMessage("§a该项目概率模式为§d"+infoPDO.getMode().getName());
					if(infoPDO.getContinuousQuantity()==0)
						p.sendMessage("§a该项目不会触发连抽");
					else p.sendMessage("§a该项目触发连抽物品数量为§b"+infoPDO.getContinuousQuantity());
					sendSeparator(p);


					break;

				case "editbom":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在editbom后输入已有的抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}
					
					PrizeDrawObject bomPDO = allPrizeDrawObjects.get(str2);

					if(bomPDO.getBottomRewards()==null || bomPDO.getBottomRewards().isEmpty()){
						p.sendMessage("§a项目§e"+str2+"§a没有任何保底物品!");
						break;
					}
					
					Map<String, Object> dataMapBom = new HashMap<>();
					dataMapBom.put("BomPDO", bomPDO);
					dataMapBom.put("firstOrder", 0);
					Inventory bomEditInv = new APDInventoryHolder<>(dataMapBom, 5 * 9, "§a项目§e" + str2 + "§a的保底物品").getInventory();
					APDManager.createEdge(bomEditInv, new ItemStack(Material.STAINED_GLASS_PANE));

					for(int i=0; i<bomPDO.getBottomRewards().size(); i++){
						if(i>=27){
							bomEditInv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"§a下一页 §e\u21E8"));
							break;
						}
						bomEditInv.setItem(9+i, bomPDO.getBottomRewards().get(i));
					}

					p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
					p.openInventory(bomEditInv);
					break;

				case "edittaken":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在edittaken后输入已有的抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}
					
					PrizeDrawObject takenPDO = allPrizeDrawObjects.get(str2);

					if(takenPDO.getOtherTakenItems()==null || takenPDO.getOtherTakenItems().isEmpty()){
						p.sendMessage("§a项目§e"+str2+"§a没有任何消耗物品!");
						break;
					}
					
					Map<String, Object> dataMapTaken = new HashMap<>();
					dataMapTaken.put("TakenPDO", takenPDO);
					dataMapTaken.put("firstOrder", 0);
					Inventory takenEditInv = new APDInventoryHolder<>(dataMapTaken, 5 * 9, "§a项目§e" + str2 + "§a的消耗物品").getInventory();
					APDManager.createEdge(takenEditInv, new ItemStack(Material.STAINED_GLASS_PANE));

					for(int i=0; i<takenPDO.getOtherTakenItems().size(); i++){
						if(i>=27){
							takenEditInv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"§a下一页 §e\u21E8"));
							break;
						}
						takenEditInv.setItem(9+i, takenPDO.getOtherTakenItems().get(i));
					}

					p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
					p.openInventory(takenEditInv);
					break;
					
				case "rename":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在rename后输入已有的抽奖项目名称以及新的抽奖项目名称");
						break;
					}
					
					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目，不能重命名！");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("§c请在旧项目名后输入新项目名");
						break;
					}
					
					allPrizeDrawObjects.put(str3, allPrizeDrawObjects.get(str2));
					allPrizeDrawObjects.remove(str2);
					p.sendMessage("§a已将§d"+str2+"§a重命名为§e"+str3);
					
					break;
					
				case "set":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在set后输入抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}

					if(handItem==null || handItem.getType()==Material.AIR){
						p.sendMessage("§c您没有拿着抽奖消耗主物品！");
						break;
					}
					
					allPrizeDrawObjects.get(str2).setTakenItem(handItem);
					p.sendMessage("§a已将§e"+str2+"§a的消耗主物品设置为["+getItemName(handItem)+"§a]");
					
					break;
					
				case "setmode":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在setmode后输入抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("§c请在抽奖项目名称后输入概率模式(shared/independent)");
						p.sendMessage("§b[shared为共享概率，independent为独立概率](可使用Tab自动补全)");
						break;
					}
					
					str3 = str3.toLowerCase();
					if(!str3.equalsIgnoreCase("shared") && !str3.equalsIgnoreCase("independent")){
						p.sendMessage("§c请输入shared或independent!");
						break;
					}
					
					allPrizeDrawObjects.get(str2).setMode(str3);
					p.sendMessage("§a已将§e"+str2+"§a的概率模式设置为§d"+str3);
					
					break;
					
				case "setcon":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在setcon后输入抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("§c请输入触发连抽物品数量");
						break;
					}

					if(!str3.matches("\\d+") || Integer.parseInt(str3)<0){
						p.sendMessage("§c请输入一个非负整数！");
						break;
					}

					int newConQuantity = Integer.parseInt(str3);
					allPrizeDrawObjects.get(str2).setContinuousQuantity(newConQuantity);
					p.sendMessage("§a已将§e"+str2+"§a的触发连抽物品数量设置为§b"+newConQuantity);
					
					break;
					
//				case "recadd":
//					if(handItem==null || handItem.getType()==Material.AIR){
//						p.sendMessage("§c请将需要自定义合成的物品拿在手中！(包括数量)");
//						break;
//					}
//
//					InventoryListener.addingRecipePlayer.put(p.getUniqueId(), handItem.clone());
//					p.openWorkbench(p.getLocation(), true);
//					
//					break;
//					
//				case "recdel":
//					if(handItem==null || handItem.getType()==Material.AIR){
//						p.sendMessage("§c请将需要移除自定义合成的物品拿在手中！(包括数量)");
//						break;
//					}
//					
//					if(!APDRecipe.allAPDRecipe.containsValue(handItem)){
//						p.sendMessage("§c这个物品没有自定义合成！(包括数量)");
//						break;
//					}
//					
//					break;
				
				case "silence":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在silence后输入抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("§c请输入true或false以开启或关闭静默模式(抽奖过程没有声音和标题)");
						break;
					}

					boolean setSilence = false;
					if(str3.equalsIgnoreCase("true"))
						setSilence = true;
					else if(!str3.equalsIgnoreCase("false")){
						p.sendMessage("§c请输入true或false以开启或关闭静默模式(抽奖过程没有声音和标题)");
						break;
					}
					
					allPrizeDrawObjects.get(str2).setSilence(setSilence);
					p.sendMessage("§a成功将项目§e"+str2+"§a的静默模式设置为：§f"+setSilence);
					break;
					
				case "get":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在get后输入抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}

					if (p.getInventory().firstEmpty() == -1) {
						p.getWorld().dropItem(p.getLocation(), allPrizeDrawObjects.get(str2).getTakenItem());
					} else {
						p.getInventory().addItem(allPrizeDrawObjects.get(str2).getTakenItem());
					}
					break;
					
				case "gettaken":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在gettaken后输入抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}

					List<ItemStack> otherTakenItemsGet = allPrizeDrawObjects.get(str2).getOtherTakenItems();
					if(otherTakenItemsGet==null || otherTakenItemsGet.isEmpty()){
						p.sendMessage("§c项目§e"+str2+"§c没有其他消耗物品！");
						break;
					}
					
					for(ItemStack otherItem : otherTakenItemsGet) {
						if (p.getInventory().firstEmpty() == -1) {
							p.getWorld().dropItem(p.getLocation(), otherItem);
						} else {
							p.getInventory().addItem(otherItem);
						}
					}
					break;
					
				case "getall":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在getall后输入抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}

					if (p.getInventory().firstEmpty() == -1) {
						p.getWorld().dropItem(p.getLocation(), allPrizeDrawObjects.get(str2).getTakenItem());
					} else {
						p.getInventory().addItem(allPrizeDrawObjects.get(str2).getTakenItem());
					}

					List<ItemStack> otherTakenItemsGetAll = allPrizeDrawObjects.get(str2).getOtherTakenItems();
					if(otherTakenItemsGetAll!=null && !otherTakenItemsGetAll.isEmpty()){
						for(ItemStack otherItem : otherTakenItemsGetAll) {
							if (p.getInventory().firstEmpty() == -1) {
								p.getWorld().dropItem(p.getLocation(), otherItem);
							} else {
								p.getInventory().addItem(otherItem);
							}
						}
					}
					break;

				case "setcooldown":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("§c请在setcooldown后输入抽奖项目名称");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("§c您还没创建过名为§e"+str2+"§c的抽奖项目！");
						break;
					}

					if(!str3.matches("\\d+") || Integer.parseInt(str3) < 0){
						p.sendMessage("§c请输入一个非负整数！");
						break;
					}

					int cooldown = Integer.parseInt(str3);

					allPrizeDrawObjects.get(str2).setCooldown(cooldown);
					p.sendMessage("§a成功将项目§e"+str2+"§a的冷却时间设置为：§f"+cooldown+"秒");
					break;

				default:
					break;
			}
		}
		
		
		return true;
	}
	
	
	public static String getItemName(ItemStack item) {
		String itemName;
		if(item.hasItemMeta()){
			ItemMeta meta = item.getItemMeta();
			if(meta.hasDisplayName())
				itemName = meta.getDisplayName();
			else if(meta.hasLocalizedName())
				itemName = meta.getLocalizedName();
			else itemName = item.getType().toString();
		} else itemName = item.getType().toString();
		
		return itemName;
	}
	
	public static void help(CommandSender sender, int page) {
		sendSeparator(sender);
		switch (page) {
			case 1:
				sender.sendMessage("§6§lAnotherPrizeDraw §b命令指南 §d第1页");
				sender.sendMessage("§e/apd create [抽奖项目名称] 概率模式(shared/independent) 触发连抽物品数量(0则不触发连抽) " +
						"是否开启静默模式(true/false,若省略则为false) §a>>创建一个抽奖项目 §c(请手持抽奖消耗的主物品输入)");
				sender.sendMessage("§e/apd remove [抽奖项目名称] §a>>移除一个抽奖项目 §c(请谨慎使用)");
				sender.sendMessage("§e/apd group §a>>查看奖励物品组操作帮助");
				sender.sendMessage("§e/apd addbottom [抽奖项目名称] §a>>为抽奖项目添加一个保底奖励物品 §c(请手持保底奖励物品输入)");
				sender.sendMessage("§e/apd addtaken [抽奖项目名称] §a>>为抽奖项目添加一个消耗物品 §c(请手持抽奖消耗物品输入)");
				sender.sendMessage("§e/apd list §a>>查看当前所有抽奖项目名称");
				sender.sendMessage("§e/apd info [抽奖项目名称] §a>>查看指定抽奖项目的信息");
				sender.sendMessage("§e/apd 2 §a>>查看APD命令指南第2页");
				
				break;
			
			case 2:
				sender.sendMessage("§6§lAnotherPrizeDraw §b命令指南 §d第2页");
				sender.sendMessage("§e/apd editbom [抽奖项目名称] §a>>GUI编辑一个抽奖项目的保底物品(查看和移除)");
				sender.sendMessage("§e/apd edittaken [抽奖项目名称] §a>>GUI编辑一个抽奖项目的消耗物品(查看和移除)");
				sender.sendMessage("§e/apd rename [旧的抽奖项目名称] [新的抽奖项目名称] §a>>更改抽奖项目名称");
				sender.sendMessage("§e/apd set [抽奖项目名称] §a>>设置抽奖项目主消耗物品 §c(请手持抽奖消耗的主物品输入)");
				sender.sendMessage("§e/apd setmode [抽奖项目名称] 概率模式(shared/independent) §a>>设置抽奖项目概率模式");
				sender.sendMessage("§e/apd setcon [抽奖项目名称] 触发连抽物品数量 §a>>设置抽奖项目触发连抽的物品数量");
//				sender.sendMessage("§e/apd recadd §a>>添加手持物品的合成(包括数量)");
//				sender.sendMessage("§e/apd recdel §a>>移除手持物品的合成(包括数量)");
				sender.sendMessage("§e/apd save §a>>保存抽奖项目配置");
				sender.sendMessage("§e/apd load §a>>从配置文件加载抽奖项目配置");
				sender.sendMessage("§e/apd 3 §a>>查看APD命令指南第3页");
				
				break;
				
			case 3:
				sender.sendMessage("§6§lAnotherPrizeDraw §b命令指南 §d第3页");
				sender.sendMessage("§e/apd silence [抽奖项目名称] true/false §a>>开启或关闭静默模式(静默模式下抽奖过程没有声音和标题)");
				sender.sendMessage("§e/apd get [抽奖项目名称] §a>>获取该项目的主消耗物品");
				sender.sendMessage("§e/apd gettaken [抽奖项目名称] §a>>获取该项目的其他消耗物品");
				sender.sendMessage("§e/apd getall [抽奖项目名称] §a>>获取该项目的所有消耗物品");
				sender.sendMessage("§e/apd setcooldown [抽奖项目名称] [秒] §a>>设置抽奖项目冷却时间");

				break;
				
			default:
				sender.sendMessage("§cAPD命令指南没有此页！");
				break;
		}
		sendSeparator(sender);
	}
}