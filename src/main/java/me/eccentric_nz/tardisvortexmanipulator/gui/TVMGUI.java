/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TVMGUI {

    private final TARDISVortexManipulator plugin;
    private final String uuid;
    private final ItemStack[] gui;

    public TVMGUI(TARDISVortexManipulator plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // display
        ItemStack dis = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta play = dis.getItemMeta();
        play.setDisplayName("Display");
        play.setLore(Arrays.asList(""));
        dis.setItemMeta(play);
        // keypad pad
        // 1
        ItemStack one = new ItemStack(Material.BOWL, 1);
        ItemMeta none = one.getItemMeta();
        none.setDisplayName("1");
        one.setItemMeta(none);
        // 2 abc
        ItemStack two = new ItemStack(Material.BOWL, 1);
        ItemMeta abc = two.getItemMeta();
        abc.setDisplayName("2");
        abc.setLore(Arrays.asList("abc"));
        two.setItemMeta(abc);
        // 3 def
        ItemStack three = new ItemStack(Material.BOWL, 1);
        ItemMeta def = three.getItemMeta();
        def.setDisplayName("3");
        def.setLore(Arrays.asList("def"));
        three.setItemMeta(def);
        // 4 ghi
        ItemStack four = new ItemStack(Material.BOWL, 1);
        ItemMeta ghi = four.getItemMeta();
        ghi.setDisplayName("4");
        ghi.setLore(Arrays.asList("ghi"));
        four.setItemMeta(ghi);
        // 5 jkl
        ItemStack five = new ItemStack(Material.BOWL, 1);
        ItemMeta jkl = five.getItemMeta();
        jkl.setDisplayName("5");
        jkl.setLore(Arrays.asList("jkl"));
        five.setItemMeta(jkl);
        // 6 mno
        ItemStack six = new ItemStack(Material.BOWL, 1);
        ItemMeta mno = six.getItemMeta();
        mno.setDisplayName("6");
        mno.setLore(Arrays.asList("mno"));
        six.setItemMeta(mno);
        // 7 pqrs
        ItemStack seven = new ItemStack(Material.BOWL, 1);
        ItemMeta pqrs = seven.getItemMeta();
        pqrs.setDisplayName("7");
        pqrs.setLore(Arrays.asList("pqrs"));
        seven.setItemMeta(pqrs);
        // 8 tuv
        ItemStack eight = new ItemStack(Material.BOWL, 1);
        ItemMeta tuv = eight.getItemMeta();
        tuv.setDisplayName("8");
        tuv.setLore(Arrays.asList("tuv"));
        eight.setItemMeta(tuv);
        // 9 wxyz
        ItemStack nine = new ItemStack(Material.BOWL, 1);
        ItemMeta wxyz = nine.getItemMeta();
        wxyz.setDisplayName("9");
        wxyz.setLore(Arrays.asList("wxyz"));
        nine.setItemMeta(wxyz);
        // 0
        ItemStack zero = new ItemStack(Material.BOWL, 1);
        ItemMeta nada = zero.getItemMeta();
        nada.setDisplayName("0");
        zero.setItemMeta(nada);
        // symbols -_*~
        ItemStack hash = new ItemStack(Material.BOWL, 1);
        ItemMeta symbols = hash.getItemMeta();
        symbols.setDisplayName("#");
        symbols.setLore(Arrays.asList("~_-"));
        hash.setItemMeta(symbols);
        // space
        ItemStack star = new ItemStack(Material.BOWL, 1);
        ItemMeta space = star.getItemMeta();
        space.setDisplayName("*");
        space.setLore(Arrays.asList("Space"));
        star.setItemMeta(space);
        // world
        ItemStack world = new ItemStack(Material.BOWL, 1);
        ItemMeta but = world.getItemMeta();
        but.setDisplayName("World");
        world.setItemMeta(but);
        // x
        ItemStack x = new ItemStack(Material.BOWL, 1);
        ItemMeta sel = x.getItemMeta();
        sel.setDisplayName("X");
        x.setItemMeta(sel);
        // y
        ItemStack y = new ItemStack(Material.BOWL, 1);
        ItemMeta hei = y.getItemMeta();
        hei.setDisplayName("Y");
        y.setItemMeta(hei);
        // z
        ItemStack z = new ItemStack(Material.BOWL, 1);
        ItemMeta coord = z.getItemMeta();
        coord.setDisplayName("Z");
        z.setItemMeta(coord);
        // tachyon level - TODO show different levels depening on % full
        TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid);
        short durability = 1562;
        int percent = rs.getTachyonLevel() / plugin.getConfig().getInt("tachyon_use.max");
        if (rs.resultSet()) {
            durability = (short) (1562 - (percent * 1562));
        }
        ItemStack tach = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta yon = tach.getItemMeta();
        yon.setDisplayName("Tachyon Level");
        List<String> lore = Arrays.asList(percent + "%");
        yon.setLore(lore);
        tach.setItemMeta(yon);
        tach.setDurability(durability);
        // warp
        ItemStack warp = new ItemStack(Material.BOWL, 1);
        ItemMeta tol = warp.getItemMeta();
        tol.setDisplayName("Enter Vortex / Save location");
        warp.setItemMeta(tol);
        // beacon
        ItemStack bea = new ItemStack(Material.BOWL, 1);
        ItemMeta con = bea.getItemMeta();
        con.setDisplayName("Beacon signal");
        bea.setItemMeta(con);
        // message
        ItemStack mess = new ItemStack(Material.BOWL, 1);
        ItemMeta age = mess.getItemMeta();
        age.setDisplayName("Messages");
        mess.setItemMeta(age);
        // save
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta curr = save.getItemMeta();
        curr.setDisplayName("Save current location");
        save.setItemMeta(curr);
        // load
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta disk = load.getItemMeta();
        disk.setDisplayName("Load saved location");
        load.setItemMeta(disk);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName("Close");
        close.setItemMeta(win);
        // next
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta cha = next.getItemMeta();
        cha.setDisplayName("Next character");
        next.setItemMeta(cha);
        // back
        ItemStack prev = new ItemStack(Material.BOWL, 1);
        ItemMeta let = prev.getItemMeta();
        let.setDisplayName("Previous character");
        prev.setItemMeta(let);

        ItemStack[] is = {
            null, null, null, null, dis, null, null, null, null,
            tach, null, world, one, two, three, null, save, null,
            null, null, x, four, five, six, null, load, null,
            null, null, y, seven, eight, nine, null, mess, null,
            null, null, z, star, zero, hash, null, bea, null,
            close, null, null, prev, null, next, null, null, warp
        };
        return is;
    }

    public ItemStack[] getGUI() {
        return gui;
    }
}
