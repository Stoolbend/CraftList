package me.Stoolbend.CraftList;
 
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
//import org.bukkit.plugin.PluginManager;

public class CraftList extends JavaPlugin {
 
	Logger log = Logger.getLogger("Minecraft");
	
	private int taskId = 0;
	public static Server server;
	protected static Configuration config;
	
	public void onDisable(){
		this.getServer().getScheduler().cancelTask(this.taskId);
		log.info("[CLIST] CraftList 1.0.1 Alpha - DISABLED");
	}
	
	public void onEnable(){
		log.info("[CLIST] Loading plugin...");
		config = getConfiguration();
		//Basic configuration file checking
		if(config.getInt("system", 0) == 0){
			log.info("[CLIST] WARN! - config.yml was not found or corrupted!");
			config.setHeader("# CraftList 1.0.1 Alpha - Fill in the details below making sure it points to the same MySQL database as your CraftList install.");
			config.setProperty("host", "localhost");
			config.setProperty("port", 3306);
			config.setProperty("database", "craftlist");
			config.setProperty("username", "root");
			config.setProperty("password", "root");
			config.setProperty("system", 1);
			config.save();
			log.info("[CLIST] WARN! - Generated new config.yml");
		}
		
		//PluginManager pm = this.getServer().getPluginManager();
		//log.info("[CLIST] PluginManager active");
		
		getCommand("clist").setExecutor(new CraftListCommandManager(this));
		CraftListCommandManager.server = this.getServer();
		server = this.getServer();
		log.info("[CLIST] CommandManager active");
		
		log.info("[CLIST] CraftList 1.0.1 Alpha - ENABLED");
	}
}