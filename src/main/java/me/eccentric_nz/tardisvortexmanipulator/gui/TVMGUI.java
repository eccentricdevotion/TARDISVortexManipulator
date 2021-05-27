/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMGUI {

	private final TARDISVortexManipulator plugin;
	private final int tachyonLevel;
	private final ItemStack[] gui;

	public TVMGUI(TARDISVortexManipulator plugin, int tachyonLevel) {
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
		ItemStack dis = new ItemStack(Material.BOWL, 1);
		ItemMeta play = dis.getItemMeta();
		assert play != null;
		play.setDisplayName("Display");
		play.setLore(Collections.singletonList(""));
		play.setCustomModelData(108);
		dis.setItemMeta(play);
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
		// tachyon level - TODO show different levels depening on % full
		double percent = tachyonLevel / plugin.getConfig().getDouble("tachyon_use.max");
		short durability = (short) (1562 - (percent * 1562));
		ItemStack tach = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		ItemMeta yon = tach.getItemMeta();
		assert yon != null;
		yon.setDisplayName("Tachyon Level");
		int level = (int) (percent * 100);
		List<String> lore = Collections.singletonList(level + "%");
		int cmd = 105;
		if (level == 0) {
			cmd = 95;
		} else if (level < 11) {
			cmd = 96;
		} else if (level < 21) {
			cmd = 97;
		} else if (level < 31) {
			cmd = 98;
		} else if (level < 41) {
			cmd = 99;
		} else if (level < 51) {
			cmd = 100;
		} else if (level < 61) {
			cmd = 101;
		} else if (level < 71) {
			cmd = 102;
		} else if (level < 81) {
			cmd = 103;
		} else if (level < 91) {
			cmd = 104;
		}
		yon.setCustomModelData(cmd);
		yon.setLore(lore);
		tach.setItemMeta(yon);
		tach.setDurability(durability);
		// lifesigns
		ItemStack life = new ItemStack(Material.BOWL, 1);
		ItemMeta signs = life.getItemMeta();
		assert signs != null;
		signs.setDisplayName("Lifesigns");
		signs.setCustomModelData(113);
		life.setItemMeta(signs);
		// warp
		ItemStack warp = new ItemStack(Material.BOWL, 1);
		ItemMeta tol = warp.getItemMeta();
		assert tol != null;
		tol.setDisplayName("Enter Vortex / Save location / Check lifesigns");
		tol.setCustomModelData(127);
		warp.setItemMeta(tol);
		// beacon
		ItemStack bea = new ItemStack(Material.BOWL, 1);
		ItemMeta con = bea.getItemMeta();
		assert con != null;
		con.setDisplayName("Beacon signal");
		con.setCustomModelData(106);
		bea.setItemMeta(con);
		// message
		ItemStack mess = new ItemStack(Material.BOWL, 1);
		ItemMeta age = mess.getItemMeta();
		assert age != null;
		age.setDisplayName("Messages");
		age.setCustomModelData(115);
		mess.setItemMeta(age);
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

		return new ItemStack[]{null, null, null, null, dis, null, null, null, null, tach, null, world, one, two, three, null, save, null, life, null, x, four, five, six, null, load, null, null, null, y, seven, eight, nine, null, mess, null, null, null, z, star, zero, hash, null, bea, null, close, null, null, prev, null, next, null, null, warp};
	}

	public ItemStack[] getGUI() {
		return gui;
	}
}
