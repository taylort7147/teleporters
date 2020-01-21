package com.github.taylort7147.tmod;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.taylort7147.tmod.teleporter.event.TeleporterLinkEventHandler;


@Mod(TMod.MODID)
public final class TMod
{
    public static final String MODID = "tmod";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    
    private TeleporterLinkEventHandler teleporterLinkEventHandler;
    
    public TMod()
    {
        LOGGER.debug("Hello from TMod!");
        teleporterLinkEventHandler = new TeleporterLinkEventHandler();
    }
}
