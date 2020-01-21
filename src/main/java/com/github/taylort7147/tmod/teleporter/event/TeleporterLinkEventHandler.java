package com.github.taylort7147.tmod.teleporter.event;

import com.github.taylort7147.tmod.TMod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = TMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TeleporterLinkEventHandler
{
//    private TeleporterLinkManager manager;

    public TeleporterLinkEventHandler()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEstablishEndpoint(TeleporterLinkEvent.EstablishEndpoint event)
    {
        if (!event.getWorld().isRemote())
            TMod.LOGGER.debug("TeleporterLinkEvent.EstablishEndpoint event received with parameters: " + "player="
                    + event.getPlayer().getName().getString() + ", target=" + event.getTarget());
    }

    @SubscribeEvent
    public void onResetLink(TeleporterLinkEvent.ResetLink event)
    {
        if (!event.getWorld().isRemote())
            TMod.LOGGER.debug("TeleporterLinkEvent.ResetLink event received with parameters: " + "player="
                    + event.getPlayer().getName().getString() + ", target=" + event.getTarget());
    }

}
