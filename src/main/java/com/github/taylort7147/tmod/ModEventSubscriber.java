package com.github.taylort7147.tmod;

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
            setUp(new Item(new Item.Properties()), "example_item"));
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
