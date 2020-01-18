package com.github.taylort7147.tmod.item;

import com.github.taylort7147.tmod.TMod;
import net.minecraft.item.Item;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTeleporterLinkEstablisher extends Item
{
    public static final String NAME = "teleporter_link_establisher";
	public static final String ITEM_NAME = NAME + "_item";
	public static final String TEXTURE_NAME = TMod.MODID + ":" + ITEM_NAME;

	public ItemTeleporterLinkEstablisher(Properties properties)
	{
		super(properties);
	}
	
//    @SideOnly(Side.CLIENT)
//    @Override
//    public boolean isFull3D()
//    {
//        return true;
//    }
}
