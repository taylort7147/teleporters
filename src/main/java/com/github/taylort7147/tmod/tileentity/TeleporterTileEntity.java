package com.github.taylort7147.tmod.tileentity;

import com.github.taylort7147.tmod.init.ModTileEntityTypes;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TeleporterTileEntity extends TileEntity
{
	public static final String NAME = "teleporter";
	
	public TeleporterTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	

	public TeleporterTileEntity() {
		this(ModTileEntityTypes.TELEPORTER);
	}
}
