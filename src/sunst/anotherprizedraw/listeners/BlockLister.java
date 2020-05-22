package sunst.anotherprizedraw.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import sunst.anotherprizedraw.managers.APDManager;
import sunst.anotherprizedraw.maps.PDOMap;
import sunst.anotherprizedraw.objects.PrizeDrawObject;

public class BlockLister implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		ItemStack handItem = e.getItemInHand();
		
		for(PrizeDrawObject objectPD : PDOMap.allPrizeDrawObjects.values()){
			ItemStack takenItem = objectPD.getTakenItem();
			if(APDManager.isItemSimilar(handItem, takenItem)){
				e.setCancelled(true);
				return;
			}
		}
	}
	
}
