package advancearmy.registry;
import advancearmy.AdvanceArmy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModStructureKeys {
    public static final ResourceKey<Structure> DEFENSE = of("defense_work");
	public static final ResourceKey<Structure> FRIEND = of("friend");
	public static final ResourceKey<Structure> ENEMY = of("enemy");
    private static ResourceKey<Structure> of(String id) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(AdvanceArmy.MODID, id));
    }
}
