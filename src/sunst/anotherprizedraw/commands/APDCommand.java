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
			sender.sendMessage("��c������op�����ܲ鿴��ʹ��AnotherPrizeDraw��ָ�");
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
					sender.sendMessage("��a�����ļ�����ɹ���");
					return true;
				}

				if(args[0].equalsIgnoreCase("load")){
					AnotherPrizeDraw.loadResConfig();
					sender.sendMessage("��a�����ļ����سɹ���");
					return true;
				}
			}
			
			if(!(sender instanceof Player)){
				sender.sendMessage("��c���ָ����Ҫ���ִ�У�");
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
						p.sendMessage("��c����create��������Ҫ�����ĳ齱��Ŀ����");
						break;
					}
					
					if(allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c���Ѵ�������Ϊ��e"+str2+"��c�ĳ齱��Ŀ�ˣ������ظ�������");
						break;
					}
					
					if(handItem==null || handItem.getType()==Material.AIR){
						p.sendMessage("��c��û�����ų齱��������Ʒ��");
						break;
					}
					
					if(str3==null || str3.isEmpty()){
						p.sendMessage("��c���ڳ齱��Ŀ���ƺ��������ģʽ(shared/independent)");
						p.sendMessage("��b[sharedΪ������ʣ�independentΪ��������](��ʹ��Tab�Զ���ȫ)");
						break;
					}
					
					str3 = str3.toLowerCase();
					if(!str3.equalsIgnoreCase("shared") && !str3.equalsIgnoreCase("independent")){
						p.sendMessage("��c������shared��independent!");
						break;
					}
					
					if(str4==null || str4.isEmpty()){
						p.sendMessage("��c���ڸ���ģʽ�����봥��������Ʒ����");
						break;
					}
					
					if(!str4.matches("\\d+") || Integer.parseInt(str4)<0){
						p.sendMessage("��c������һ���Ǹ�������");
						break;
					}
					
					boolean isSilence = false;
					if(str5!=null){
						if(str5.equalsIgnoreCase("true"))
							isSilence = true;
						else if(!str5.equalsIgnoreCase("false")){
							p.sendMessage("��c������true/false�������Ƿ�����Ĭģʽ(�齱�����������ͱ���)");
							break;
						}
					}
					
					int conQuantity = Integer.parseInt(str4);
					allPrizeDrawObjects.put(str2, new PrizeDrawObject(handItem, PrizeDrawObject.PDOMode.getByName(str3), conQuantity, isSilence, 0));
					
					p.sendMessage("��a��Ϊ��������Ϊ��e"+str2+"��a�ĳ齱��Ŀ~");
					sendSeparator(p);
					p.sendMessage("��a����Ŀ�齱��������ƷΪ["+getItemName(handItem)+"��a]");
					p.sendMessage("��a����Ŀ����ģʽΪ��d"+str3);
					if(conQuantity==0)
						p.sendMessage("��a����Ŀ���ᴥ������");
					else p.sendMessage("��a����Ŀ����������Ʒ����Ϊ��b"+conQuantity);
					sendSeparator(p);
					
					break;
					
				case "remove":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����remove��������Ҫ�Ƴ��ĳ齱��Ŀ����");
						break;
					}
					
					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����δ��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}
					
					if(str3==null || str3.isEmpty()){
						p.sendMessage("��c���ڳ齱��Ŀ���ƺ�����accept��ȷ��ɾ����");
						break;
					}
					
					str3 = str3.toLowerCase();
					if(!str3.equalsIgnoreCase("accept")){
						p.sendMessage("��c������accept��ȷ��ɾ��(����ؽ���ɾ��)!");
						break;
					}
					
					allPrizeDrawObjects.remove(str2);
					p.sendMessage("��a�ɹ�����Ϊ��e"+str2+"��a�ĳ齱��Ŀ�Ƴ���");
					break;
					
				case "group":
					if(str2==null || str2.isEmpty()){
						sendSeparator(sender);
						sender.sendMessage("��6��lAnotherPrizeDraw ��bGroup������Ʒ��������� ��d��1ҳ");
						sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] ��a>>�鿴һ��������Ʒ�����Ϣ");
						sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] create ���и���ĸ��� ��a>>����һ��������Ʒ��");
						sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] remove ��a>>�Ƴ�һ��������Ʒ��");
						sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] add ��a>>Ϊ�����һ��������Ʒ ��c(���ֳֽ�����Ʒ����)");
						sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] edit ��a>>���ӻ��༭һ����(GUI����)");
						sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] setmsg ��Ϣ���� ��a>>���ó��и������ȫ�����͵���Ϣ");
						sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] addcmd ָ������(����/) ��a>>���ó��и����ִ�е�ָ��");
						sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] delcmd ָ����� ��a>>ɾ�����и����ִ��ĳһָ��" +
								"��b(ָ���������/apd group [�齱��Ŀ����] [������] ���鿴)");
						sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] setpro ���и���ĸ��� ��a>>���ó��и���ĸ���(0-100֮���ʵ��)");
						sender.sendMessage("��e/apd g2 ��a>>�鿴Group������Ʒ�����������2ҳ");
						sendSeparator(sender);
						break;
					}
					
					if(str4!=null){
						str4 = str4.toLowerCase();
						
						if(str4.equals("create")){
							if(str3==null || str3.isEmpty()){
								p.sendMessage("��c����createǰ������Ҫ������������");
								break;
							}

							if(allPrizeDrawObjects.get(str2).getGroups().containsKey(str3)){
								p.sendMessage("��c�����ڡ�e"+str2+"��c�д�������Ϊ��6"+str3+"��c�����ˣ������ظ�������");
								break;
							}

							if(str5==null || str5.isEmpty()){
								p.sendMessage("��c����create��������и���ĸ���(һ��0-100֮���ʵ����Ϊ�ٷֱ�)");
								break;
							}

							double probability;
							try {
								probability = Double.parseDouble(str5);
							} catch (NumberFormatException ex) {
								p.sendMessage("��c������ĸ��ʲ���ȷ��");
								break;
							}
							allPrizeDrawObjects.get(str2).addGroup(str3, probability);

							p.sendMessage("��a�ɹ��ڡ�e"+str2+"��a�д������6"+str3);
							p.sendMessage("��a���и���ĸ���Ϊ��b"+probability+"��d%");
							break;
						}
					}
					
					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ������/apd create������");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("��c���ڳ齱��Ŀ������������");
						p.sendMessage("��b��Ŀ��e"+str2+"��b�����飺");
						for(String groupName : allPrizeDrawObjects.get(str2).getGroups().keySet())
							p.sendMessage(groupName);
						break;
					}

					Map<String, APDGroup> groupMap = allPrizeDrawObjects.get(str2).getGroups();
					if(!groupMap.containsKey(str3)){
						p.sendMessage("��c����û�ڡ�e"+str2+"��c�д�������Ϊ��6"+str3+"��c���飬���á�a/apd group [��Ŀ��] [����] create ���и���ĸ��� ��c��������");
						break;
					}

					APDGroup infoGroup = groupMap.get(str3);
					if(str4==null || str4.isEmpty()){
						sendSeparator(p);
						p.sendMessage("��a��Ŀ��e"+str2+"��a�еĽ�����Ʒ�����ƣ���6"+str3);
						p.sendMessage("��a���и���ĸ���Ϊ��b"+infoGroup.getProbability()+"��d%");
						if(infoGroup.getMessage()!=null){
							p.sendMessage("��a���и������ȫ�����͵���ϢΪ��");
							p.sendMessage(infoGroup.getMessage());
						}
						if(infoGroup.getCommands()!=null && !infoGroup.getCommands().isEmpty()){
							p.sendMessage("��a���и����ִ�е�����Ϊ��");
							for(int i=0; i<infoGroup.getCommands().size(); i++){
								p.sendMessage("��a"+i+": ��f/"+infoGroup.getCommands().get(i));
							}
						}
						sendSeparator(p);
						break;
					}
					
					switch (str4) {
						case "add":
							if(handItem==null || handItem.getType()==Material.AIR){
								p.sendMessage("��c��û�����Ž�����Ʒ��");
								break;
							}

							infoGroup.addReward(handItem);
							p.sendMessage("��a�ɹ�Ϊ��e"+str2+"��a�е����6"+str3+"��a��ӽ�����Ʒ["+getItemName(handItem)+"��a]");
							break;
							
						case "remove":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("��c���������ƺ�����accept��ȷ��ɾ����");
								break;
							}

							str5 = str5.toLowerCase();
							if(!str5.equalsIgnoreCase("accept")){
								p.sendMessage("��c������accept��ȷ��ɾ��(����ؽ���ɾ��)!");
								break;
							}
							
							groupMap.remove(str3);
							p.sendMessage("��a�ɹ�����e"+str2+"��a�еġ�6"+str3+"��a���Ƴ���");
							break;
							
						case "edit":
							if(infoGroup.getRewardItems()==null || infoGroup.getRewardItems().isEmpty()){
								p.sendMessage("��a��Ŀ��e"+str2+"��a�е����6"+str3+"��aû���κν�����Ʒ!");
								break;
							}
							
							Map<String, Object> dataMap = new HashMap<>();
							dataMap.put("APDGroup", infoGroup);
							dataMap.put("firstOrder", 0);
							Inventory editInv = new APDInventoryHolder<>(dataMap, 5 * 9, "��a��Ŀ��e" + str2 + "��a�е����6" + str3).getInventory();
							APDManager.createEdge(editInv, new ItemStack(Material.STAINED_GLASS_PANE));
							
							for(int i=0; i<infoGroup.getRewardItems().size(); i++){
								if(i>=27){
									editInv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
											"��a��һҳ ��e\u21E8"));
									break;
								}
								editInv.setItem(9+i, infoGroup.getRewardItems().get(i));
							}
							
							p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
							p.openInventory(editInv);
							break;
							
						case "setmsg":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("��c����setmsg��������и������ȫ�����͵���Ϣ����");
								p.sendMessage("��b��ʹ��&��Ϊ��ɫ����");
								break;
							}

							StringBuilder newMsg = new StringBuilder();
							for(int i=4; i<args.length; i++)
								newMsg.append(args[i]).append(" ");
							
							infoGroup.setMessage(newMsg.toString());
							p.sendMessage("��a�ɹ�����e"+str2+"��a�е����6"+str3+"��a�����к���ȫ�����͵���Ϣ����Ϊ��");
							p.sendMessage(infoGroup.getMessage());
							break;
							
						case "addcmd":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("��c����addcmd��������и����ִ�е�ָ��(��������/)");
								p.sendMessage("��b����ָ������{player}������ң���ʹ��&��Ϊ��ɫ����");
								break;
							}

							StringBuilder newCommand = new StringBuilder();
							for(int i=4; i<args.length; i++)
								newCommand.append(args[i]).append(" ");

							infoGroup.addCommand(newCommand.toString());
							p.sendMessage("��a�ɹ�Ϊ��e"+str2+"��a�е����6"+str3+"��a����˱����к�ִ�е�ָ�");
							p.sendMessage("/"+newCommand.toString().replace('&', '��'));
							break;
							
						case "delcmd":
							if(str5==null || !str5.matches("\\d+") || Integer.parseInt(str5)<0){
								p.sendMessage("��c����delcmd������һ��Ϊ�Ǹ�������ָ����š�b(ָ���������/apd group [�齱��Ŀ����] [������] ���鿴)");
								break;
							}
							
							int commandOrder = Integer.parseInt(str5);
							if(commandOrder>=infoGroup.getCommands().size()){
								p.sendMessage("��c������ָ����Ų����ڣ�");
								break;
							}
							
							p.sendMessage("��a�ɹ��Ƴ���e"+str2+"��a�е����6"+str3+"��a�еĵ�"+commandOrder+"��ָ�");
							p.sendMessage("/"+infoGroup.getCommands().get(commandOrder));
							infoGroup.getCommands().remove(commandOrder);
							break;
							
						case "setpro":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("��c����setpro��������и���ĸ���(һ��0-100֮���ʵ����Ϊ�ٷֱ�)");
								break;
							}

							double probability;
							try {
								probability = Double.parseDouble(str5);
							} catch (NumberFormatException ex) {
								p.sendMessage("��c������ĸ��ʲ���ȷ��");
								break;
							}
							
							infoGroup.setProbability(probability);
							p.sendMessage("��a�ɹ�����e"+str2+"��a�е����6"+str3+"��a�ĳ��и�������Ϊ����b"+probability+"��d%");
							break;
							
						case "rename":
							if(str5==null || str5.isEmpty()){
								p.sendMessage("��c����rename����������������");
								break;
							}
							
							if(groupMap.containsKey(str5)){
								p.sendMessage("��c���������Ѵ��ڣ��뻻һ����");
								break;
							}
							
							groupMap.put(str5, infoGroup);
							groupMap.remove(str3);

							p.sendMessage("��a�ѽ���d"+str3+"��a������Ϊ��6"+str5);
							break;
							
						case "get":
							List<ItemStack> groupRewardItems = infoGroup.getRewardItems();
							if(groupRewardItems==null || groupRewardItems.isEmpty()){
								p.sendMessage("��c����û�н�����Ʒ��");
								break;
							}
							
							for(ItemStack groupItem : groupRewardItems)
								p.getWorld().dropItem(p.getLocation(), groupItem);
							break;
					}
					
					break;
					
				case "g2":
					sendSeparator(sender);
					sender.sendMessage("��6��lAnotherPrizeDraw ��bGroup������Ʒ��������� ��d��2ҳ");
					sender.sendMessage("��e/apd group [�齱��Ŀ����] [�ɵ�������] rename [�µ�������] ��a>>������һ��������Ʒ��");
					sender.sendMessage("��e/apd group [�齱��Ŀ����] [������] get ��a>>��øý�����Ʒ������н�����Ʒ");
					sendSeparator(sender);
					break;
					
					
				case "addbottom":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����addbottom���������еĳ齱��Ŀ���ƣ���û�У�����/apd create������");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ������/apd create������");
						break;
					}

					if(handItem==null || handItem.getType()==Material.AIR){
						p.sendMessage("��c��û�����ű��׽�����Ʒ��");
						break;
					}
					
					allPrizeDrawObjects.get(str2).addBottomReward(handItem);

					
					p.sendMessage("��a�ɹ�Ϊ��e"+str2+"��a��ӱ��׽�����Ʒ["+getItemName(handItem)+"��a]");
					
					break;
					
				case "addtaken":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����addtaken���������еĳ齱��Ŀ���ƣ���û�У�����/apd create������");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ������/apd create������");
						break;
					}

					if(handItem==null || handItem.getType()==Material.AIR){
						p.sendMessage("��c��û�����ų齱������Ʒ��");
						break;
					}
					
					allPrizeDrawObjects.get(str2).addOtherTakenItem(handItem);
					
					p.sendMessage("��a�ɹ�Ϊ��e"+str2+"��a���������Ʒ["+getItemName(handItem)+"��a]");
					
					break;
					
				case "list":
					if(allPrizeDrawObjects.isEmpty()){
						p.sendMessage("��c��ǰ��û���κγ齱��Ŀ��������/apd create����һ����~");
						break;
					}
					
					p.sendMessage("��a��ǰ���г齱��Ŀ���ƣ�");
					for(Map.Entry<String, PrizeDrawObject> entry : allPrizeDrawObjects.entrySet()){
						p.sendMessage(entry.getKey());
					}
					
					break;
					
				case "info":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����info���������еĳ齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}

					PrizeDrawObject infoPDO = allPrizeDrawObjects.get(str2);
					sendSeparator(p);
					p.sendMessage("��a�齱��Ŀ���ƣ���e"+str2);
					p.sendMessage("��a����Ŀ�齱��������ƷΪ["+getItemName(infoPDO.getTakenItem())+"��a]");
					p.sendMessage("��a����Ŀ����ģʽΪ��d"+infoPDO.getMode().getName());
					if(infoPDO.getContinuousQuantity()==0)
						p.sendMessage("��a����Ŀ���ᴥ������");
					else p.sendMessage("��a����Ŀ����������Ʒ����Ϊ��b"+infoPDO.getContinuousQuantity());
					sendSeparator(p);


					break;

				case "editbom":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����editbom���������еĳ齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}
					
					PrizeDrawObject bomPDO = allPrizeDrawObjects.get(str2);

					if(bomPDO.getBottomRewards()==null || bomPDO.getBottomRewards().isEmpty()){
						p.sendMessage("��a��Ŀ��e"+str2+"��aû���κα�����Ʒ!");
						break;
					}
					
					Map<String, Object> dataMapBom = new HashMap<>();
					dataMapBom.put("BomPDO", bomPDO);
					dataMapBom.put("firstOrder", 0);
					Inventory bomEditInv = new APDInventoryHolder<>(dataMapBom, 5 * 9, "��a��Ŀ��e" + str2 + "��a�ı�����Ʒ").getInventory();
					APDManager.createEdge(bomEditInv, new ItemStack(Material.STAINED_GLASS_PANE));

					for(int i=0; i<bomPDO.getBottomRewards().size(); i++){
						if(i>=27){
							bomEditInv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"��a��һҳ ��e\u21E8"));
							break;
						}
						bomEditInv.setItem(9+i, bomPDO.getBottomRewards().get(i));
					}

					p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
					p.openInventory(bomEditInv);
					break;

				case "edittaken":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����edittaken���������еĳ齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}
					
					PrizeDrawObject takenPDO = allPrizeDrawObjects.get(str2);

					if(takenPDO.getOtherTakenItems()==null || takenPDO.getOtherTakenItems().isEmpty()){
						p.sendMessage("��a��Ŀ��e"+str2+"��aû���κ�������Ʒ!");
						break;
					}
					
					Map<String, Object> dataMapTaken = new HashMap<>();
					dataMapTaken.put("TakenPDO", takenPDO);
					dataMapTaken.put("firstOrder", 0);
					Inventory takenEditInv = new APDInventoryHolder<>(dataMapTaken, 5 * 9, "��a��Ŀ��e" + str2 + "��a��������Ʒ").getInventory();
					APDManager.createEdge(takenEditInv, new ItemStack(Material.STAINED_GLASS_PANE));

					for(int i=0; i<takenPDO.getOtherTakenItems().size(); i++){
						if(i>=27){
							takenEditInv.setItem(cooToOrder(7, 5), new APDItem(Material.STAINED_GLASS_PANE, (short) 5,
									"��a��һҳ ��e\u21E8"));
							break;
						}
						takenEditInv.setItem(9+i, takenPDO.getOtherTakenItems().get(i));
					}

					p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1.2f);
					p.openInventory(takenEditInv);
					break;
					
				case "rename":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����rename���������еĳ齱��Ŀ�����Լ��µĳ齱��Ŀ����");
						break;
					}
					
					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��������������");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("��c���ھ���Ŀ������������Ŀ��");
						break;
					}
					
					allPrizeDrawObjects.put(str3, allPrizeDrawObjects.get(str2));
					allPrizeDrawObjects.remove(str2);
					p.sendMessage("��a�ѽ���d"+str2+"��a������Ϊ��e"+str3);
					
					break;
					
				case "set":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����set������齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}

					if(handItem==null || handItem.getType()==Material.AIR){
						p.sendMessage("��c��û�����ų齱��������Ʒ��");
						break;
					}
					
					allPrizeDrawObjects.get(str2).setTakenItem(handItem);
					p.sendMessage("��a�ѽ���e"+str2+"��a����������Ʒ����Ϊ["+getItemName(handItem)+"��a]");
					
					break;
					
				case "setmode":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����setmode������齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("��c���ڳ齱��Ŀ���ƺ��������ģʽ(shared/independent)");
						p.sendMessage("��b[sharedΪ������ʣ�independentΪ��������](��ʹ��Tab�Զ���ȫ)");
						break;
					}
					
					str3 = str3.toLowerCase();
					if(!str3.equalsIgnoreCase("shared") && !str3.equalsIgnoreCase("independent")){
						p.sendMessage("��c������shared��independent!");
						break;
					}
					
					allPrizeDrawObjects.get(str2).setMode(str3);
					p.sendMessage("��a�ѽ���e"+str2+"��a�ĸ���ģʽ����Ϊ��d"+str3);
					
					break;
					
				case "setcon":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����setcon������齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("��c�����봥��������Ʒ����");
						break;
					}

					if(!str3.matches("\\d+") || Integer.parseInt(str3)<0){
						p.sendMessage("��c������һ���Ǹ�������");
						break;
					}

					int newConQuantity = Integer.parseInt(str3);
					allPrizeDrawObjects.get(str2).setContinuousQuantity(newConQuantity);
					p.sendMessage("��a�ѽ���e"+str2+"��a�Ĵ���������Ʒ��������Ϊ��b"+newConQuantity);
					
					break;
					
