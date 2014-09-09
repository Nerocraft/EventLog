package main.nerocraft.eventlog;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Util {
    /**
     * Gets whether or not this player is an NPC
     *
     * @param player the player
     * @return true if player is an NPC, otherwise false
     */
    public static boolean isNPC(Player player) {
        return player.hasMetadata("NPC");
    }

    public static void logDamage(Player victim, double damageDealt, String damager, boolean isNPC, String shooter, Material item) {
        double damage = Math.round(damageDealt * 100.0D) / 100.0D;
        double health = Math.round(victim.getHealth() * 100.0D) / 100.0D;
        String finalString = victim.getName() + " took " + Double.toString(damage) + " damage from "
                + damager + (isNPC ? " (NPC)" : "") + (item != null ? " holding " + item.toString() : "")
                + (shooter != null ? " (" + shooter + ")" : "") + " (" + Double.toString(health) + "/"
                + Double.toString(victim.getMaxHealth()) + " Health)";
        Bukkit.getLogger().log(Level.INFO, finalString);
    }
}
