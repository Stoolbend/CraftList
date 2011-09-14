package me.Stoolbend.CraftList;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import java.sql.*;

public class CraftListCommandManager implements CommandExecutor {
	public static CraftList plugin;
	public static Server server;
	public static List<LivingEntity> entities;
	protected static Configuration CONFIG;
	
	// Thanks for DominionSpy for the help with the DB Connection
	public Connection CLDB;
			
	public CraftListCommandManager(CraftList instance){
		plugin = instance;
		try{
			CONFIG = me.Stoolbend.CraftList.CraftList.config;
			CONFIG.load();
			String sql_host = CONFIG.getString("host", "localhost");
			int sql_port = CONFIG.getInt("port", 3306);
			String sql_db = CONFIG.getString("database", "craftlist");
			String sql_user = CONFIG.getString("username", "root");
			String sql_pass = CONFIG.getString("password", "root");
			CLDB = DriverManager.getConnection("jdbc:mysql://" + sql_host + ":" + sql_port + "/" + sql_db + "?user=" + sql_user + "&password=" + sql_pass);
		}
		catch(SQLException E){
			E.printStackTrace();
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(label.equalsIgnoreCase("clist")){
			
			Player player = (Player) sender;
			
			if(args.length == 1 && args[0].equalsIgnoreCase("apps")){
				// /clist apps - Lists number of "new" apps
				Statement stmt = null;
				ResultSet rs = null;
				int AppCount = 0;
				try {
					stmt = CLDB.createStatement();
					rs = stmt.executeQuery("SELECT COUNT(*) FROM `apps` WHERE `status` = 'sent'");
					rs.next();
					AppCount = rs.getInt(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				sender.sendMessage(ChatColor.GREEN + "[CraftList] There are currently " + AppCount + " new applications.");
				sender.sendMessage(ChatColor.YELLOW + "[CraftList] Visit your CraftList Staff Area to view them!");
			}
			
			if(args.length == 1 && args[0].equalsIgnoreCase("myapp")){
				// /clist myapp - Lists information about the current players application (if they have one)
				// NOTE! This will flesh out through due course
				Statement stmt = null;
				ResultSet rs = null;
				try {
					stmt = CLDB.createStatement();
					rs = stmt.executeQuery("SELECT `status` FROM `apps` WHERE `user` = '"+player.getName()+"';");
					rs.next();
					String appStatus = rs.getString("status");
					if(appStatus.equals("sent"))
					{
						sender.sendMessage(ChatColor.YELLOW + "[CraftList] Your application is still awaiting staff approval");
					}
					else if(appStatus.equals("accepted"))
					{
						sender.sendMessage(ChatColor.YELLOW + "[CraftList] Your application has been " + ChatColor.DARK_GREEN + "Accepted!" + ChatColor.YELLOW);
					}
					else if(appStatus.equals("rejected"))
					{
						sender.sendMessage(ChatColor.YELLOW + "[CraftList] Your application has been " + ChatColor.DARK_RED + "turned down." + ChatColor.YELLOW + " Sorry!");
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "[CraftList] You have not applied yet. :(");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.DARK_RED + "[CraftList] There was an error!");
				}
			}
			
			if(args.length == 0){
				sender.sendMessage(ChatColor.AQUA + "[CraftList] This server is running CraftList 1.0.1 Alpha");
				sender.sendMessage(ChatColor.AQUA + "[CraftList] Stay tuned for updates at www.github.com/Stoolbend/CraftList/");
				sender.sendMessage(ChatColor.YELLOW + "/clist apps " + ChatColor.WHITE + "- Lists the amount of new applications currently waiting");
				sender.sendMessage(ChatColor.YELLOW + "/clist myapp " + ChatColor.WHITE + "- Check the status of your application (if you applied)");
			}
			
		}
		return false;
	}
}