// Copyright © Oren Iouchavaev (Neroren) - Nerocraft
package main.nerocraft.eventlog;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventListener implements Listener {
    Util util = new Util();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            EntityType damagerType = event.getDamager().getType();
            String damager = damagerType.toString();
            double damage = event.getDamage();
            if (util.isNPC(victim)) return;
            if (damagerType == EntityType.PLAYER) {
                if (util.isNPC((Player) event.getDamager())) return;
                damager = ((Player) event.getDamager()).getName();
            }
            Bukkit.getLogger().log(Level.INFO, victim.getName() + " was damaged by " + damager + " for " + Double.toString(damage) + " health (" + victim.getHealth() + "/" + victim.getMaxHealth() + " Health)");
        }
    }
}
