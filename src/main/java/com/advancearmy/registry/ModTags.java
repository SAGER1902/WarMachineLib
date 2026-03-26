package advancearmy.registry;

import advancearmy.AdvanceArmy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public interface ModTags {
    TagKey<Biome> IS_OVERWORLD = TagKey.create(Registries.BIOME, new ResourceLocation(AdvanceArmy.MODID, "is_overworld"));
	TagKey<Biome> NO_FOREST = TagKey.create(Registries.BIOME, new ResourceLocation(AdvanceArmy.MODID, "no_forest"));
}
