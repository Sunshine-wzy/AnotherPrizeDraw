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
		
		loggerAPD.info("��������AnotherPrizeDraw�����");
		loggerAPD.info("���ߣ�Sunshine_wzy");
		loggerAPD.info("Bug����QȺ��423179929");

		//ע�����л���
		ConfigurationSerialization.registerClass(PrizeDrawObject.class);
		ConfigurationSerialization.registerClass(APDGroup.class);
		
		//ע��ָ��
		Bukkit.getPluginCommand("apd").setExecutor(new APDCommand());
		Bukkit.getPluginCommand("apd").setTabCompleter(new APDTabCompleter());
		
		//ע�������
		registerListeners();
		
		//���������ļ�
		loadResConfig();
		
	}

	@Override
	public void onDisable() {
		//ȡ����������
		Bukkit.getScheduler().cancelTasks(this);
		
		//���������ļ�
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
				loggerAPD.info("�����ļ��������");
			}
		} else if(dir.mkdirs()){
			loggerAPD.info("�����ļ��гɹ�����");
		} else loggerAPD.info("�����ļ�������ʧ��");
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
							loggerAPD.info("��ɾ������������ļ�");
						} else loggerAPD.info("����������ļ�ɾ��ʧ��");
					}
				}
				
			} finally {
				loggerAPD.info("�����ļ�����ɹ�");
			}
		} else if(dir.mkdirs()){
			loggerAPD.info("�����ļ��гɹ�����");
		} else loggerAPD.info("�����ļ�������ʧ��");
		
	}
	
}
