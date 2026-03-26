package advancearmy.registry;

import advancearmy.AdvanceArmy;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import advancearmy.AAConfig;
public class ModStructureSets {
    public static final ResourceKey<StructureSet> DEFENSE = register("defense_work");
	public static final ResourceKey<StructureSet> FRIEND = register("friend");
	public static final ResourceKey<StructureSet> ENEMY = register("enemy");
	
    public static void bootstrap(BootstapContext<StructureSet> bootstapContext) {
        HolderGetter<Structure> holderGetter = bootstapContext.lookup(Registries.STRUCTURE);
        bootstapContext.register(DEFENSE, new StructureSet(holderGetter.getOrThrow(ModStructureKeys.DEFENSE), new RandomSpreadStructurePlacement(6, 5, RandomSpreadType.LINEAR, 867700448)));
		bootstapContext.register(FRIEND, new StructureSet(holderGetter.getOrThrow(ModStructureKeys.FRIEND), new RandomSpreadStructurePlacement(6, 5, RandomSpreadType.LINEAR, 867700447)));
		bootstapContext.register(ENEMY, new StructureSet(holderGetter.getOrThrow(ModStructureKeys.ENEMY), new RandomSpreadStructurePlacement(6, 5, RandomSpreadType.LINEAR, 867700446)));
    }

    private static ResourceKey<StructureSet> register(String string) {
        return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(AdvanceArmy.MODID, string));
    }
}
