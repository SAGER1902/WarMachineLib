package advancearmy.world;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import java.util.Random;
import advancearmy.AAConfig;
public class SpawnChecker {
    public static boolean canNightSpawn(EntityType<? extends Mob> livingType, ServerLevelAccessor worldIn,
                                        MobSpawnType reason, BlockPos pos, RandomSource rand, float level) {
        return AAConfig.spawnMob && worldIn.getDifficulty() != Difficulty.PEACEFUL && checkSpawnNight(livingType, worldIn, reason, pos, rand);
    }
    public static boolean canGroundNightSpawn(EntityType<? extends Mob> livingType, ServerLevelAccessor worldIn,
                                        MobSpawnType reason, BlockPos pos, RandomSource rand, float level) {
        return AAConfig.spawnMob && worldIn.getDifficulty() != Difficulty.PEACEFUL && worldIn.canSeeSky(pos) && checkSpawnNight(livingType, worldIn, reason, pos, rand);
    }
    public static boolean canHardSpawn(EntityType<? extends Mob> livingType, ServerLevelAccessor worldIn,
                                       MobSpawnType reason, BlockPos pos, RandomSource rand, float level) {
        return AAConfig.spawnMob && worldIn.getDifficulty() == Difficulty.HARD && worldIn.canSeeSky(pos) && checkSpawnNight(livingType, worldIn, reason, pos, rand);
    }
    public static boolean canNormalSpawn(EntityType<? extends Mob> livingType, LevelAccessor worldIn,
                                         MobSpawnType reason, BlockPos pos, RandomSource rand, float level) {
        return AAConfig.spawnMob && checkSpawnNormal(livingType, worldIn, reason, pos, rand);
    }
    public static boolean friendSpawn(EntityType<? extends Mob> livingType, ServerLevelAccessor worldIn,
                                      MobSpawnType reason, BlockPos pos, RandomSource rand, float level) {
        return AAConfig.spawnSoldier && checkSpawnSun(livingType, worldIn, reason, pos, rand);
    }

    private static boolean checkSpawnNight(EntityType<? extends Mob> livingType, ServerLevelAccessor worldIn,
                                           MobSpawnType reason, BlockPos pos, RandomSource rand) {
        boolean isFlatWorld = worldIn.getLevel().getChunkSource().getGenerator() instanceof FlatLevelSource;
        if (isFlatWorld) {
            return rand.nextInt(64)==0 && Mob.checkMobSpawnRules(livingType,worldIn, reason, pos, rand);
        } else{
            return isDarkEnoughToSpawn(worldIn, pos, rand) && Mob.checkMobSpawnRules(livingType,worldIn, reason, pos, rand)
			&& rand.nextFloat() < getDaySpawnProbability(worldIn,false)*AAConfig.mobSpawnChance/100F;
        }
    }
    private static boolean checkSpawnSun(EntityType<? extends Mob> livingType, ServerLevelAccessor worldIn,
                                         MobSpawnType reason, BlockPos pos, RandomSource rand) {
		boolean isFlatWorld = worldIn.getLevel().getChunkSource().getGenerator() instanceof FlatLevelSource;
        if (isFlatWorld) {
            return /*rand.nextInt(4)==0 && */Mob.checkMobSpawnRules(livingType,worldIn, reason, pos, rand);
        } else{
			return worldIn.getRawBrightness(pos, 0) > 7 && Mob.checkMobSpawnRules(livingType,worldIn, reason, pos, rand)
			&& rand.nextFloat() < getDaySpawnProbability(worldIn,true)*AAConfig.friendSpawnChance/100F;
		}
    }
	
    private static boolean checkSpawnNormal(EntityType<? extends Mob> livingType, LevelAccessor worldIn,
                                            MobSpawnType reason, BlockPos pos, RandomSource rand) {
        return worldIn.getRawBrightness(pos, 0) < 8 && Mob.checkMobSpawnRules(livingType,worldIn, reason, pos, rand);
    }
	
    public static boolean isDarkEnoughToSpawn(ServerLevelAccessor worldIn, BlockPos pos, RandomSource rand) {
        if (worldIn.getBrightness(LightLayer.SKY, pos) > rand.nextInt(32)) {
            return false;
        }
        DimensionType dim = worldIn.dimensionType();
        int light = dim.monsterSpawnBlockLightLimit();
        if (light < 15 && worldIn.getBrightness(LightLayer.BLOCK, pos) > light) {
            return false;
        }
        int light2 = worldIn.getLevel().isThundering() ? worldIn.getMaxLocalRawBrightness(pos, 10) : worldIn.getMaxLocalRawBrightness(pos);
        return light2 <= dim.monsterSpawnLightTest().sample(rand);
    }
	
    public static float getDaySpawnProbability(ServerLevelAccessor worldIn, boolean isFriend) {
        long time = worldIn.getLevel().getDayTime();
        long day = time / 24000L;
		if(isFriend){
			if (day <= 0) {
				return 0.1F;
			} else if (day >= AAConfig.maxFriendSpawnDay) {
				return 1;
			} else {
				float progress = (float)day / (float)AAConfig.maxFriendSpawnDay;// 计算进度百分比
				return 0.1F + (1 - 0.1F) * progress;
			}
		}else{
			if (day <= 0) {
				return 0.1F;
			} else if (day >= AAConfig.maxMobSpawnDay) {
				return 1;
			} else {
				float progress = (float)day / (float)AAConfig.maxMobSpawnDay;// 计算进度百分比
				return 0.1F + (1 - 0.1F) * progress;
			}
		}

    }
}