package com.github.xaxys.simpleprotect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.xaxys.simpleprotect.common.util.OptionParser;
import com.github.xaxys.simpleprotect.manager.EntityManager;

public class Commands implements CommandExecutor {
	private final Main plugin;
	public Commands(Main protect) {
		this.plugin = protect;
	}

	private String[] getPluginInfo() {
		String[] strings = { ChatColor.DARK_AQUA + "/protect entity [world STR] 查看实体信息",
				ChatColor.DARK_AQUA + "/protect entity purge <TYPE|all> [world STR] [rate INT] 清理实体",
				ChatColor.DARK_AQUA + "/protect chunk 查看区块信息", ChatColor.DARK_AQUA + "/protect chunk unload 卸载空闲区块",
				};
		return strings;
	}

	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if ((sender instanceof Player) && !((Player)sender).hasPermission("simpleprotect.admin")) {
			sender.sendMessage(ChatColor.RED + "你没有权限使用 SimpleProtect");
			return true;
		}
		
		if (args.length < 1) {
			sender.sendMessage(getPluginInfo());
		} else if (args[0].equals("entity")) {
			OptionParser parser = new OptionParser();
			parser.addFilter("purge", OptionParser.FilterMode.WITH_ARGUMENT);
			parser.addFilter("world", OptionParser.FilterMode.WITH_ARGUMENT);
			parser.addFilter("rate", OptionParser.FilterMode.WITH_ARGUMENT);
			OptionParser.ParsedOption option = parser.parse(Arrays.copyOfRange(args, 1, args.length));
			if (option.getSingleList().size() > 0) {
				sender.sendMessage(ChatColor.RED + "不正确的参数");
			} else if (option.has("purge")) {
				int rate = 16;
				if ((option.has("rate")) && (option.isInteger("rate"))) {
					rate = option.getInteger("rate");
				} else if (option.has("rate")) {
					rate = -1;
				}
				if (rate < -1) {
					sender.sendMessage(ChatColor.RED + "不正确的限制值");
				} else if ((option.has("world")) && (Bukkit.getWorld(option.getString("world")) != null)) {
					sender.sendMessage(
							purgeEntity(Bukkit.getWorld(option.getString("world")), option.getString("purge"), rate));
				} else if (option.has("world")) {
					sender.sendMessage(ChatColor.RED + "错误的世界名");
				} else {
					sender.sendMessage(purgeEntity(option.getString("purge"), rate));
				}
			} else if ((option.has("world")) && (Bukkit.getWorld(option.getString("world")) != null)) {
				sender.sendMessage(getEntityInfo(this.plugin.getServer().getWorld(option.getString("world"))));
			} else if (option.has("world")) {
				sender.sendMessage(ChatColor.RED + "错误的世界名");
			} else {
				sender.sendMessage(getEntityInfo());
			}
		} else if (args[0].equals("chunk")) {
			if (args.length < 2) {
				sender.sendMessage(getChunkInfo());
			} else if (args.length < 3) {
				if (args[1].equals("unload")) {
					sender.sendMessage(unloadChunk());
				}
			} else {
				sender.sendMessage(getPluginInfo());
			}
		} else if (args[0].equals("reload")) {
			Main.PLUGIN.reloadConfig();
			Main.PLUGIN.readConfig();
		} else if (args[0].equals("debug")) {
			Main.PLUGIN.DEBUG = !Main.PLUGIN.DEBUG;
			sender.sendMessage(String.format("DEBUG: %b", Main.PLUGIN.DEBUG));
		} else {
			sender.sendMessage(getPluginInfo());
		}
		return true;
	}

	private String[] getEntityInfo() {
		List<World> worlds = this.plugin.getServer().getWorlds();
		return getEntityInfo(EntityManager.getManager().getEntityInfo(worlds));
	}

	private String[] getEntityInfo(World world) {
		return getEntityInfo(EntityManager.getManager().getEntityInfo(world));
	}

	private String[] getEntityInfo(Map<String, Integer> map) {
		List<String> list = new ArrayList<String>();
		int count = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			list.add(ChatColor.GOLD + (String) entry.getKey() + ": " + entry.getValue());
			count += ((Integer) entry.getValue()).intValue();
		}
		list.add(ChatColor.RED + "TATOL: " + count);
		return list.toArray(new String[0]);
	}

	private String purgeEntity(String type, int rate) {
		List<World> worlds = this.plugin.getServer().getWorlds();
		int count = EntityManager.getManager().purgeEntity(worlds, type.toUpperCase(), rate);
		return ChatColor.GOLD + "Purge done: " + count;
	}

	private String purgeEntity(World world, String type, int rate) {
		int count = EntityManager.getManager().purgeEntity(world, type.toUpperCase(), rate);
		return ChatColor.GOLD + "Purge done: " + count;
	}

	private String[] getChunkInfo() {
		List<String> messages = new ArrayList<String>();
		int total = 0;
		for (World world : Bukkit.getWorlds()) {
			int size = world.getLoadedChunks().length;
			total += size;
			messages.add(ChatColor.GOLD + world.getName() + ": " + size);
		}
		messages.add(ChatColor.GOLD + "Total: " + total);
		int size = messages.size();
		return messages.toArray(new String[size]);
	}

	private String unloadChunk() {
		int i = 0;
		int j = 0;
		for (World world : Bukkit.getWorlds()) {
			Chunk[] chunks = world.getLoadedChunks();
			i += chunks.length;
			for (Chunk chunk : chunks) {
				chunk.unload();
			}
			j += world.getLoadedChunks().length;
		}
		i -= j;
		return new String(ChatColor.GOLD + "Purge chunk number: " + i);
	}
}