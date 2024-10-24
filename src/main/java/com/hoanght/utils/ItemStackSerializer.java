package com.hoanght.utils;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.util.stream.Stream;

public class ItemStackSerializer {

    public String encode(ItemStack[] itemStackArray) {
        NBTTagList nBTTagList = new NBTTagList();

        for (ItemStack itemStack : itemStackArray) {
            CraftItemStack craftItemStack = getCraftVersion(itemStack);
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            if (craftItemStack != null) {
                try {
                    nBTTagCompound = (NBTTagCompound) CraftItemStack.asNMSCopy(craftItemStack).b(
                            HolderLookup.a.a(Stream.empty()), nBTTagCompound);
                } catch (NullPointerException e) {
                    Bukkit.getLogger().warning("Failed to serialize item (" + itemStack + ")");
                }
            }
            nBTTagList.add(nBTTagCompound);
        }
        return Base64Coder.encodeString(nBTTagList.toString());
    }

    private CraftItemStack getCraftVersion(ItemStack itemStack) {
        if (itemStack instanceof CraftItemStack) {
            return (CraftItemStack) itemStack;
        }
        if (itemStack != null) {
            return CraftItemStack.asCraftCopy(itemStack);
        }
        return null;
    }
}
