package com.vanillasaplingtrees;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;

/**
 * Per-species trunk adjustments. Keys are vanilla configured feature names (incl. *_bees_* variants).
 *
 * Extra trunk height added on top of the vanilla height (vanilla numbers from memory, unverified in game):
 *  oak +2, fancy oak +2, birch +1, small jungle tree +2, acacia +1, cherry +2,
 *  spruce +3 and foliage trunk_height 3-4 (leaves start trunk_height + offset above ground).
 *  Dark oak and all 2x2 "mega" trees (mega_jungle_tree, mega_spruce, mega_pine) already have
 *  well over 4 bare blocks, so they are intentionally NOT changed (2x2 behaviour stays vanilla).
 */
final class TreeVariants {
    private TreeVariants() {}

    enum Profile { OAK, FANCY_OAK, BIRCH, JUNGLE, SPRUCE, ACACIA, CHERRY }

    static Profile profileFor(String path) {
        if (path.equals("oak") || path.startsWith("oak_bees_")) return Profile.OAK;
        if (path.equals("fancy_oak") || path.startsWith("fancy_oak_bees_")) return Profile.FANCY_OAK;
        if (path.equals("birch") || path.startsWith("birch_bees_")) return Profile.BIRCH;
        if (path.equals("jungle_tree_no_vine")) return Profile.JUNGLE;
        if (path.equals("spruce")) return Profile.SPRUCE;
        if (path.equals("acacia")) return Profile.ACACIA;
        if (path.equals("cherry") || path.startsWith("cherry_bees_")) return Profile.CHERRY;
        return null;
    }

    static TreeConfiguration apply(TreeConfiguration cfg, Profile p) {
        int extra = switch (p) {
            case OAK, FANCY_OAK, JUNGLE, CHERRY -> 2;
            case BIRCH, ACACIA -> 1;
            case SPRUCE -> 3;
        };
        FoliagePlacer foliage = cfg.foliagePlacer;
        if (p == Profile.SPRUCE && foliage instanceof SpruceFoliagePlacer) {
            // vanilla: radius U(2,3), offset U(0,2), trunk_height U(1,2)
            foliage = new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(3, 4));
        }
        // 6-argument constructor (rootPlacer kept); dirtProvider is set separately.
        TreeConfiguration.TreeConfigurationBuilder b = new TreeConfiguration.TreeConfigurationBuilder(
                cfg.trunkProvider, new TallerTrunkPlacer(cfg.trunkPlacer, extra),
                cfg.foliageProvider, foliage, cfg.rootPlacer, cfg.minimumSize)
                .dirt(cfg.dirtProvider)
                .decorators(cfg.decorators);
        if (cfg.ignoreVines) b.ignoreVines();
        if (cfg.forceDirt) b.forceDirt();
        return b.build();
    }
}
