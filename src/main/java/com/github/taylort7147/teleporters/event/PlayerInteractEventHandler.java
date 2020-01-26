package com.github.taylort7147.teleporters.event;

import com.github.taylort7147.teleporters.Endpoint;
import com.github.taylort7147.teleporters.Teleporters;
import com.github.taylort7147.teleporters.block.BlockTeleporter;
import com.github.taylort7147.teleporters.item.ItemTeleporterLinkEstablisher;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Teleporters.MODID, bus = EventBusSubscriber.Bus.FORGE)
public final class PlayerInteractEventHandler
{
    private static boolean isUsingTeleporterLinkEstablisher(final PlayerInteractEvent event)
    {
        final ItemStack itemStackInHand = event.getItemStack();
        final Item itemInHand = (itemStackInHand == null) ? null : itemStackInHand.getItem();
        final Hand hand = event.getHand();

        return (hand == Hand.MAIN_HAND) && (itemInHand instanceof ItemTeleporterLinkEstablisher);
    }

    private static boolean isTargetingTeleporter(final PlayerInteractEvent event)
    {
        final World world = event.getWorld();
        final BlockPos target = event.getPos();
        final Block targetBlock = world.getBlockState(target).getBlock();
        return targetBlock instanceof BlockTeleporter;
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        Teleporters.LOGGER.debug("onPlayerRightClickBlock");
        if (isUsingTeleporterLinkEstablisher(event) && isTargetingTeleporter(event))
        {
            Teleporters.LOGGER.debug("onPlayerRightClickBlock (using teleporter link establisher and targeting teleporter)");
            World world = event.getWorld();
            final BlockPos target = event.getPos();
            PlayerEntity player = event.getPlayer();
            Endpoint endpoint = new Endpoint(target, world.getDimension().getType().getId());
            Event forwardEvent = new TeleporterEvent.TeleporterLinkEstablisherUsed(world, player, endpoint);
            MinecraftForge.EVENT_BUS.post(forwardEvent);
        }
    }

    @SubscribeEvent
    public static void onPlayerUse(PlayerInteractEvent.RightClickItem event)
    {
        Teleporters.LOGGER.debug("onPlayerUse");
        if (isUsingTeleporterLinkEstablisher(event))
        {
            Teleporters.LOGGER.debug("onPlayerUse (using teleporter link establisher");
            World world = event.getWorld();
            PlayerEntity player = event.getPlayer();
            Event forwardEvent = new TeleporterEvent.TeleporterLinkEstablisherUsed(world, player, null);
            MinecraftForge.EVENT_BUS.post(forwardEvent);
        }
    }
}
