package com.github.taylort7147.tmod.teleporter.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.github.taylort7147.tmod.TMod;
import com.github.taylort7147.tmod.block.BlockTeleporter;
import com.github.taylort7147.tmod.teleporter.Endpoint;
import com.github.taylort7147.tmod.teleporter.Link;
import com.github.taylort7147.tmod.teleporter.TeleporterLinkManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = TMod.MODID, bus = EventBusSubscriber.Bus.FORGE)
public final class TeleporterLinkEventHandler
{
//    private TeleporterLinkManager manager;
    private static final LinkRequestManager pendingLinkRequests = new LinkRequestManager();
    private static final TeleportRequestManager pendingTeleportRequests = new TeleportRequestManager();

//    public TeleporterLinkEventHandler(/*TeleporterLinkManager manager*/)
//    {
////        this.manager = manager;
//        this.pendingLinkRequests = new LinkRequestManager();
//        this.pendingTeleportRequests = new TeleportRequestManager();
//        MinecraftForge.EVENT_BUS.register(this);
//    }

    // TODO: Make scheduling more generic, giving a callback to call after the
    // specified number of ticks has elapsed.
    // TODO: Move to separate file
    private static class TeleportRequestManager
    {
        List<TeleportRequest> requests;

        public TeleportRequestManager()
        {
            this.requests = new ArrayList<TeleportRequest>();
        }

        public void add(TeleportRequest request)
        {
            requests.add(request);
        }

        public void tick()
        {
            if (requests.isEmpty())
                return;

            List<TeleportRequest> processedRequests = new ArrayList<TeleportRequest>();
            requests.stream().forEach(r -> {
                r.tick();
                if (r.getRemainingTicks() <= 0)
                {
                    // TODO: Check if player is still in the game
                    // TODO: Check if player is still on the original teleporter
                    PlayerEntity player = r.getPlayer();
                    Endpoint destination = r.getDestination();

                    TMod.LOGGER.debug("Teleporting " + player.getName().getString() + " to " + destination);
                    player.sendMessage(new StringTextComponent("Teleporting to " + destination));
                    // TODO: Safe teleport
                    final BlockPos destinationPos = destination.getPos();
                    player.setPositionAndUpdate(destinationPos.getX() + 0.5, destinationPos.getY() + 0.2,
                            destinationPos.getZ() + 0.5);
                    processedRequests.add(r);
                }
            });

            requests.removeAll(processedRequests);
        }
    }

    // TODO: Move to separate file
    private static class TeleportRequest
    {
        private PlayerEntity player;
        private Endpoint destination;
        private int remainingTicks;
        // TODO: Add ability to cancel?

        public TeleportRequest(PlayerEntity player, Endpoint destination, int numberOfTicks)
        {
            this.player = player;
            this.destination = destination;
            this.remainingTicks = numberOfTicks;
        }

        public PlayerEntity getPlayer()
        {
            return player;
        }

        public Endpoint getDestination()
        {
            return destination;
        }

        public int getRemainingTicks()
        {
            return remainingTicks;
        }

        public void tick()
        {
            --remainingTicks;
        }
    }

    // TODO: Move to separate file
    private static class LinkRequest
    {
        private UUID owner;
        private Endpoint endpoint;

        public LinkRequest(UUID owner, Endpoint endpoint)
        {
            this.owner = owner;
            this.endpoint = endpoint;
        }

        public UUID getOwner()
        {
            return owner;
        }

        public Endpoint getEndpoint()
        {
            return endpoint;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (!(obj instanceof LinkRequest))
                return false;

            LinkRequest other = (LinkRequest) obj;
            return other.owner.equals(owner) && other.endpoint.equals(endpoint);
        }
    }

    // TODO: Move to separate file
    private static class LinkRequestManager
    {
        private List<LinkRequest> requests;

        public LinkRequestManager()
        {
            this.requests = new ArrayList<LinkRequest>();
        }

        @Nullable
        public LinkRequest find(UUID owner)
        {
            Optional<LinkRequest> request = requests.stream().filter(r -> r.getOwner().equals(owner)).findFirst();
            return (request.isPresent()) ? request.get() : null;
        }

        @Nullable
        public LinkRequest find(Endpoint endpoint)
        {
            Optional<LinkRequest> request = requests.stream().filter(r -> r.getEndpoint().equals(endpoint)).findFirst();
            return (request.isPresent()) ? request.get() : null;
        }

        // TODO: Consider returning an enum rather than throwing in each of these cases.
        public void add(LinkRequest request)
        {
            LinkRequest existingRequestForPlayer = find(request.getOwner());
            LinkRequest existingRequestForValue = find(request.getEndpoint());

            if (existingRequestForValue != null)
            {
                throw new RuntimeException(
                        "An existing request contains this endpoint " + request.getEndpoint().toString());
            }

            if (existingRequestForPlayer != null)
            {
                throw new RuntimeException(
                        "An existing request exists for this player with UUID " + request.getOwner());
            }
            requests.add(request);
        }

        public void remove(LinkRequest request)
        {
            if (!requests.remove(request))
            {
                throw new RuntimeException("The request is not in the list: " + request.toString());
            }
        }
    }

