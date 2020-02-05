package com.github.xaxys.simpleprotect.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class AntiExplosion implements Listener {
	private static final AntiExplosion LISTENER = new AntiExplosion();
	public static List<String> List = new ArrayList<String>();
	public static boolean ExclusiveMode = true;
	
	public static AntiExplosion getListener() {
		return LISTENER;
	}

	@EventHandler
	public void onEntityExplosion(EntityExplodeEvent event) {
		if (ExclusiveMode ^ List.contains(event.getLocation().getWorld().getName())) {
			event.blockList().clear();
		}
	}
	
	@EventHandler
	public void onBlockExplosion(BlockExplodeEvent event) {
		if (ExclusiveMode ^ List.contains(event.getBlock().getWorld().getName())) {
			event.blockList().clear();
		}
	}
}
