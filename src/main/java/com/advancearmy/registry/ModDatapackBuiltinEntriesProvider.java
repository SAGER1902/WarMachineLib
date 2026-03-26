package advancearmy.registry;

import advancearmy.AdvanceArmy;
import advancearmy.registry.ModStructureSets;
//import advancearmy.registry.ModConfiguredFeatures;
//import advancearmy.registry.ModPlacedFeatures;
import advancearmy.registry.ModStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackBuiltinEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            //.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            //.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap)
            .add(Registries.STRUCTURE, ModStructures::bootstrap);

    public ModDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(AdvanceArmy.MODID));
    }

}
