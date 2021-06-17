/*
 * Copyright (C) 2021 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.tardisvortexmanipulator.database.TvmQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TvmResultSetTachyon;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TvmTachyonRunnable implements Runnable {

    private final TardisVortexManipulatorPlugin plugin;
    private final int recharge;
    private final int max;
    private final TvmQueryFactory queryFactory;

    public TvmTachyonRunnable(TardisVortexManipulatorPlugin plugin) {
        this.plugin = plugin;
        recharge = this.plugin.getConfig().getInt("tachyon_use.recharge");
        max = this.plugin.getConfig().getInt("tachyon_use.max");
        queryFactory = new TvmQueryFactory(this.plugin);
    }

    @Override
    public void run() {
        // get Vortex manipulators
        TvmResultSetTachyon resultSetTachyon = new TvmResultSetTachyon(plugin);
        if (resultSetTachyon.resultSet()) {
            resultSetTachyon.getManipulators().forEach((tvmTachyon) -> {
                // player must be online to recharge
                Player player = plugin.getServer().getPlayer(tvmTachyon.getUuid());
                if (player != null && player.isOnline()) {
                    // check their tachyon level
                    if (tvmTachyon.getLevel() + recharge <= max) {
                        // recharge them if they are not full
                        queryFactory.alterTachyons(tvmTachyon.getUuid().toString(), recharge);
                    } else {
                        // catch slightly off levels ie 98%
                        queryFactory.alterTachyons(tvmTachyon.getUuid().toString(), max - tvmTachyon.getLevel());
                    }
                }
            });
        }
    }
}
