package sunst.anotherprizedraw.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import sunst.anotherprizedraw.objects.PrizeDrawObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sunst.anotherprizedraw.maps.PDOMap.allPrizeDrawObjects;

public class APDTabCompleter implements TabCompleter {
	public static List<String> allAPDCommand = Arrays.asList(
			"create", "remove", "group", "g2", "addbottom", "addtaken", "list", "info", "2",
			"editbom", "edittaken", "rename", "set", "setmode", "setcon", "save", "load",
			"silence", "get", "gettaken", "getall"
	);
	
	public static List<String> groupCommand = Arrays.asList(
			"create", "remove", "add", "edit", "setmsg", "addcmd", "delcmd", "setpro",
			"rename", "get"
	);
	
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("apd") && args.length>=1) { // your command name
			List<String> ans = new ArrayList<>(); // makes a ArrayList
			String firstStr = args[0].toLowerCase();
			
			switch (args.length) {
				case 1:
					addTabCommand(ans, firstStr, allAPDCommand);
					break;
					
				case 2:
					switch (firstStr) {
						case "remove":
						case "group":
						case "addbottom":
						case "addtaken":
						case "edit":
						case "info":
						case "editbom":
						case "edittaken":
						case "rename":
						case "set":
						case "setmode":
						case "setcon":
						case "silence":
						case "get":
						case "gettaken":
						case "getall":
							List<String> objectNames = new ArrayList<>(allPrizeDrawObjects.keySet());
							addTabCommand(ans, args[1], objectNames);
							
							if(ans.isEmpty())
								ans.addAll(objectNames);
							
							break;
					}
					break;
					
				case 3:
					switch (firstStr) {
						case "remove":
							ans.add("accept");
							break;
						
						case "setmode":
						case "create":
							if(args[2].startsWith("s")){
								ans.add("shared");
							} else if(args[2].startsWith("i")){
								ans.add("independent");
							} else {
								ans.add("shared");
								ans.add("independent");
							}
							break;
							
						case "group":
							if(args[1]==null || !allPrizeDrawObjects.containsKey(args[1]))
								break;
							PrizeDrawObject thePDO = allPrizeDrawObjects.get(args[1]);

							List<String> groupNames = new ArrayList<>(thePDO.getGroups().keySet());
							addTabCommand(ans, args[2], groupNames);
							
							break;
							
						case "silence":
							addTabCommand(ans, args[2], Arrays.asList("true", "false"));
							break;
					}
					break;
					
				case 4:
					switch (firstStr) {
						case "group":
							addTabCommand(ans, args[3], groupCommand);

							if(ans.isEmpty())
								ans.addAll(groupCommand);

							break;
					}
					break;
					
				case 5:
					switch (firstStr) {
						case "group":
							switch (args[3].toLowerCase()) {
								case "remove":
									ans.add("accept");
									break;
							}
							break;
					}
					break;
					
				default:
					return null;
			}
			
			
			return ans; // returns the possibility's to the client
		}
		return null;
		// this is a little confusing but just put it there
		// it just returns NULL if absolutely nothing has happened
	}
	
	
	public static void addTabCommand(List<String> ans, String arg, List<String> list) {
		arg = arg.toLowerCase();
		
		nextCommand:
		for(String str : list){
			if(arg.startsWith(str.substring(0, 1))){
				if(str.length() < arg.length())
					continue;
				
				for(int i = 0; i < arg.length(); i++){
					if(arg.charAt(i) != str.charAt(i))
						continue nextCommand;
				}
				ans.add(str);
			}
		}
		
		if(ans.isEmpty())
			ans.addAll(list);
	}
	
}
