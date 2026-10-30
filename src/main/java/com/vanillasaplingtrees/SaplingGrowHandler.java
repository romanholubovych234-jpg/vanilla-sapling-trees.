package com.vanillasaplingtrees;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;

import java.util.Optional;

/**
 * Fires only when a sapling grows (BlockGrowFeatureEvent), never during world generation.
 * Replaces the vanilla tree feature with a copy whose trunk is taller. The copy is a direct
 * (unregistered) holder, so the global configured features used by world generation are untouched.
 */
public final class SaplingGrowHandler {
    private SaplingGrowHandler() {}

    public static void onGrow(BlockGrowFeatureEvent event) {
        LevelAccessor level = event.getLevel();

        // 1. The block at the growth position must be a vanilla sapling.
        BlockState state = level.getBlockState(event.getPos());
        if (!(state.getBlock() instanceof SaplingBlock)) return;
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (!"minecraft".equals(blockId.getNamespace())) return;

        // 2. The feature must be a registered vanilla one (a direct holder = already ours / not vanilla).
        Holder<ConfiguredFeature<?, ?>> holder = event.getFeature();
        if (holder == null) return;
        Optional<ResourceKey<ConfiguredFeature<?, ?>>> key = holder.unwrapKey();
        if (key.isEmpty() || !"minecraft".equals(key.get().location().getNamespace())) return;

        // 3. Only tree features with a known profile.
        if (!(holder.value().config() instanceof TreeConfiguration cfg)) return;
        TreeVariants.Profile profile = TreeVariants.profileFor(key.get().location().getPath());
        if (profile == null) return;

        TreeConfiguration taller = TreeVariants.apply(cfg, profile);
        event.setFeature(Holder.<ConfiguredFeature<?, ?>>direct(new ConfiguredFeature<>(Feature.TREE, taller)));
    }
}
