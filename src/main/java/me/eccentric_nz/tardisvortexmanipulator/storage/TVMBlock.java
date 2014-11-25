/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.storage;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TVMBlock {

    private Block block;
    private Material type;
    private byte data;

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Material getType() {
        return type;
    }

    public void setType(Material type) {
        this.type = type;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }
}
