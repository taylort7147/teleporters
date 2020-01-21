package com.github.taylort7147.tmod.item;

import com.github.taylort7147.tmod.TMod;

import net.minecraft.item.Item;

public class ItemTeleporterLinkEstablisher extends Item
{
    public static final String NAME = "teleporter_link_establisher";
    public static final String ITEM_NAME = NAME + "_item";
    public static final String TEXTURE_NAME = TMod.MODID + ":" + ITEM_NAME;

    public ItemTeleporterLinkEstablisher(Properties properties)
    {
        super(properties);
    }

}
