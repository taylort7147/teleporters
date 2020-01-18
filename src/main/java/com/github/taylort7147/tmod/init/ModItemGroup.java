package com.github.taylort7147.tmod.init;

import com.github.taylort7147.tmod.TMod;
import java.util.function.Supplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup extends ItemGroup
{

    public static final ItemGroup MOD_ITEM_GROUP = new ModItemGroup(TMod.MODID,
        () -> new ItemStack(ModItems.PURPUR_INGOT));

    private final Supplier<ItemStack> mIconSupplier;

    public ModItemGroup(final String name, final Supplier<ItemStack> iconSupplier)
    {
        super(name);
        mIconSupplier = iconSupplier;
    }

    @Override
    public ItemStack createIcon()
    {
        return mIconSupplier.get();
    }
}
