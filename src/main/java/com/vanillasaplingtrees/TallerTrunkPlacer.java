package com.vanillasaplingtrees;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Wraps a vanilla trunk placer and only adds extra height; shape, branches and foliage
 * attachment points still come from the wrapped placer. Never serialized (runtime-only).
 */
final class TallerTrunkPlacer extends TrunkPlacer {
    private final TrunkPlacer delegate;
    private final int extra;

    TallerTrunkPlacer(TrunkPlacer delegate, int extra) {
        super(0, 0, 0);
        this.delegate = delegate;
        this.extra = extra;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.STRAIGHT_TRUNK_PLACER; // only used for codec serialization, never reached here
    }

    @Override
    public int getTreeHeight(RandomSource random) {
        return delegate.getTreeHeight(random) + extra;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> setter, RandomSource random, int freeTreeHeight,
            BlockPos pos, TreeConfiguration config) {
        return delegate.placeTrunk(level, setter, random, freeTreeHeight, pos, config);
    }
}
