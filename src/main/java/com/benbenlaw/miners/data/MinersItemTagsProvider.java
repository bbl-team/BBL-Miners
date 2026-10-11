package com.benbenlaw.miners.data;

import com.benbenlaw.miners.Miners;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MinersItemTagsProvider extends ItemTagsProvider {

    MinersItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Miners.MOD_ID);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {


    }

}