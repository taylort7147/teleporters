package com.github.taylort7147.tmod.teleporter.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class TeleporterLinkEvent extends Event
{
    protected PlayerEntity player;
    protected World world;
    protected BlockPos blockPos;

    private TeleporterLinkEvent(PlayerEntity player, World world, BlockPos blockPos)
    {
        super();
        this.player = player;
        this.world = world;
        this.blockPos = blockPos;
    }

    /**
     * An event that signals the resetting of a teleporter link.
     * 
     * @author taylor
     *
     */
    @Cancelable
    public static class ResetLink extends TeleporterLinkEvent
    {

        public ResetLink(PlayerEntity player, World world, BlockPos blockPos)
        {
            super(player, world, blockPos);
        }
    }

    /**
     * An event that represents a player establishing one side of a teleporter link.
     * 
     * @author taylor
     *
     */
    @Cancelable
    public static class EstablishEndpoint extends TeleporterLinkEvent
    {
        public EstablishEndpoint(PlayerEntity player, World world, BlockPos blockPos)
        {
            super(player, world, blockPos);
        }
    }

    /**
     * Get the player that caused the event.
     * 
     * @return
     */
    public PlayerEntity getPlayer()
    {
        return player;
    }

    /**
     * Get the world that the event occurred in.
     * 
     * @return
     */
    public World getWorld()
    {
        return world;
    }

    /**
     * Get the block position of the target of the event.
     * 
     * @return
     */
    public BlockPos getTarget()
    {
        return blockPos;
    }
}
