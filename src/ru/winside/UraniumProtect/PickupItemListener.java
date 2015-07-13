package ru.winside.UraniumProtect;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickupItemListener implements Listener {
	public static UraniumProtectMain plugin;
	int i = 0;

	@SuppressWarnings("deprecation")
	@EventHandler
	public void PickupItem(PlayerPickupItemEvent event) {
		Player p = event.getPlayer();
		if (UraniumProtectMain.blocked.contains(event.getItem().getItemStack().getTypeId())) {
			if (p.getEquipment().getHelmet() != null
					&& p.getEquipment().getChestplate() != null
					&& p.getEquipment().getLeggings() != null
					&& p.getEquipment().getBoots() != null
					&& p.getEquipment().getHelmet().getTypeId() == UraniumProtectMain.Helmet
					&& p.getEquipment().getChestplate().getTypeId() == UraniumProtectMain.Chestplate
					&& p.getEquipment().getLeggings().getTypeId() == UraniumProtectMain.Leggings
					&& p.getEquipment().getBoots().getTypeId() == UraniumProtectMain.Boots) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
				i++;

				if ((i % 40) == 0) {
					p.sendMessage(UraniumProtectMain.msg);
					World w = p.getWorld();
					if(UraniumProtectMain.Sound){
						w.playSound(p.getLocation(), Sound.CLICK, 100, 1);
					}
				}
			}
		}
	}
}