//				case "recadd":
//					if(handItem==null || handItem.getType()==Material.AIR){
//						p.sendMessage("��c�뽫��Ҫ�Զ���ϳɵ���Ʒ�������У�(��������)");
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
//						p.sendMessage("��c�뽫��Ҫ�Ƴ��Զ���ϳɵ���Ʒ�������У�(��������)");
//						break;
//					}
//					
//					if(!APDRecipe.allAPDRecipe.containsValue(handItem)){
//						p.sendMessage("��c�����Ʒû���Զ���ϳɣ�(��������)");
//						break;
//					}
//					
//					break;
				
				case "silence":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����silence������齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}

					if(str3==null || str3.isEmpty()){
						p.sendMessage("��c������true��false�Կ�����رվ�Ĭģʽ(�齱����û�������ͱ���)");
						break;
					}

					boolean setSilence = false;
					if(str3.equalsIgnoreCase("true"))
						setSilence = true;
					else if(!str3.equalsIgnoreCase("false")){
						p.sendMessage("��c������true��false�Կ�����رվ�Ĭģʽ(�齱����û�������ͱ���)");
						break;
					}
					
					allPrizeDrawObjects.get(str2).setSilence(setSilence);
					p.sendMessage("��a�ɹ�����Ŀ��e"+str2+"��a�ľ�Ĭģʽ����Ϊ����f"+setSilence);
					break;
					
				case "get":
					if(str2==null || str2.isEmpty()){
						p.sendMessage("��c����get������齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
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
						p.sendMessage("��c����gettaken������齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}

					List<ItemStack> otherTakenItemsGet = allPrizeDrawObjects.get(str2).getOtherTakenItems();
					if(otherTakenItemsGet==null || otherTakenItemsGet.isEmpty()){
						p.sendMessage("��c��Ŀ��e"+str2+"��cû������������Ʒ��");
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
						p.sendMessage("��c����getall������齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
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
						p.sendMessage("��c����setcooldown������齱��Ŀ����");
						break;
					}

					if(!allPrizeDrawObjects.containsKey(str2)){
						p.sendMessage("��c����û��������Ϊ��e"+str2+"��c�ĳ齱��Ŀ��");
						break;
					}

					if(!str3.matches("\\d+") || Integer.parseInt(str3) < 0){
						p.sendMessage("��c������һ���Ǹ�������");
						break;
					}

					int cooldown = Integer.parseInt(str3);

					allPrizeDrawObjects.get(str2).setCooldown(cooldown);
					p.sendMessage("��a�ɹ�����Ŀ��e"+str2+"��a����ȴʱ������Ϊ����f"+cooldown+"��");
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
				sender.sendMessage("��6��lAnotherPrizeDraw ��b����ָ�� ��d��1ҳ");
				sender.sendMessage("��e/apd create [�齱��Ŀ����] ����ģʽ(shared/independent) ����������Ʒ����(0�򲻴�������) " +
						"�Ƿ�����Ĭģʽ(true/false,��ʡ����Ϊfalse) ��a>>����һ���齱��Ŀ ��c(���ֳֳ齱���ĵ�����Ʒ����)");
				sender.sendMessage("��e/apd remove [�齱��Ŀ����] ��a>>�Ƴ�һ���齱��Ŀ ��c(�����ʹ��)");
				sender.sendMessage("��e/apd group ��a>>�鿴������Ʒ���������");
				sender.sendMessage("��e/apd addbottom [�齱��Ŀ����] ��a>>Ϊ�齱��Ŀ���һ�����׽�����Ʒ ��c(���ֱֳ��׽�����Ʒ����)");
				sender.sendMessage("��e/apd addtaken [�齱��Ŀ����] ��a>>Ϊ�齱��Ŀ���һ��������Ʒ ��c(���ֳֳ齱������Ʒ����)");
				sender.sendMessage("��e/apd list ��a>>�鿴��ǰ���г齱��Ŀ����");
				sender.sendMessage("��e/apd info [�齱��Ŀ����] ��a>>�鿴ָ���齱��Ŀ����Ϣ");
				sender.sendMessage("��e/apd 2 ��a>>�鿴APD����ָ�ϵ�2ҳ");
				
				break;
			
			case 2:
				sender.sendMessage("��6��lAnotherPrizeDraw ��b����ָ�� ��d��2ҳ");
				sender.sendMessage("��e/apd editbom [�齱��Ŀ����] ��a>>GUI�༭һ���齱��Ŀ�ı�����Ʒ(�鿴���Ƴ�)");
				sender.sendMessage("��e/apd edittaken [�齱��Ŀ����] ��a>>GUI�༭һ���齱��Ŀ��������Ʒ(�鿴���Ƴ�)");
				sender.sendMessage("��e/apd rename [�ɵĳ齱��Ŀ����] [�µĳ齱��Ŀ����] ��a>>���ĳ齱��Ŀ����");
				sender.sendMessage("��e/apd set [�齱��Ŀ����] ��a>>���ó齱��Ŀ��������Ʒ ��c(���ֳֳ齱���ĵ�����Ʒ����)");
				sender.sendMessage("��e/apd setmode [�齱��Ŀ����] ����ģʽ(shared/independent) ��a>>���ó齱��Ŀ����ģʽ");
				sender.sendMessage("��e/apd setcon [�齱��Ŀ����] ����������Ʒ���� ��a>>���ó齱��Ŀ�����������Ʒ����");
//				sender.sendMessage("��e/apd recadd ��a>>����ֳ���Ʒ�ĺϳ�(��������)");
//				sender.sendMessage("��e/apd recdel ��a>>�Ƴ��ֳ���Ʒ�ĺϳ�(��������)");
				sender.sendMessage("��e/apd save ��a>>����齱��Ŀ����");
				sender.sendMessage("��e/apd load ��a>>�������ļ����س齱��Ŀ����");
				sender.sendMessage("��e/apd 3 ��a>>�鿴APD����ָ�ϵ�3ҳ");
				
				break;
				
			case 3:
				sender.sendMessage("��6��lAnotherPrizeDraw ��b����ָ�� ��d��3ҳ");
				sender.sendMessage("��e/apd silence [�齱��Ŀ����] true/false ��a>>������رվ�Ĭģʽ(��Ĭģʽ�³齱����û�������ͱ���)");
				sender.sendMessage("��e/apd get [�齱��Ŀ����] ��a>>��ȡ����Ŀ����������Ʒ");
				sender.sendMessage("��e/apd gettaken [�齱��Ŀ����] ��a>>��ȡ����Ŀ������������Ʒ");
				sender.sendMessage("��e/apd getall [�齱��Ŀ����] ��a>>��ȡ����Ŀ������������Ʒ");
				sender.sendMessage("��e/apd setcooldown [�齱��Ŀ����] [��] ��a>>���ó齱��Ŀ��ȴʱ��");

				break;
				
			default:
				sender.sendMessage("��cAPD����ָ��û�д�ҳ��");
				break;
		}
		sendSeparator(sender);
	}
}