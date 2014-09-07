package main.nerocraft.eventlog;

import org.bukkit.entity.Player;

public class Util {
    /**
     * Gets whether or not this player is an NPC
     *
     * @param player the player
     * @return true if player is an NPC, otherwise false
     */
    public boolean isNPC(Player player) {
        return player.hasMetadata("NPC");
    }
}
