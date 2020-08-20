package sunst.anotherprizedraw;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import sunst.anotherprizedraw.commands.APDCommand;
import sunst.anotherprizedraw.commands.APDTabCompleter;
import sunst.anotherprizedraw.listeners.BlockLister;
import sunst.anotherprizedraw.listeners.InventoryListener;
import sunst.anotherprizedraw.listeners.PlayerListener;
import sunst.anotherprizedraw.objects.APDGroup;
import sunst.anotherprizedraw.objects.PrizeDrawObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static sunst.anotherprizedraw.maps.PDOMap.allPrizeDrawObjects;

public class AnotherPrizeDraw extends JavaPlugin {
	public static Plugin pluginAPD;
	public static Logger loggerAPD;
	public static PluginManager pluginManager;
	
	public static final String PATH_PDO = "PrizeDrawObjects";
	public static final String PATH_RECIPE = "Recipes";
	public static File dataFolder;
	
	
	@Override
	public void onEnable() {
		pluginAPD = this;
		loggerAPD = this.getLogger();
		pluginManager = Bukkit.getServer().getPluginManager();
		dataFolder = this.getDataFolder();
		
		loggerAPD.info("您已启用AnotherPrizeDraw插件！");
		loggerAPD.info("作者：Sunshine_wzy");
		loggerAPD.info("Bug反馈Q群：423179929");

		//注册序列化类
		ConfigurationSerialization.registerClass(PrizeDrawObject.class);
		ConfigurationSerialization.registerClass(APDGroup.class);
		
		//注册指令
		Bukkit.getPluginCommand("apd").setExecutor(new APDCommand());
		Bukkit.getPluginCommand("apd").setTabCompleter(new APDTabCompleter());
		
		//注册监听器
		registerListeners();
		
		//载入配置文件
		loadResConfig();
		
	}

	@Override
	public void onDisable() {
		//取消调度任务
		Bukkit.getScheduler().cancelTasks(this);
		
		//保存配置文件
		saveResConfig();
	}


	private void registerListeners() {
		pluginManager.registerEvents(new PlayerListener(), this);
		pluginManager.registerEvents(new BlockLister(), this);
		pluginManager.registerEvents(new InventoryListener(), this);
	}
	
	
	public static void loadResConfig() {
		File dir = new File(dataFolder, PATH_PDO);
		if(dir.exists()){
			try {
				for(File file : Objects.requireNonNull(dir.listFiles())){
					if(file.getName().endsWith(".yml")){
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
						
						String name = file.getName().replace(".yml", "");
						allPrizeDrawObjects.put(name, (PrizeDrawObject) cfg.get("SunST"));
					}
				}
			} finally {
				loggerAPD.info("配置文件加载完成");
			}
		} else if(dir.mkdirs()){
			loggerAPD.info("配置文件夹成功生成");
		} else loggerAPD.info("配置文件夹生成失败");
	}
	
	public static void saveResConfig() {
		File dir = new File(dataFolder, PATH_PDO);
		if(dir.exists()){
			try {
				List<String> fileNames = new ArrayList<>();
				List<String> usedNames = new ArrayList<>();
				for(File file : Objects.requireNonNull(dir.listFiles()))
					fileNames.add(file.getName());
				
				for(Map.Entry<String, PrizeDrawObject> entry : allPrizeDrawObjects.entrySet()){
					File cfgFile = new File(dir, entry.getKey()+".yml");
					
					if(fileNames.contains(cfgFile.getName())){
						usedNames.add(cfgFile.getName());
						
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(cfgFile);
						cfg.set("SunST", entry.getValue());
						
						try {
							cfg.save(cfgFile);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					} else {
						FileConfiguration cfg = new YamlConfiguration();
						cfg.set("SunST", entry.getValue());

						try {
							cfg.save(cfgFile);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
				
				for(String str : fileNames){
					if(!usedNames.contains(str)){
						File noUseFile = new File(dir, str);
						if(noUseFile.delete()){
							loggerAPD.info("已删除多余的配置文件");
						} else loggerAPD.info("多余的配置文件删除失败");
					}
				}
				
			} finally {
				loggerAPD.info("配置文件保存成功");
			}
		} else if(dir.mkdirs()){
			loggerAPD.info("配置文件夹成功生成");
		} else loggerAPD.info("配置文件夹生成失败");
		
	}
	
}
