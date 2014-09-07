// Copyright © Oren Iouchavaev (Neroren) - Nerocraft
package main.nerocraft.eventlog;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;

public class EventListener implements Listener {
    Util util = new Util();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            EntityType damagerType = event.getDamager().getType();
            String damager = damagerType.toString();
            String shooter = null;
            boolean isNPC = false;
            double damage = event.getFinalDamage();

            if (util.isNPC(victim)) return;
            if (damagerType == EntityType.PLAYER) {
                if (util.isNPC((Player) event.getDamager())) isNPC = true;
                damager = ((Player) event.getDamager()).getName();
            }
            if (event.getCause() == DamageCause.PROJECTILE) {
                ProjectileSource source = ((Projectile) event.getDamager()).getShooter();
                if (source instanceof LivingEntity) {
                    if (source instanceof Player) {
                        Player player = (Player) source;
                        shooter = player.getName();
                    }
                }
            }

            printString(victim, damage, damager, isNPC, shooter);
        }
    }

    public void printString(Player victim, double damage, String damager, boolean isNPC, String shooter) {
        String finalString = victim.getName() + " took " + Double.toString(damage) + " damage from "
                + damager + (isNPC ? " (NPC)" : "") + (shooter != null ? " (" + shooter + ")" : "") + " (" + Double.toString(victim.getHealth()) + "/"
                + Double.toString(victim.getMaxHealth()) + " Health)";
        Bukkit.getLogger().log(Level.INFO, finalString);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player) {
            switch (event.getCause()) {
            case ENTITY_ATTACK:
            case ENTITY_EXPLOSION:
            case PROJECTILE:
            case MAGIC:
            case FALLING_BLOCK:
            case LIGHTNING:
                return;
            default:
                break;
            }

            printString((Player) event.getEntity(), event.getFinalDamage(), event.getCause().toString(), false, null);
        }
    }
}
