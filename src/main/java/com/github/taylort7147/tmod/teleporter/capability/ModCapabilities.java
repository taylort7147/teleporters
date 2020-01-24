package com.github.taylort7147.tmod.teleporter.capability;

import com.github.taylort7147.tmod.teleporter.TeleporterLinkManager;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities
{
    @CapabilityInject(ITeleporterData.class)
    public static Capability<ITeleporterData> CAPABILITY_TELEPORTER_DATA = null;

    public static void registerCapabilities()
    {
        CapabilityManager.INSTANCE.register(ITeleporterData.class, new CapabilityTeleporterData.Storage(),
                TeleporterLinkManager::new);
    }
}
