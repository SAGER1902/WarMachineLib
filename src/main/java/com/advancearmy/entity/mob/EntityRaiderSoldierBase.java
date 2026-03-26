package advancearmy.entity.mob;

import java.util.List;

import advancearmy.AdvanceArmy;

import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.network.chat.Component;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Team;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AgeableMob;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import wmlib.common.living.EntityWMVehicleBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.AgeableMob;
import java.util.UUID;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LightLayer;


import net.minecraft.world.entity.raid.Raider;
public abstract class EntityRaiderSoldierBase extends Raider{
	public EntityRaiderSoldierBase(EntityType<? extends EntityRaiderSoldierBase> sodier, Level worldIn) {
		super(sodier, worldIn);
	}
    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }
    @Override
    public void applyRaidBuffs(int wave, boolean unusedFalse) {
    }
    /*@Override
    public boolean canBeLeader() {
        return false;
    }*/
	/*public static boolean canSpawnInLight(LevelAccessor level, BlockPos pos, RandomSource randomIn) {
		return level.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn((ServerLevelAccessor) level, pos, randomIn);
	}
	public static boolean checkEroMobSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		BlockPos blockpos = pos.below();
		boolean light = canSpawnInLight(level, pos, random);
		return reason == MobSpawnType.SPAWNER || light && level.getBlockState(blockpos).isValidSpawn(level, blockpos, type) && random.nextFloat() < 0.25F;
	}
	
    public static boolean checkMobSpawnRules(EntityType<? extends Mob> $$0, LevelAccessor $$1, MobSpawnType $$2, BlockPos $$3, RandomSource $$4) {
        BlockPos $$5 = $$3.below();
        return $$2 == MobSpawnType.SPAWNER || $$1.getBlockState($$5).isValidSpawn($$1, $$5, $$0) && isDarkEnoughToSpawn((ServerLevelAccessor) $$1, $$3, $$4);
    }
	@Override
	public boolean checkSpawnRules(LevelAccessor accessor, MobSpawnType type) {
		//if (!accessor.getLevelData().getGameRules().getBoolean(RatsMod.SPAWN_PIPERS)) return false;
		if (type == MobSpawnType.EVENT || type == MobSpawnType.SPAWNER) return super.checkSpawnRules(accessor, type);
		/*int spawnRoll = RatConfig.piperSpawnDecrease;
		if (spawnRoll == 0 || accessor.getRandom().nextInt(spawnRoll) == 0) {
			return super.checkSpawnRules(accessor, type);
		}*
		return false;
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
    }*/

	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return 1.8F;
	}
	
	public double getMountedYOffset() {
		return 0.6D;//0.12D
	}
	
	protected void registerGoals() {

	}
	
	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }
	}
	
   public EntityRaiderSoldierBase getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
      return null;
   }
	
	public static AttributeSupplier.Builder createMonsterAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D);
	}
	
    private static final EntityDataAccessor<Integer> remain1 = 
    		SynchedEntityData.<Integer>defineId(EntityRaiderSoldierBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> remain2 = 
    		SynchedEntityData.<Integer>defineId(EntityRaiderSoldierBase.class, EntityDataSerializers.INT);
	/*private static final EntityDataAccessor<Boolean> isattack = 
    		SynchedEntityData.<Boolean>defineId(EntityRaiderSoldierBase.class, EntityDataSerializers.BOOLEAN);*/
	private static final EntityDataAccessor<Boolean> choose = 
    		SynchedEntityData.<Boolean>defineId(EntityRaiderSoldierBase.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> movetype = SynchedEntityData.<Integer>defineId(EntityRaiderSoldierBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movex = SynchedEntityData.<Integer>defineId(EntityRaiderSoldierBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movey = SynchedEntityData.<Integer>defineId(EntityRaiderSoldierBase.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> movez = SynchedEntityData.<Integer>defineId(EntityRaiderSoldierBase.class, EntityDataSerializers.INT);

	public int cooltime;
	public int cooltime2;
	public int reload1 = 0;
	public int reload_time1;

	public float turretYaw;
	public float turretPitch;
	public float rotation_max = 60;
	public float turretPitchMax = -90;
	public float turretPitchMin = 90;
	
	public float attack_range_max = 34;
	public float attack_range_min = 0;
	public float attack_height_max = 75;
	public float attack_height_min = -20;

	public int magazine = 5;
	public boolean counter1 = false;
	public boolean counter2 = false;

	public int countlimit1 = 0;
	public boolean firetrue = false;

	public int startTime = 0;
	public float rote =0;
	public float yaw =0;
	public float throttle;//
	public float throttleRight;//
	public float throttleLeft;//
	public float thpera;
	public boolean sneak_aim = false;//
	public int aim_time = 0;
	
	
	public int anim1 = 0;
	public int anim2 = 0;

	public boolean hurt(DamageSource source, float par2)
    {
    	return super.hurt(source, par2);
    }
	public float cooltime5 = 0;
	public float cooltime6 = 0;
	public int find_time = 0;	 
	int clear_time = 0;
	public void tick() {
		super.tick();
		if(this.isAttacking()){
			if(aim_time<100)++aim_time;
		}else{
			if(aim_time>0)--aim_time;
		}
		if(this.anim1<25)++this.anim1;
		if(this.anim2<25)++this.anim2;
	}
	
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		{
			compound.putInt("movetype", this.getMoveType());
			compound.putInt("movex", this.getMovePosX());
			compound.putInt("movey", this.getMovePosY());
			compound.putInt("movez", this.getMovePosZ());
			compound.putInt("remain2", this.getRemain2());
			compound.putInt("remain1", this.getRemain1());
			//compound.putBoolean("isattack", this.isAttacking());
			compound.putBoolean("choose", this.getChoose());
		}
	}
	public void readAdditionalSaveData(CompoundTag compound)
	{
	   super.readAdditionalSaveData(compound);
		{
			this.setMoveType(compound.getInt("movetype"));
			this.setMovePosX(compound.getInt("movex"));
			this.setMovePosY(compound.getInt("movey"));
			this.setMovePosZ(compound.getInt("movez"));
			this.setRemain2(compound.getInt("remain2"));
			this.setRemain1(compound.getInt("remain1"));
			//this.setAttacking(compound.getBoolean("isattack"));
			this.setChoose(compound.getBoolean("choose"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(movetype, Integer.valueOf(0));
		this.entityData.define(movex, Integer.valueOf(0));
		this.entityData.define(movey, Integer.valueOf(0));
		this.entityData.define(movez, Integer.valueOf(0));
		this.entityData.define(remain2, Integer.valueOf(0));
		this.entityData.define(remain1, Integer.valueOf(0));
		//this.entityData.define(isattack, Boolean.valueOf(false));
		this.entityData.define(choose, Boolean.valueOf(false));
	}
	
	public int getMoveType() {
			return ((this.entityData.get(movetype)).intValue());
	}
	public void setMoveType(int stack) {
		this.entityData.set(movetype, Integer.valueOf(stack));
	}
	public int getMovePosX() {
		return ((this.entityData.get(movex)).intValue());
	}
	public void setMovePosX(int stack) {
	this.entityData.set(movex, Integer.valueOf(stack));
	}
	public int getMovePosY() {
	return ((this.entityData.get(movey)).intValue());
	}
	public void setMovePosY(int stack) {
	this.entityData.set(movey, Integer.valueOf(stack));
	}
	public int getMovePosZ() {
	return ((this.entityData.get(movez)).intValue());
	}
	public void setMovePosZ(int stack) {
	this.entityData.set(movez, Integer.valueOf(stack));
	}
	public int getRemain2() {
		return ((this.entityData.get(remain2)).intValue());
	}
	public void setRemain2(int stack) {
		this.entityData.set(remain2, Integer.valueOf(stack));
	}
	public int getRemain1() {
		return ((this.entityData.get(remain1)).intValue());
	}
	public void setRemain1(int stack) {
		this.entityData.set(remain1, Integer.valueOf(stack));
	}
	public boolean isAttacking() {
		//return ((this.entityData.get(isattack)).booleanValue());
		return this.isAggressive();
	}
	public void setAttacking(boolean stack) {
		//this.entityData.set(isattack, Boolean.valueOf(stack));
        this.setAggressive(stack);
	}
	public boolean getChoose() {
		return ((this.entityData.get(choose)).booleanValue());
	}
	public void setChoose(boolean stack) {
		this.entityData.set(choose, Boolean.valueOf(stack));
	}
}