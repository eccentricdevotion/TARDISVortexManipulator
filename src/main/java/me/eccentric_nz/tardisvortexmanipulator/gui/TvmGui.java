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
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.tardisvortexmanipulator.TardisVortexManipulatorPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TvmGui {

    private final TardisVortexManipulatorPlugin plugin;
    private final int tachyonLevel;
    private final ItemStack[] gui;

    public TvmGui(TardisVortexManipulatorPlugin plugin, int tachyonLevel) {
        this.plugin = plugin;
        this.tachyonLevel = tachyonLevel;
        gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // display
        ItemStack display = new ItemStack(Material.BOWL, 1);
        ItemMeta displayMeta = display.getItemMeta();
        assert displayMeta != null;
        displayMeta.setDisplayName("Display");
        displayMeta.setLore(Collections.singletonList(""));
        displayMeta.setCustomModelData(108);
        display.setItemMeta(displayMeta);
        // keypad pad
        // 1
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta none = one.getItemMeta();
        assert none != null;
        none.setDisplayName("1");
        none.setCustomModelData(118);
        one.setItemMeta(none);
        // 2 abc
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta abc = two.getItemMeta();
        assert abc != null;
        abc.setDisplayName("2");
        abc.setLore(Collections.singletonList("abc"));
        abc.setCustomModelData(126);
        two.setItemMeta(abc);
        // 3 def
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta def = three.getItemMeta();
        assert def != null;
        def.setDisplayName("3");
        def.setLore(Collections.singletonList("def"));
        def.setCustomModelData(125);
        three.setItemMeta(def);
        // 4 ghi
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta ghi = four.getItemMeta();
        assert ghi != null;
        ghi.setDisplayName("4");
        ghi.setLore(Collections.singletonList("ghi"));
        ghi.setCustomModelData(111);
        four.setItemMeta(ghi);
        // 5 jkl
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta jkl = five.getItemMeta();
        assert jkl != null;
        jkl.setDisplayName("5");
        jkl.setLore(Collections.singletonList("jkl"));
        jkl.setCustomModelData(110);
        five.setItemMeta(jkl);
        // 6 mno
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta mno = six.getItemMeta();
        assert mno != null;
        mno.setDisplayName("6");
        mno.setLore(Collections.singletonList("mno"));
        mno.setCustomModelData(123);
        six.setItemMeta(mno);
        // 7 pqrs
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta pqrs = seven.getItemMeta();
        assert pqrs != null;
        pqrs.setDisplayName("7");
        pqrs.setLore(Collections.singletonList("pqrs"));
        pqrs.setCustomModelData(122);
        seven.setItemMeta(pqrs);
        // 8 tuv
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta tuv = eight.getItemMeta();
        assert tuv != null;
        tuv.setDisplayName("8");
        tuv.setLore(Collections.singletonList("tuv"));
        tuv.setCustomModelData(109);
        eight.setItemMeta(tuv);
        // 9 wxyz
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta wxyz = nine.getItemMeta();
        assert wxyz != null;
        wxyz.setDisplayName("9");
        wxyz.setLore(Collections.singletonList("wxyz"));
        wxyz.setCustomModelData(117);
        nine.setItemMeta(wxyz);
        // 0
        ItemStack zero = new ItemStack(Material.BOWL, 1);
        ItemMeta nada = zero.getItemMeta();
        assert nada != null;
        nada.setDisplayName("0");
        nada.setCustomModelData(132);
        zero.setItemMeta(nada);
        // symbols -_*~
        ItemStack hash = new ItemStack(Material.BOWL, 1);
        ItemMeta symbols = hash.getItemMeta();
        assert symbols != null;
        symbols.setDisplayName("#");
        symbols.setLore(Collections.singletonList("~_-"));
        symbols.setCustomModelData(112);
        hash.setItemMeta(symbols);
        // space
        ItemStack star = new ItemStack(Material.BOWL, 1);
        ItemMeta space = star.getItemMeta();
        assert space != null;
        space.setDisplayName("*");
        space.setLore(Collections.singletonList("Space"));
        space.setCustomModelData(124);
        star.setItemMeta(space);
        // world
        ItemStack world = new ItemStack(Material.BOWL, 1);
        ItemMeta but = world.getItemMeta();
        assert but != null;
        but.setDisplayName("World");
        but.setCustomModelData(128);
        world.setItemMeta(but);
        // x
        ItemStack x = new ItemStack(Material.BOWL, 1);
        ItemMeta sel = x.getItemMeta();
        assert sel != null;
        sel.setDisplayName("X");
        sel.setCustomModelData(129);
        x.setItemMeta(sel);
        // y
        ItemStack y = new ItemStack(Material.BOWL, 1);
        ItemMeta hei = y.getItemMeta();
        assert hei != null;
        hei.setDisplayName("Y");
        hei.setCustomModelData(130);
        y.setItemMeta(hei);
        // z
        ItemStack z = new ItemStack(Material.BOWL, 1);
        ItemMeta coord = z.getItemMeta();
        assert coord != null;
        coord.setDisplayName("Z");
        coord.setCustomModelData(131);
        z.setItemMeta(coord);
        // tachyon level - TODO Show different levels depening on % full
        double percent = tachyonLevel / plugin.getConfig().getDouble("tachyon_use.max");
        short durability = (short) (1562 - (percent * 1562));
        ItemStack tachyon = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta tachyonMeta = tachyon.getItemMeta();
        assert tachyonMeta != null;
        tachyonMeta.setDisplayName("Tachyon Level");
        int level = (int) (percent * 100);
        List<String> lore = Collections.singletonList(level + "%");
        int customModelData = 105;
        if (level == 0) {
            customModelData = 95;
        } else if (level < 11) {
            customModelData = 96;
        } else if (level < 21) {
            customModelData = 97;
        } else if (level < 31) {
            customModelData = 98;
        } else if (level < 41) {
            customModelData = 99;
        } else if (level < 51) {
            customModelData = 100;
        } else if (level < 61) {
            customModelData = 101;
        } else if (level < 71) {
            customModelData = 102;
        } else if (level < 81) {
            customModelData = 103;
        } else if (level < 91) {
            customModelData = 104;
        }
        tachyonMeta.setCustomModelData(customModelData);
        tachyonMeta.setLore(lore);
        ((Damageable) tachyonMeta).setDamage(durability);
        tachyon.setItemMeta(tachyonMeta);
        // lifesigns
        ItemStack lifesigns = new ItemStack(Material.BOWL, 1);
        ItemMeta lifesignsMeta = lifesigns.getItemMeta();
        assert lifesignsMeta != null;
        lifesignsMeta.setDisplayName("Lifesigns");
        lifesignsMeta.setCustomModelData(113);
        lifesigns.setItemMeta(lifesignsMeta);
        // warp
        ItemStack warp = new ItemStack(Material.BOWL, 1);
        ItemMeta tol = warp.getItemMeta();
        assert tol != null;
        tol.setDisplayName("Enter Vortex / Save location / Check lifesigns");
        tol.setCustomModelData(127);
        warp.setItemMeta(tol);
        // beacon
        ItemStack beacon = new ItemStack(Material.BOWL, 1);
        ItemMeta beaconMeta = beacon.getItemMeta();
        assert beaconMeta != null;
        beaconMeta.setDisplayName("Beacon signal");
        beaconMeta.setCustomModelData(106);
        beacon.setItemMeta(beaconMeta);
        // message
        ItemStack message = new ItemStack(Material.BOWL, 1);
        ItemMeta messageMeta = message.getItemMeta();
        assert messageMeta != null;
        messageMeta.setDisplayName("Messages");
        messageMeta.setCustomModelData(115);
        message.setItemMeta(messageMeta);
        // save
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta curr = save.getItemMeta();
        assert curr != null;
        curr.setDisplayName("Save current location");
        curr.setCustomModelData(74);
        save.setItemMeta(curr);
        // load
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta disk = load.getItemMeta();
        assert disk != null;
        disk.setDisplayName("Load saved location");
        disk.setCustomModelData(114);
        load.setItemMeta(disk);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        assert win != null;
        win.setDisplayName("Close");
        win.setCustomModelData(1);
        close.setItemMeta(win);
        // next
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta cha = next.getItemMeta();
        assert cha != null;
        cha.setDisplayName("Next character");
        cha.setCustomModelData(116);
        next.setItemMeta(cha);
        // back
        ItemStack prev = new ItemStack(Material.BOWL, 1);
        ItemMeta let = prev.getItemMeta();
        assert let != null;
        let.setDisplayName("Previous character");
        let.setCustomModelData(120);
        prev.setItemMeta(let);

        return new ItemStack[]{null, null, null, null, display, null, null, null, null, tachyon, null, world, one, two, three, null, save, null, lifesigns, null, x, four, five, six, null, load, null, null, null, y, seven, eight, nine, null, message, null, null, null, z, star, zero, hash, null, beacon, null, close, null, null, prev, null, next, null, null, warp};
    }

    public ItemStack[] getGui() {
        return gui;
    }
}
