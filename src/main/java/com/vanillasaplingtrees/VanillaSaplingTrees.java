package com.vanillasaplingtrees;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(VanillaSaplingTrees.MOD_ID)
public class VanillaSaplingTrees {
    public static final String MOD_ID = "vanilla_sapling_trees";

    public VanillaSaplingTrees(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(SaplingGrowHandler::onGrow);
    }
}
