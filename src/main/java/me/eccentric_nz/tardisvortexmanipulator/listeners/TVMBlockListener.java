package me.eccentric_nz.tardisvortexmanipulator.listeners;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TVMBlockListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInteract(BlockBreakEvent event) {
        if (TARDISVortexManipulator.getBlocks().contains(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}
