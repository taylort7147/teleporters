package com.github.taylort7147.tmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.taylort7147.tmod.teleporter.TeleporterLinkManager;
import com.github.taylort7147.tmod.teleporter.event.TeleporterLinkEventHandler;

import net.minecraftforge.fml.common.Mod;

@Mod(TMod.MODID)
public final class TMod
{
    public static final String MODID = "tmod";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private TeleporterLinkManager teleporterLinkManager;
    private TeleporterLinkEventHandler teleporterLinkEventHandler;

    public TMod()
    {
        LOGGER.debug("Hello from TMod!");
        this.teleporterLinkManager = new TeleporterLinkManager();
        this.teleporterLinkEventHandler = new TeleporterLinkEventHandler(this.teleporterLinkManager);
    }
}
