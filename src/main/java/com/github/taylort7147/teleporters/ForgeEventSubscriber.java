package com.github.taylort7147.teleporters;

import com.github.taylort7147.teleporters.capability.CapabilityTeleporterLinkManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Teleporters.MODID, bus = Bus.FORGE)
public class ForgeEventSubscriber
{
    @SubscribeEvent
    public static void onAttachWorldCapabilities(AttachCapabilitiesEvent<World> event)
    {
        Teleporters.LOGGER.debug("onAttachWorldCapabilities");
        Teleporters.LOGGER.info("Attaching capabilities to world " + event.getObject().getWorldInfo().getWorldName());
        event.addCapability(new ResourceLocation(Teleporters.MODID, "teleporter_link_manager"),
                new CapabilityTeleporterLinkManager.Provider());
    }
}
