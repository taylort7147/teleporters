package com.github.taylort7147.tmod.teleporter.capability;

import java.util.List;
import java.util.concurrent.Callable;

import com.github.taylort7147.tmod.TMod;
import com.github.taylort7147.tmod.teleporter.Endpoint;
import com.github.taylort7147.tmod.teleporter.Link;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityTeleporterData
{
    public static class Storage implements Capability.IStorage<ITeleporterData>
    {
        @Override
        public INBT writeNBT(Capability<ITeleporterData> capability, ITeleporterData instance, Direction side)
        {
            ListNBT nbt = new ListNBT();
            instance.getLinks().forEach(link -> nbt.add(link.serializeNBT()));
            return nbt;
        }

        @Override
        public void readNBT(Capability<ITeleporterData> capability, ITeleporterData instance, Direction side, INBT nbt)
        {
            if(nbt instanceof ListNBT)
            {
                ListNBT listNbt = (ListNBT) nbt;
                listNbt.forEach(linkNbt -> {
                    if(linkNbt instanceof CompoundNBT)
                    {
                        CompoundNBT linkCompoundNbt = (CompoundNBT) linkNbt;

                        Link link = new Link(null, null, null);
                        link.deserializeNBT(linkCompoundNbt);
                        instance.register(link);
                    }
                    else
                    {
                        TMod.LOGGER.error("NBT is not a CompoundNBT");
                    }
                });
            }
            else
            {
                TMod.LOGGER.error("NBT is not a ListNBT");
            }

        }
    }
}
