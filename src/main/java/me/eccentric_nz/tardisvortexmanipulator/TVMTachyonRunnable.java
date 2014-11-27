/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import java.util.HashMap;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetTachyon;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMTachyon;

/**
 *
 * @author eccentric_nz
 */
public class TVMTachyonRunnable implements Runnable {

    private final TARDISVortexManipulator plugin;
    private final int recharge;
    private final int max;
    private final TVMQueryFactory qf;

    public TVMTachyonRunnable(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
        this.recharge = this.plugin.getConfig().getInt("tachyon_use.recharge");
        this.max = this.plugin.getConfig().getInt("tachyon_use.max");
        this.qf = new TVMQueryFactory(this.plugin);
    }

    @Override
    public void run() {
        // get Vortex manipulators
        TVMResultSetTachyon rs = new TVMResultSetTachyon(plugin);
        if (rs.resultSet()) {
            for (TVMTachyon t : rs.getMaipulators()) {
                // player must be online to recharge
                if (plugin.getServer().getPlayer(t.getUuid()).isOnline()) {
                    // check their tachyon level
                    if (t.getLevel() + recharge <= max) {
                        // recharge them if they are not full
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("tachyon_level", t.getLevel() + recharge);
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("uuid", t.getUuid().toString());
                        qf.doUpdate("manipulator", set, where);
                    }
                }
            }
        }
    }
}
