package com.github.taylort7147.tmod.teleporter.event;

import com.github.taylort7147.tmod.TMod;
import com.github.taylort7147.tmod.block.BlockTeleporter;
import com.github.taylort7147.tmod.item.ItemTeleporterLinkEstablisher;

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

@EventBusSubscriber(modid = TMod.MODID, bus = EventBusSubscriber.Bus.FORGE)
public final class PlayerInteractEventHandler
{
    private static boolean isTeleportLinkEvent(final PlayerInteractEvent event)
    {
        final World world = event.getWorld();
        final BlockPos target = event.getPos();
        final Block targetBlock = world.getBlockState(target).getBlock();
        final ItemStack itemStackInHand = event.getItemStack();
        final Item itemInHand = (itemStackInHand == null) ? null : itemStackInHand.getItem();
        final Hand hand = event.getHand();

        return (hand == Hand.MAIN_HAND) && (targetBlock instanceof BlockTeleporter)
                && (itemInHand instanceof ItemTeleporterLinkEstablisher);
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (isTeleportLinkEvent(event))
        {
            BlockPos target = event.getPos();
            World world = event.getWorld();
            PlayerEntity player = event.getPlayer();
            if (player.isCrouching())
            {
                Event forwardEvent = new TeleporterLinkEvent.ResetLink(player, world, target);
                MinecraftForge.EVENT_BUS.post(forwardEvent);
            } else
            {
                Event forwardEvent = new TeleporterLinkEvent.EstablishEndpoint(player, world, target);
                MinecraftForge.EVENT_BUS.post(forwardEvent);
            }
        }
    }
}
