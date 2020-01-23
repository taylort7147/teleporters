package com.github.taylort7147.tmod.teleporter;

import net.minecraft.util.math.BlockPos;

public class Endpoint
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
}
