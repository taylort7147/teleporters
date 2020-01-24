package com.github.taylort7147.tmod.teleporter.capability;

import com.github.taylort7147.tmod.teleporter.TeleporterLinkManager;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class TeleporterLinkDataProvider implements ICapabilitySerializable<INBT>
{
//    private World world;
    private TeleporterLinkManager manager;
    Capability<ITeleporterData> capability;

    public TeleporterLinkDataProvider(/* World world */)
    {
//        this.capability = new Capability<ITeleporterData>()
//        this.world = world;
        this.manager = new TeleporterLinkManager();
    }

    @CapabilityInject(ITeleporterData.class)
    public static Capability<ITeleporterData> CAPABILITY_TELEPORTER_LINK_DATA = null;

    private LazyOptional<ITeleporterData> instance = LazyOptional
            .of(CAPABILITY_TELEPORTER_LINK_DATA::getDefaultInstance);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        return cap == CAPABILITY_TELEPORTER_LINK_DATA ? LazyOptional.of(() -> this.manager).cast()
                : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT()
    {
        return CAPABILITY_TELEPORTER_LINK_DATA.getStorage().writeNBT(CAPABILITY_TELEPORTER_LINK_DATA,
                instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt)
    {
        CAPABILITY_TELEPORTER_LINK_DATA.getStorage().readNBT(CAPABILITY_TELEPORTER_LINK_DATA,
                instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty")), null, nbt);

    }
}
