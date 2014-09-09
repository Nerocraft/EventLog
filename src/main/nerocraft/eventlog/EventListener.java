// Copyright © Oren Iouchavaev (Neroren) - Nerocraft
package main.nerocraft.eventlog;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EventListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            EntityType damagerType = event.getDamager().getType();
            String damager = damagerType.toString();
            String shooter = null;
            boolean isNPC = false;
            Material item = null;

            if (event.getDamager() instanceof LivingEntity) {
                item = ((LivingEntity) event.getDamager()).getEquipment().getItemInHand().getType();
                if (item == Material.AIR) {
                    item = null;
                }
            }

            if (Util.isNPC(victim)) return;
            if (damagerType == EntityType.PLAYER) {
                if (Util.isNPC((Player) event.getDamager())) isNPC = true;
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

            logDamage(victim, event.getFinalDamage(), damager, isNPC, shooter, item);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {
        String name = event.getPlayer().getName();
        Location loc = event.getPlayer().getLocation();
        double x = Math.round(loc.getX() * 10.0D) / 10.0D;
        double y = Math.round(loc.getY() * 10.0D) / 10.0D;
        double z = Math.round(loc.getZ() * 10.0D) / 10.0D;

        if (event.isCancelled()) {
            Bukkit.getLogger().log(Level.INFO, name + (event.getPlayer().isInfected() ? " (Infected)" : "") + " denied access to " + event.getInventory().getType().toString() + " (" + Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z) + ")");
        } else {
            Bukkit.getLogger().log(Level.INFO, name + " interacted with " + event.getInventory().getType().toString() + " (" + Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z) + ")");
        }
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

            logDamage((Player) event.getEntity(), event.getFinalDamage(), event.getCause().toString(), false, null, null);
        }
    }

    public void logDamage(Player victim, double damageDealt, String damager, boolean isNPC, String shooter, Material item) {
        double damage = Math.round(damageDealt * 100.0D) / 100.0D;
        double health = Math.round(victim.getHealth() * 100.0D) / 100.0D;
        String finalString = victim.getName() + " took " + Double.toString(damage) + " damage from "
                + damager + (isNPC ? " (NPC)" : "") + (item != null ? " holding " + item.toString() : "")
                + (shooter != null ? " (" + shooter + ")" : "") + " (" + Double.toString(health) + "/"
                + Double.toString(victim.getMaxHealth()) + " Health)";
        Bukkit.getLogger().log(Level.INFO, finalString);
    }
}
