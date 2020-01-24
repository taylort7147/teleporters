package com.github.taylort7147.tmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

@Mod(TMod.MODID)
public final class TMod
{
    public static final String MODID = "tmod";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public TMod()
    {
        LOGGER.debug("Hello from TMod!");
    }
}
