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
import java.util.Optional;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import advancearmy.AAConfig;
public class FriendBaseStructure extends Structure {
    public static final Codec<FriendBaseStructure> CODEC = FriendBaseStructure.simpleCodec(FriendBaseStructure::new);

    public FriendBaseStructure(StructureSettings config) {
        super(config);
    }

	private static boolean hasSpawnStructureGenerated = false;
    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos currentChunk = context.chunkPos();
        boolean isSpawnChunk = (Math.abs(currentChunk.x) <= 6 && Math.abs(currentChunk.z) <= 6); // 判断是否为原点区块
        if (isSpawnChunk) {
			/*if(!hasSpawnStructureGenerated)*/{
				int x = currentChunk.getMiddleBlockX();
				int z = currentChunk.getMiddleBlockZ();
				int seaLevel = context.chunkGenerator().getSeaLevel();
				int height = 128;
				NoiseColumn noiseColumn = context.chunkGenerator().getBaseColumn(
					x, z, context.heightAccessor(), context.randomState()
				);
				for (int y = height; y > seaLevel; y--) {
					if (!noiseColumn.getBlock(y).isAir() && noiseColumn.getBlock(y + 1).isAir()) {
						break;
					}
					height--;
				}
				/*if (height > seaLevel)*/{
					//hasSpawnStructureGenerated = true; // 设置标志，防止重复生成
					BlockPos spawnPos = new BlockPos(x, height, z);
					return Optional.of(new GenerationStub(spawnPos, builder -> {
						BaseGenerator.addPiecesOne(context.structureTemplateManager(), 
							spawnPos, 
							Rotation.NONE, // 固定旋转
							builder, 
							11 // 固定使用第0个变体
						);
					}));
				}
			}
        }
		{
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
			BaseGenerator.addPieces(context.structureTemplateManager(), blockPos, Rotation.getRandom(random), structurePiecesBuilder, true)));
		}
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
