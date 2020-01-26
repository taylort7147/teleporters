package com.github.taylort7147.teleporters.capability;

import com.github.taylort7147.teleporters.ITeleporterLinkManager;
import com.github.taylort7147.teleporters.Link;
import com.github.taylort7147.teleporters.TeleporterLinkManager;
import com.github.taylort7147.teleporters.Teleporters;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityTeleporterLinkManager
{
    @CapabilityInject(ITeleporterLinkManager.class)
    public static Capability<ITeleporterLinkManager> INSTANCE = null;

    /**
     * Static helper method for registering this capability
     */
    public static void register()
    {
        CapabilityManager.INSTANCE.register(ITeleporterLinkManager.class, new CapabilityTeleporterLinkManager.Storage(),
                TeleporterLinkManager::new);
    }

    public static class Storage implements Capability.IStorage<ITeleporterLinkManager>
    {

        @Override
        public INBT writeNBT(Capability<ITeleporterLinkManager> capability, ITeleporterLinkManager instance,
                Direction side)
        {
            ListNBT nbt = new ListNBT();
            instance.getLinks().forEach(link -> nbt.add(link.serializeNBT()));
            return nbt;
        }

        @Override
        public void readNBT(Capability<ITeleporterLinkManager> capability, ITeleporterLinkManager instance,
                Direction side, INBT nbt)
        {
            if (nbt instanceof ListNBT)
            {
                ListNBT listNbt = (ListNBT) nbt;
                listNbt.forEach(linkNbt -> {
                    if (linkNbt instanceof CompoundNBT)
                    {
                        CompoundNBT linkCompoundNbt = (CompoundNBT) linkNbt;

                        Link link = new Link(null, null, null);
                        link.deserializeNBT(linkCompoundNbt);
                        instance.register(link);
                    } else
                    {
                        Teleporters.LOGGER.error("NBT is not a CompoundNBT");
                    }
                });
            } else
            {
                Teleporters.LOGGER.error("NBT is not a ListNBT");
            }

        }
    }

    /**
     * This class is responsible for providing the capability implementation.
     */
    public static class Provider implements ICapabilitySerializable<INBT>
    {
        /**
         * A LazyOptional to return the default instance
         */
        private LazyOptional<ITeleporterLinkManager> defaultInstance = LazyOptional
                .of(CapabilityTeleporterLinkManager.INSTANCE::getDefaultInstance);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
        {
            return cap == CapabilityTeleporterLinkManager.INSTANCE ? defaultInstance.cast() : LazyOptional.empty();
        }

        @Override
        public INBT serializeNBT()
        {
            return CapabilityTeleporterLinkManager.INSTANCE.getStorage().writeNBT(
                    CapabilityTeleporterLinkManager.INSTANCE,
                    defaultInstance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty")),
                    null);
        }

        @Override
        public void deserializeNBT(INBT nbt)
        {
            CapabilityTeleporterLinkManager.INSTANCE.getStorage().readNBT(CapabilityTeleporterLinkManager.INSTANCE,
                    defaultInstance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty")),
                    null, nbt);

        }
    }
}
