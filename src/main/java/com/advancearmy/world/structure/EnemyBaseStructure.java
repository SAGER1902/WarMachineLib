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
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource; // 用于判断超平坦世界
import java.util.List;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;
import advancearmy.AAConfig;
public class EnemyBaseStructure extends Structure {
    public static final Codec<EnemyBaseStructure> CODEC = EnemyBaseStructure.simpleCodec(EnemyBaseStructure::new);

    public EnemyBaseStructure(StructureSettings config) {
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
		/*{
			ChunkGenerator chunkGenerator = context.chunkGenerator();
			/*int seaLevel = chunkGenerator.getSeaLevel();
			boolean isSuperflat = false;
			if (chunkGenerator instanceof FlatLevelSource) {
				isSuperflat = true;
			}
			if (isSuperflat) *{
				// 1. 确定X, Z坐标（和原逻辑一样在区块内随机）
				int x = chunkPos.getMinBlockX() + worldgenRandom.nextInt(16);
				int z = chunkPos.getMinBlockZ() + worldgenRandom.nextInt(16);
				int flatGroundHeight = -61; // 超平坦经典预设的草地层在Y=4
				int structureHeight = flatGroundHeight + 1; // 在草地上方1格开始生成
				/*if (chunkGenerator instanceof FlatLevelSource flatSource) {
					List<BlockState> layers = flatSource.settings().getLayers();
					if (!layers.isEmpty()) {
						// 查找最高非空气方块
						for (int y = layers.size() - 1; y >= 0; y--) {
							BlockState state = layers.get(y);
							if (state != null && !state.isAir()) {
								// 找到最高非空气方块，在其上方生成
								structureHeight = y + 1;
								break;
							}
						}
						// 如果没找到非空气方块（理论上不可能），则使用图层数量作为高度
						if (structureHeight < 0) {
							structureHeight = layers.size();
						}
						if (structureHeight < seaLevel) {
							structureHeight = seaLevel + 1;
						}
					}
				}*
				BlockPos blockPos = new BlockPos(x, structureHeight, z);
				return Optional.of(new GenerationStub(blockPos, structurePiecesBuilder -> 
					BaseGenerator.addPieces(context.structureTemplateManager(), blockPos, 
						Rotation.getRandom(worldgenRandom), structurePiecesBuilder, worldgenRandom.nextInt(9))));
			}
		}*/
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
		BaseGenerator.addPieces(context.structureTemplateManager(), blockPos, Rotation.getRandom(random), structurePiecesBuilder, false)));
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
