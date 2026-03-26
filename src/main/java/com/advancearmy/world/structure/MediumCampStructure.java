package advancearmy.world.structure;

import com.mojang.serialization.Codec;
import advancearmy.registry.ModStructureTypes;
import advancearmy.world.structure.BaseGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;
import advancearmy.AAConfig;
public class MediumCampStructure extends Structure {
    public static final Codec<MediumCampStructure> CODEC = MediumCampStructure.simpleCodec(MediumCampStructure::new);

    public MediumCampStructure(StructureSettings config) {
        super(config);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        WorldgenRandom worldgenRandom = generationContext.random();
        int x = generationContext.chunkPos().getMinBlockX() + worldgenRandom.nextInt(16);
        int z = generationContext.chunkPos().getMinBlockZ() + worldgenRandom.nextInt(16);
        int seaLevel = generationContext.chunkGenerator().getSeaLevel();

        int height = 128;

        NoiseColumn noiseColumn = generationContext.chunkGenerator().getBaseColumn(x, z, generationContext.heightAccessor(), generationContext.randomState());

        for (int y = height; y > seaLevel; y--) {

            if (!noiseColumn.getBlock(y).isAir() && noiseColumn.getBlock(y + 1).isAir()) break;

            height--;
        }

        if (height <= seaLevel) {
            return Optional.empty();
        }

        BlockPos blockPos = new BlockPos(x, height, z);
        WorldgenRandom random = generationContext.random();

        return Optional.of(new Structure.GenerationStub(blockPos, structurePiecesBuilder -> 
		BaseGenerator.addPieces(generationContext.structureTemplateManager(), blockPos, Rotation.getRandom(random), structurePiecesBuilder,true)));
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.DEFENSE.get();
    }

}
