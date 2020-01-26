package com.github.taylort7147.teleporters;

import java.util.UUID;

import com.github.taylort7147.teleporters.block.BlockTeleporter;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class Link implements INBTSerializable<CompoundNBT>
{
    private UUID owner;
    private Endpoint left;
    private Endpoint right;

    private Link()
    {
        this.owner = null;
        this.left = null;
        this.right = null;
    }

    public static Link fromNbt(CompoundNBT nbt)
    {
        Link link = new Link();
        link.deserializeNBT(nbt);
        return link;
    }

    public Link(UUID owner, Endpoint left, Endpoint right)
    {
        this.owner = owner;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString()
    {
        String leftString = (left == null) ? "null" : left.toString();
        String rightString = (right == null) ? "null" : right.toString();
        return leftString + " <==> " + rightString;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Link))
            return false;

        Link other = (Link) obj;
        return (left.equals(other.left) && right.equals(other.right))
                || (left.equals(other.right) && right.equals(other.left));
    }

    public UUID getOwner()
    {
        return owner;
    }

    public Endpoint getLeft()
    {
        return left;
    }

    public Endpoint getRight()
    {
        return right;
    }

    public boolean isValid()
    {
        return left != null && right != null && !left.equals(right) && left.getDimension() == right.getDimension();
    }

    public boolean isValidLinkInWorld(World world)
    {
        if (!isValid())
            return false;

        final Block leftBlock = world.getBlockState(left.getPos()).getBlock();
        final Block rightBlock = world.getBlockState(right.getPos()).getBlock();
        final int dimension = world.getDimension().getType().getId();

        return (left.getDimension() == dimension) && (right.getDimension() == dimension)
                && (leftBlock instanceof BlockTeleporter) && (rightBlock instanceof BlockTeleporter);
    }

    public boolean contains(Endpoint endpoint)
    {
        return (left != null && left.equals(endpoint)) || (right != null && right.equals(endpoint));
    }

    public Endpoint getOther(Endpoint endpoint)
    {
        if (left.equals(endpoint))
        {
            return right;
        } else if (right.equals(endpoint))
        {
            return left;
        } else
        {
            throw new IndexOutOfBoundsException("The link does not contain the object " + endpoint.toString());
        }
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("Owner", this.owner);
        nbt.put("Left", left.serializeNBT());
        nbt.put("Right", right.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        this.owner = nbt.getUniqueId("Owner");
        this.left = Endpoint.fromNbt(nbt.getCompound("Left"));
        this.right = Endpoint.fromNbt(nbt.getCompound("Right"));
    }
}
