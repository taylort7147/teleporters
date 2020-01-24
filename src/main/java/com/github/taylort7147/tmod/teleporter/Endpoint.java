package com.github.taylort7147.tmod.teleporter;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class Endpoint implements INBTSerializable<CompoundNBT>
{
    private BlockPos pos;
    private int dimension;

    public Endpoint(BlockPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public int getDimension()
    {
        return dimension;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Endpoint))
            return false;
        Endpoint other = (Endpoint) obj;
        return (pos.equals(other.pos)) && (dimension == other.dimension);

    }

    @Override
    public String toString()
    {
        return "{dimension=" + dimension + ", pos=(" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + ")}";
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("dim", dimension);
        nbt.putInt("X", pos.getX());
        nbt.putInt("Y", pos.getY());
        nbt.putInt("Z", pos.getZ());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        this.dimension = nbt.getInt("dim");
        int x = nbt.getInt("X");
        int y = nbt.getInt("Y");
        int z = nbt.getInt("Z");
        this.pos = new BlockPos(x, y, z);
    }
}
