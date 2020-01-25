package com.github.taylort7147.tmod;

import com.github.taylort7147.tmod.teleporter.capability.CapabilityTeleporterLinkManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = TMod.MODID, bus = Bus.FORGE)
public class ForgeEventSubscriber
{
    @SubscribeEvent
    public static void onAttachWorldCapabilities(AttachCapabilitiesEvent<World> event)
    {
        TMod.LOGGER.debug("onAttachWorldCapabilities");
        TMod.LOGGER.info("Attaching capabilities to world " + event.getObject().getWorldInfo().getWorldName());
        event.addCapability(new ResourceLocation(TMod.MODID, "teleporter_link_manager"),
                new CapabilityTeleporterLinkManager.Provider());
    }
}
