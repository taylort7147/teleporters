package com.github.taylort7147.teleporters.item;

import com.github.taylort7147.teleporters.Teleporters;

import net.minecraft.item.Item;

public class ItemPurpurPlate extends Item
{
    public static final String NAME = "purpur_plate";
    public static final String ITEM_NAME = NAME + "_item";
    public static final String TEXTURE_NAME = Teleporters.MODID + ":" + ITEM_NAME;

    public ItemPurpurPlate(Properties properties)
    {
        super(properties);
    }
}