    /**
     * This event handler is responsible for handling teleporter link establisher
     * item use, which involves establishing and resetting links.
     * 
     * @param event
     */
    @SubscribeEvent
    public static void onTeleporterLinkEstablisherUsed(TeleporterEvent.TeleporterLinkEstablisherUsed event)
    {
        if (event.getWorld().isRemote())
        {
            return;
        }

        TMod.LOGGER.debug("onTeleporterLinkEstablisherUsed called with parameters: " + "player="
                + event.getPlayer().getName().getString() + ", endpoint=" + event.getEndpoint());

        Endpoint endpoint = event.getEndpoint();
        PlayerEntity player = event.getPlayer();
        UUID playerId = player.getUniqueID();

        LinkRequest pendingRequestForPlayer = pendingLinkRequests.find(playerId);
        boolean isPendingOwn = pendingRequestForPlayer != null;
        TMod.LOGGER.debug("isPending=" + isPendingOwn);

        // TODO: Reduce nesting
        if (endpoint == null)
        {
            if (event.wasCrouching() && isPendingOwn)
            {
                pendingLinkRequests.remove(pendingRequestForPlayer);
                player.sendMessage(new StringTextComponent("Link request cancelled"));
            } else
            {
                event.setCanceled(true);
            }
        } else
        {
            LinkRequest pendingRequestForEndpoint = pendingLinkRequests.find(endpoint);
            boolean isPendingOther = pendingRequestForEndpoint != null && pendingRequestForPlayer == null;
            TMod.LOGGER.debug("isPendingOther=" + isPendingOther);

            if (event.wasCrouching())
            {
                TMod.LOGGER.debug("Reset the link");

                TeleporterLinkManager manager = null; // TODO: Get from capabilities

                Link link = manager.getLink(endpoint);
                if (link == null)
                {
                    if (isPendingOwn)
                    {
                        pendingLinkRequests.remove(pendingRequestForPlayer);
                        player.sendMessage(new StringTextComponent("Link request cancelled"));
                    } else if (isPendingOther)
                    {
                        player.sendMessage(
                                new StringTextComponent("This link is part of another player's pending request"));
                        event.setCanceled(true);
                    } else
                    {
                        player.sendMessage(new StringTextComponent("No link was found for this endpoint"));
                        event.setCanceled(true);
                    }
                } else if (!playerId.equals(link.getOwner()))
                {
                    player.sendMessage(new StringTextComponent("You are not the owner of this link"));
                    event.setCanceled(true);
                } else
                {
                    manager.unregister(link);
                    player.sendMessage(new StringTextComponent("Link reset"));
                }
            } else
            {
                TMod.LOGGER.debug("Request endpoint/link");

                TeleporterLinkManager manager = null; // TODO: Get from capabilities

                boolean linkExists = manager.isEndpointPartOfLink(endpoint);
                TMod.LOGGER.debug("linkExists=" + linkExists);

                if (linkExists)
                {
                    // Cancel
                    player.sendMessage(new StringTextComponent("This endpoint is already part of a link"));
                    event.setCanceled(true);
                } else if (isPendingOther)
                {
                    // Cancel
                    player.sendMessage(new StringTextComponent(
                            "Another player has a pending link request containing this endpoint"));
                    event.setCanceled(true);
                } else if (isPendingOwn)
                {
                    if (pendingRequestForPlayer.getEndpoint().equals(endpoint))
                    {
                        player.sendMessage(
                                new StringTextComponent("You are already establishing a link with this endpoint"));
                        event.setCanceled(true);
                    } else
                    {
                        // New link
                        Link link = new Link(playerId, pendingRequestForPlayer.getEndpoint(), endpoint);
                        pendingLinkRequests.remove(pendingRequestForPlayer);
                        manager.register(link);
                        player.sendMessage(new StringTextComponent("Link established: " + link.toString()));
                    }
                } else
                {
                    // New pending request
                    LinkRequest request = new LinkRequest(playerId, endpoint);
                    pendingLinkRequests.add(request);
                    player.sendMessage(
                            new StringTextComponent("New request started with endpoint " + endpoint.toString()));
                }
            }
        }
    }

    /**
     * Schedules teleport requests for all players in the event if the teleportation
     * is valid.
     * 
     * @param event
     */
    @SubscribeEvent
    public static void onTeleporterActivated(TeleporterEvent.TeleporterActivated event)
    {
        TMod.LOGGER.debug("onTeleporterActivated");

        TeleporterLinkManager manager = null; // TODO: Get from capabilities

        final Endpoint origin = event.getOrigin();
        final Link link = manager.getLink(origin);
        final World world = event.getWorld();
        final List<PlayerEntity> players = event.getPlayers();

        // TODO: Reduce nesting
        if (link == null)
        {
            TMod.LOGGER.debug("Teleporter is not linked");
        } else
        {
            Endpoint destination = link.getOther(origin);
            if (destination == null)
            {
                TMod.LOGGER.debug("Destination does not exist for link " + link + " from origin " + origin);
            } else
            {
                BlockState destinationBlockState = world.getBlockState(destination.getPos());
                Block destinationBlock = destinationBlockState.getBlock();

                if (destinationBlock instanceof BlockTeleporter)
                {
                    TMod.LOGGER
                            .debug("Scheduling teleport request for " + players.size() + " players to " + destination);
                    players.stream().forEach(player -> {
                        pendingTeleportRequests.add(new TeleportRequest(player, destination, 10));
                    });
                } else
                {
                    TMod.LOGGER.debug("There is no teleporter at the destination " + destination);
                    ITextComponent message = new StringTextComponent(
                            "There is no teleporter at the destination " + destination);
                    for (PlayerEntity player : players)
                    {
                        player.sendMessage(message);
                    }
                }
            }
        }
    }

    /**
     * Processes pending teleport requests.
     * 
     * @param event
     */
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent event)
    {
        pendingTeleportRequests.tick();
    }
}
