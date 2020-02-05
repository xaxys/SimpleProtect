package com.github.xaxys.simpleprotect;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xaxys.simpleprotect.listener.AntiBreakFarm;
import com.github.xaxys.simpleprotect.listener.AntiExplosion;
import com.github.xaxys.simpleprotect.manager.EntityManager;

public class Main extends JavaPlugin {
	public boolean DEBUG = false;
	public static Main PLUGIN;
	private boolean enableKeepFarm = false;
	private boolean enableSpawnMob = false;
	private boolean enableAntiExplosion = false;
	
	public void onLoad() {
		getLogger().info("§d§lSimpleProtect §9§l插件已加载！§6作者：xa");
		saveDefaultConfig();
		PLUGIN = this;
	}

	public void onEnable() {
		getLogger().info("§d§lSimpleProtect §9§l插件已启用！§6作者：xa");
		readConfig();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void readConfig() {
		if (getConfig().getBoolean("KeepFarm.enable", true)) {
			getLogger().info("§9§l防止耕地破坏已启用");
			AntiBreakFarm.ExclusiveMode = getConfig().getBoolean("KeepFarm.ExclusiveMode");
			AntiBreakFarm.List  = (List<String>) getConfig().getList("KeepFarm.world");
			if (!enableKeepFarm) {
				registerKeepFarm();
				enableKeepFarm = true;
			}
		} else if (enableKeepFarm) {
			unregisterKeepFarm();
			enableKeepFarm = false;
		}
		if (getConfig().getBoolean("SpawnMob.enable", true)) {
			getLogger().info("§9§l防止密集刷怪已启用");
			if (!enableSpawnMob) {
				registerSpawnMob();
				enableSpawnMob = true;
			}
		} else if (enableSpawnMob) {
			unregisterSpawnMob();
			enableSpawnMob = false;
		}
		if (getConfig().getBoolean("AntiExplosion.enable", true)) {
			getLogger().info("§9§l防止爆炸毁图已启用");
			AntiExplosion.ExclusiveMode = getConfig().getBoolean("AntiExplosion.ExclusiveMode");
			AntiExplosion.List  = (List<String>) getConfig().getList("AntiExplosion.world");
			if (!enableAntiExplosion) {
				registerNoExplosionBlockDamage();
				enableAntiExplosion = true;
			}
		} else if (enableAntiExplosion) {
			unregisterNoExplosionBlockDamage();
			enableAntiExplosion = false;
		}
		List<Integer> list = (List<Integer>) getConfig().getList("PurgeExclusion");
		for (Integer i : list) {
			EntityType type = EntityType.fromId(i);
			if (type != null) EntityManager.ExclusiveEntityList.add(type);
		}
		getLogger().info("§9§l实体排除列表已加载");
		getCommand("protect").setExecutor(new Commands(this));
	}

	private void registerKeepFarm() {
		getLogger().info("§9§l正在注册耕地破坏事件监听器...");
		Bukkit.getPluginManager().registerEvents(AntiBreakFarm.getListener(), this);
	}

	private void registerSpawnMob() {
		getLogger().info("§9§l正在注册生物生成事件监听器...");
		int value = getConfig().getInt("SpawnMob.value", 16);
		EntityManager manager = EntityManager.getManager();
		Bukkit.getPluginManager().registerEvents(manager.getEvents(value), this);
	}

	private void registerNoExplosionBlockDamage() {
		getLogger().info("§9§l正在注册爆炸事件监听器...");
		Bukkit.getPluginManager().registerEvents(AntiExplosion.getListener(), this);
	}

	private void unregisterKeepFarm() {
		getLogger().info("§9§l正在卸载耕地破坏事件监听器...");
		PlayerInteractEvent.getHandlerList().unregister(this);
		EntityInteractEvent.getHandlerList().unregister(this);
	}

	private void unregisterSpawnMob() {
		getLogger().info("§9§l正在卸载生物生成事件监听器...");
		CreatureSpawnEvent.getHandlerList().unregister(this);
	}

	private void unregisterNoExplosionBlockDamage() {
		getLogger().info("§9§l正在卸载爆炸事件监听器...");
		EntityExplodeEvent.getHandlerList().unregister(this);
	}

	public void onDisable() {
		getLogger().info("§d§l*SimpleProtect* §9§l插件已卸载！");
		unregisterKeepFarm();
		unregisterSpawnMob();
		unregisterNoExplosionBlockDamage();
	}
	
	
}