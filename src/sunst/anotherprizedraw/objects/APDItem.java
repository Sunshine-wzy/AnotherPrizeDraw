package sunst.anotherprizedraw.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sunst.anotherprizedraw.managers.APDManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class APDItem extends ItemStack {
	public APDItem(Material mtl, int amount, short damage, String name, String... lore) {
		super(mtl, amount, damage);
		ItemMeta meta = this.hasItemMeta() ? this.getItemMeta(): Bukkit.getItemFactory().getItemMeta(this.getType());

		//设置lore
		List<String> loreList = new ArrayList<>();
		Collections.addAll(loreList, lore);
		meta.setLore(loreList);

		//设置name
		meta.setDisplayName(name);
		this.setItemMeta(meta);
	}
	
	public APDItem(Material mtl, int amount, String name, String... lore) {
		this(mtl, amount, (short) 0, name, lore);
	}
	
	public APDItem(Material mtl, short damage, String name, String... lore) {
		this(mtl, 1, damage, name, lore);
	}
	
	public APDItem(Material mtl, String name, String... lore) {
		this(mtl, 1, name, lore);
	}

	public APDItem(Material mtl, int amount, short damage, String name) {
		super(mtl, amount, damage);
		ItemMeta meta = this.hasItemMeta() ? this.getItemMeta(): Bukkit.getItemFactory().getItemMeta(this.getType());

		//设置name
		meta.setDisplayName(name);
		this.setItemMeta(meta);
	}
	
	public APDItem(Material mtl, int amount, String name) {
		this(mtl, amount, (short) 0, name);
	}
	
	public APDItem(Material mtl, short damage, String name) {
		this(mtl, 1, damage, name);
	}
	
	public APDItem(Material mtl, String name) {
		this(mtl, 1, name);
	}
	
	public APDItem(ItemStack item, String name) {
		super(item);
		ItemMeta meta = this.hasItemMeta() ? this.getItemMeta(): Bukkit.getItemFactory().getItemMeta(this.getType());

		//设置name
		meta.setDisplayName(name);
		this.setItemMeta(meta);
	}
	
	public APDItem(ItemStack item, String name, String... lore) {
		super(item);
		ItemMeta meta = this.hasItemMeta() ? this.getItemMeta(): Bukkit.getItemFactory().getItemMeta(this.getType());

		//设置lore
		List<String> loreList = new ArrayList<>();
		Collections.addAll(loreList, lore);
		meta.setLore(loreList);

		//设置name
		meta.setDisplayName(name);
		this.setItemMeta(meta);
	}
	
	
	public boolean isItemSimilar(ItemStack item, boolean lore) {
		return APDManager.isItemSimilar(item, this, lore);
	}
	
	public boolean isItemSimilar(ItemStack item) {
		return APDManager.isItemSimilar(item, this);
	}
}
