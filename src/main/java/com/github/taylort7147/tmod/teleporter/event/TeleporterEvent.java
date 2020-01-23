package com.github.taylort7147.tmod.teleporter.event;

import java.util.List;

import com.github.taylort7147.tmod.teleporter.Endpoint;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class TeleporterEvent extends Event
{

    protected World world;

    public TeleporterEvent(World world)
    {
        super();
        this.world = world;
    }

    @Cancelable
    public static class TeleporterLinkEstablisherUsed extends TeleporterEvent
    {
        protected PlayerEntity player;
        private Endpoint endpoint;
        private boolean wasCrouching;

        public TeleporterLinkEstablisherUsed(World world, PlayerEntity player, Endpoint endpoint)
        {
            super(world);
            this.player = player;
            this.endpoint = endpoint;
            this.wasCrouching = player.isCrouching();
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

        public Endpoint getEndpoint()
        {
            return endpoint;
        }

        public boolean wasCrouching()
        {
            return wasCrouching;
        }
    }

    @Cancelable
    public static class TeleporterActivated extends TeleporterEvent
    {
        Endpoint origin;
        List<PlayerEntity> players;

        public TeleporterActivated(World world, Endpoint origin)
        {
            super(world);
            this.origin = origin;
            this.players = world.getEntitiesWithinAABB(EntityType.PLAYER, new AxisAlignedBB(origin.getPos()),
                    e -> true);
        }
        
        public Endpoint getOrigin()
        {
            return origin;
        }

        public List<PlayerEntity> getPlayers()
        {
            return players;
        }
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
}
