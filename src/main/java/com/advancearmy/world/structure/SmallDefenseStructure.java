package advancearmy.world.structure;

import com.mojang.serialization.Codec;
import advancearmy.registry.ModStructureTypes;
import advancearmy.world.structure.DefenseGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.ChunkPos;
import java.util.Optional;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import advancearmy.AAConfig;
public class SmallDefenseStructure extends Structure {
    public static final Codec<SmallDefenseStructure> CODEC = SmallDefenseStructure.simpleCodec(SmallDefenseStructure::new);

    public SmallDefenseStructure(StructureSettings config) {
        super(config);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
		if (Math.abs(chunkPos.x) <= 15 && 
			Math.abs(chunkPos.z) <= 15) {
			return Optional.empty();
		}
		
        WorldgenRandom worldgenRandom = context.random();
        int x = context.chunkPos().getMinBlockX() + worldgenRandom.nextInt(16);
        int z = context.chunkPos().getMinBlockZ() + worldgenRandom.nextInt(16);
        int seaLevel = context.chunkGenerator().getSeaLevel();
        int height = 128;
        NoiseColumn noiseColumn = context.chunkGenerator().getBaseColumn(x, z, context.heightAccessor(), context.randomState());
        for (int y = height; y > seaLevel; y--) {
            if (!noiseColumn.getBlock(y).isAir() && noiseColumn.getBlock(y + 1).isAir()) break;
            height--;
        }
        if (height <= seaLevel) {
            return Optional.empty();
        }
        BlockPos blockPos = new BlockPos(x, height, z);
		
		if (!hasOverheadObstructionImproved(context, blockPos, 5, height)) {
			return Optional.empty();
		}
		
        WorldgenRandom random = context.random();
        return Optional.of(new Structure.GenerationStub(blockPos, structurePiecesBuilder -> 
		DefenseGenerator.addPieces(context.structureTemplateManager(), blockPos, Rotation.getRandom(random), structurePiecesBuilder)));
    }

	private boolean hasOverheadObstructionImproved(GenerationContext context, BlockPos centerPos, 
												   int radius, int structureHeight) {
		int checkHeight = structureHeight + 3;
		int centerX = centerPos.getX();
		int centerY = centerPos.getY();
		int centerZ = centerPos.getZ();
		
		// 为了性能，只在少数关键点检查
		int[][] checkPoints = {
			{0, 0},          // 中心
			{-radius, 0},    // 左
			{radius, 0},     // 右
			{0, -radius},    // 前
			{0, radius}      // 后
		};
		
		for (int[] point : checkPoints) {
			int checkX = centerX + point[0];
			int checkZ = centerZ + point[1];
			
			// 获取该列的NoiseColumn
			NoiseColumn column = context.chunkGenerator().getBaseColumn(
				checkX, checkZ, context.heightAccessor(), context.randomState()
			);
			
			// 检查上方空间
			for (int dy = 1; dy <= checkHeight; dy++) {
				BlockState state = column.getBlock(centerY + dy);
				
				if (!state.isAir() && !state.canBeReplaced() && 
					//!state.getMaterial().isLiquid() && 
					!state.is(BlockTags.LEAVES) && 
					!state.is(BlockTags.FLOWERS)) {
					return true;
				}
			}
		}
		return false;
	}

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.DEFENSE.get();
    }

}
