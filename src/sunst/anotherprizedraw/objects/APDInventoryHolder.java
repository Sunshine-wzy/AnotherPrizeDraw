package sunst.anotherprizedraw.objects;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class APDInventoryHolder<T> implements InventoryHolder {
	protected Inventory inventory;
	protected T data;
	
	
	public APDInventoryHolder(T data, int size, String title) {
		this.data = data;
		
		this.inventory = Bukkit.createInventory(this, size, title);
	}


	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public T getData() {
		return data;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
	public static APDInventoryHolder<?> getHolder(Inventory inv) {
		InventoryHolder holder = inv.getHolder();
		if(!(holder instanceof APDInventoryHolder))
			return null;
		
		return (APDInventoryHolder<?>) holder;
	}

}
