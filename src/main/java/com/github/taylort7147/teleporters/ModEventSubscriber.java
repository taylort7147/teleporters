package com.github.taylort7147.teleporters;

import javax.annotation.Nonnull;

import com.github.taylort7147.teleporters.block.BlockTeleporter;
import com.github.taylort7147.teleporters.capability.CapabilityTeleporterLinkManager;
import com.github.taylort7147.teleporters.init.ModBlocks;
import com.github.taylort7147.teleporters.init.ModItemGroup;
import com.github.taylort7147.teleporters.item.ItemEnderCircuit;
import com.github.taylort7147.teleporters.item.ItemPurpurIngot;
import com.github.taylort7147.teleporters.item.ItemPurpurPlate;
import com.github.taylort7147.teleporters.item.ItemTeleporterLinkEstablisher;
import com.github.taylort7147.teleporters.tileentity.TeleporterTileEntity;
import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = Teleporters.MODID, bus = Bus.MOD)
public final class ModEventSubscriber
{

    /**
     * Event handler for registering all Teleporters items
     * 
     * @param event
     */
    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event)
    {
        Teleporters.LOGGER.debug("onRegisterItems");
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(
                setUp(new ItemPurpurIngot(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)),
                        ItemPurpurIngot.NAME),
                setUp(new ItemPurpurPlate(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)),
                        ItemPurpurPlate.NAME),
                setUp(new ItemEnderCircuit(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)),
                        ItemEnderCircuit.NAME),
                setUp(new ItemTeleporterLinkEstablisher(new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP)),
                        ItemTeleporterLinkEstablisher.NAME));

        for (final Block block : ForgeRegistries.BLOCKS.getValues())
        {
            registerBlockItemForBlock(registry, block);
        }
    }

    /**
     * Helper for registering a block item for a block with a default-generated
     * BlockItem
     * 
     * @param registry
     * @param block
     */
    private static void registerBlockItemForBlock(IForgeRegistry<Item> registry, final Block block)
    {
        final ResourceLocation blockRegistryName = block.getRegistryName();
        // An extra safe-guard against badly registered blocks
        Preconditions.checkNotNull(blockRegistryName, "Registry Name of block \"" + block + "\" of class \""
                + block.getClass().getName() + "\"is null! This is not allowed!");

        if (!blockRegistryName.getNamespace().equals(Teleporters.MODID))
        {
            return;
        }

        final Item.Properties properties = new Item.Properties().group(ModItemGroup.MOD_ITEM_GROUP);
        final BlockItem blockItem = new BlockItem(block, properties);
        registry.register(setUp(blockItem, blockRegistryName));
    }

    /**
     * Event handler for registering all Teleporters blocks
     * 
     * @param event
     */
    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
    {
        Teleporters.LOGGER.debug("onRegisterBlocks");
        event.getRegistry()
                .registerAll(setUp(
                        new BlockTeleporter(Block.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 2.0F)),
                        BlockTeleporter.NAME));
    }

    /**
     * Event handler for registering all Teleporters tile entity types
     * 
     * @param event
     */
    @SubscribeEvent
    public static void onRegisterTileEntityTypes(@Nonnull final RegistryEvent.Register<TileEntityType<?>> event)
    {
        Teleporters.LOGGER.debug("onRegisterTileEntityTypes");
        event.getRegistry().registerAll(
                // We don't have a datafixer for our TileEntity, so we pass null into build
                setUp(TileEntityType.Builder.create(TeleporterTileEntity::new, ModBlocks.TELEPORTER).build(null),
                        TeleporterTileEntity.NAME));
    }

    /**
     * Generic helper for setting the registry name on an object under the
     * Teleporters namespace
     * 
     * @param <T>
     * @param entry The object being registered
     * @param name  The un-namespaced name of the object being registered
     * @return
     */
    public static <T extends IForgeRegistryEntry<T>> T setUp(final T entry, final String name)
    {
        return setUp(entry, new ResourceLocation(Teleporters.MODID, name));
    }

    /**
     * Generic helper for setting the registry name on an object under the
     * Teleporters namespace
     * 
     * @param <T>
     * @param entry        The object being registered
     * @param registryName The resource location of the object being registered
     * @return
     */
    public static <T extends IForgeRegistryEntry<T>> T setUp(final T entry, final ResourceLocation registryName)
    {
        entry.setRegistryName(registryName);
        return entry;
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event)
    {
        Teleporters.LOGGER.debug("onCommonSetup");
        Teleporters.LOGGER.info("Registering capabilities");
        CapabilityTeleporterLinkManager.register();
    }
}
