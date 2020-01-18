package com.github.taylort7147.tmod;

import com.github.taylort7147.tmod.init.ModItemGroup;
import com.github.taylort7147.tmod.item.ItemPurpurIngot;
import com.github.taylort7147.tmod.item.ItemPurpurPlate;
import com.github.taylort7147.tmod.item.ItemEnderCircuit;
import com.github.taylort7147.tmod.item.ItemTeleporterLinkEstablisher;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;                 
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid=TMod.MODID, bus=EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber
{
    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
            setUp(new ItemPurpurIngot(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)), ItemPurpurIngot.NAME),
            setUp(new ItemPurpurPlate(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)), ItemPurpurPlate.NAME),
            setUp(new ItemEnderCircuit(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)), ItemEnderCircuit.NAME),
            setUp(new ItemTeleporterLinkEstablisher(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)), ItemTeleporterLinkEstablisher.NAME));
    }

    public static <T extends IForgeRegistryEntry<T>> T 
    setUp(final T entry, final String name)
    {
        return setUp(entry, new ResourceLocation(TMod.MODID, name));
    }

    public static <T extends IForgeRegistryEntry<T>> T 
    setUp(final T entry, final ResourceLocation registryName)
    {
        entry.setRegistryName(registryName);
        return entry;
    }
}
