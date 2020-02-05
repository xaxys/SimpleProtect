package com.github.xaxys.simpleprotect.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiBreakFarm implements Listener {
	private static final AntiBreakFarm LISTENER = new AntiBreakFarm();
	public static List<String> List = new ArrayList<String>();
	public static boolean ExclusiveMode = true;

	public static AntiBreakFarm getListener() {
		return LISTENER;
	}

	@EventHandler
	public void soilChangePlayer(PlayerInteractEvent event) {
		if ((ExclusiveMode ^ List.contains(event.getPlayer().getWorld().getName())
				&& (event.getAction().equals(Action.PHYSICAL))
				&& (event.getClickedBlock().getType().equals(Material.SOIL)))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void soilChangeEntity(EntityInteractEvent event) {
		if ((ExclusiveMode ^ List.contains(event.getEntity().getWorld().getName())
				&& (event.getEntityType() != EntityType.PLAYER)
				&& (event.getBlock().getType().equals(Material.SOIL)))) {
			event.setCancelled(true);
		}
	}
}
