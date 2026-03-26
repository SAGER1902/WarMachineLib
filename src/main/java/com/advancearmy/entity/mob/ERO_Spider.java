package advancearmy.entity.mob;
import java.util.List;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.animal.IronGolem;
import advancearmy.AdvanceArmy;
import net.minecraft.world.entity.Pose;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.entity.PathfinderMob;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import advancearmy.entity.ai.ZombieAttackGoalSA;
import advancearmy.init.ModEntities;
import wmlib.api.ITool;
import net.minecraft.util.Mth;
// 新增导入
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.world.phys.Vec3;
import wmlib.common.living.ai.LivingLockGoal;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;

import net.minecraft.world.entity.monster.Monster;
import advancearmy.util.TargetSelect;
public class ERO_Spider extends Monster implements IEnemy {
    // 使用字节存储标志位，与原版蜘蛛一致
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(ERO_Spider.class, EntityDataSerializers.BYTE);
    
    public ERO_Spider(EntityType<? extends ERO_Spider> entityType, Level level) {
        super(entityType, level);
        //this.xpReward = 1;
		this.setMaxUpStep(1.5F);
    }
	
   public void updateSwingTime(){
	   
   }
	
	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }
	}
    public ERO_Spider(PlayMessages.SpawnEntity packet, Level worldIn) {
        super(ModEntities.ERO_SPIDER.get(), worldIn);
    }

    /**
     * 使用 WallClimberNavigation 作为导航系统
     */
    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }
    /**
     * 注册数据同步字段
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }
	
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        //this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 1f));
        //this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		//this.goalSelector.addGoal(2, new ZombieAttackGoalSA(this, 1.0D, false));
		this.goalSelector.addGoal(6, new LivingLockGoal(this, 1.0D, true));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 10F, false, false, (attackentity) -> {
		return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 10F, false, false, (attackentity) -> {
		return true;}));
    }
	

	/*public void onSyncedDataUpdated(EntityDataAccessor<?> p_184206_1_) {
		if (this.isAggressive()) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(p_184206_1_);
	}*/
	
	public int attack_time = 0;
	boolean canSummon = false;
	int summonTime = 0;
	float runSpeed = 0.1F;
	int fastTime = 0;
	int jumpCool = 0;
	public boolean doHurtTarget(Entity entity) {
		boolean flag = entity.hurt(this.damageSources().mobAttack(this), 6);
		if (this.level().random.nextInt(3)==0 && !this.level().isClientSide()) {
			BlockState iblockstate = this.level().getBlockState(entity.blockPosition());
			if (iblockstate.isAir()){
				this.level().setBlockAndUpdate(entity.blockPosition(), Blocks.COBWEB.defaultBlockState());
			}
		}
		return flag;
	}
    @Override
    public void tick() {
        super.tick();
		if(summonTime<100)++summonTime;
		if(this.getTarget()!=null){
			LivingEntity living = this.getTarget();
			if(living.isAlive() && living!=null){
				//float f1 = this.yBodyRot * (2 * (float) Math.PI / 360);
				double dx = living.getX() - this.getX();
				double dz = living.getZ() - this.getZ();
				float targetYaw = (float) Math.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
				/*float angleDiff = targetYaw - this.yHeadRot;
				if (angleDiff > 180.0F) {
					angleDiff -= 360.0F;
				} else if (angleDiff < -180.0F) {
					angleDiff += 360.0F;
				}
				if (Mth.abs(angleDiff) > 170.0F && Math.abs(angleDiff) < 190.0F)*/{
					this.setYRot(targetYaw);
					this.yHeadRot=targetYaw;
				}/* else {
					this.setYRot(Mth.lerp(0.3F, this.getYRot(), targetYaw));
					this.yHeadRot=(Mth.lerp(0.5F, this.yHeadRot, targetYaw));
				}*/

				double dis = Math.sqrt(dx * dx + dz * dz);
				
				if(dis<2){
					++attack_time;
					if(attack_time>10){
						attack_time=0;
						this.doHurtTarget(living);
					}
				}else if(summonTime>40){
					float yawoffset = -((float) Math.atan2(dx, dz)) * 180.0F / (float) Math.PI;
					float yaw = yawoffset * (2 * (float) Math.PI / 360);
					double mox = 0;
					double moy = -0.2D;
					double moz = 0;
					if(fastTime<401)++fastTime;
					if(jumpCool<100)++jumpCool;
					if(fastTime>200){
						runSpeed=0.5F;
					}else{
						runSpeed=0.1F;
					}
					if(fastTime>300)fastTime=0;
					boolean flag = this.getSensing().hasLineOfSight(living);
					if(flag){
						mox -= Math.sin(yaw) * runSpeed;
						moz += Math.cos(yaw) * runSpeed;
						this.setDeltaMovement(mox, moy, moz);
					}else{
						this.getNavigation().moveTo(living.getX(), living.getY(), living.getZ(), runSpeed*4);
					}
					if(jumpCool>60 && this.level().random.nextInt(4)==0){//跳跃模式
						Vec3 vector3d = this.getDeltaMovement();
						this.setDeltaMovement(5F*vector3d.x, 0.3D+this.level().random.nextInt(2)*0.3D, 5F*vector3d.z);
						jumpCool = 0;
					}
				}
			}
		}else{
			if(fastTime>0)--fastTime;
		}
		int maxcount = 20;
		if(this.level().getDifficulty() == Difficulty.HARD)maxcount=30;
		if(this.level().getDifficulty() == Difficulty.EASY)maxcount=10;
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
		if (!(this.level() instanceof ServerLevel)) {
		} else {
			ServerLevel serverworld = (ServerLevel)this.level();
			if(this.getArmorValue()>=5D && this.hurtTime==0){
				if(summonTime>8 && this.isAggressive()){
					int count = 0;
					List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(40D, 40.0D, 40D));
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity friend = list.get(k2);
						if(friend instanceof ERO_Spider){
							++count;
						}
					}
					if(count<maxcount){
						this.getAttribute(Attributes.ARMOR).setBaseValue(this.getArmorValue()-1);
						ERO_Spider spider = new ERO_Spider(ModEntities.ERO_SPIDER.get(), serverworld);
						spider.setPos(this.getX(), this.getY()+2, this.getZ());
						//spider.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10+this.level().random.nextInt(10)*10*this.getMaxHealth()/50F);
						spider.finalizeSpawn(serverworld, this.level().getCurrentDifficultyAt(spider.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
						serverworld.addFreshEntity(spider);
					}
					summonTime=0;
				}
			}
		}
    }
    
    /**
     * 判断是否正在攀爬
     */
    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }
    
    /**
     * 获取爬墙状态
     */
    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }
    
    /**
     * 设置爬墙状态
     */
    public void setClimbing(boolean climbing) {
        byte currentFlags = this.entityData.get(DATA_FLAGS_ID);
        byte newFlags = climbing ? (byte)(currentFlags | 1) : (byte)(currentFlags & 0xFFFFFFFE);
        this.entityData.set(DATA_FLAGS_ID, newFlags);
    }
    
    /**
     * 蜘蛛在蛛网中不会被减速
     */
    @Override
    public void makeStuckInBlock(BlockState state, net.minecraft.world.phys.Vec3 motionMultiplier) {
        /*if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplier);
        }*/
    }
    
    /**
     * 蜘蛛免疫中毒效果
     */
    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return false;
    }
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F * (0.5F - this.random.nextFloat()) + 0.8F;
    }
    public boolean CanAttack(Entity entity) {
        return TargetSelect.mobCanAttack(this,entity,this.getTarget());
    }
    public boolean hurt(DamageSource source, float par2) {
        Entity entity;
        entity = source.getEntity();
        if (entity != null) {
            if (entity instanceof IEnemy) {
                return false;
            } else {
                return super.hurt(source, par2);
            }
        } else {
            return super.hurt(source, par2);
        }
    }
    /*protected boolean isSunSensitive() {
        return true;
    }*/
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT; // 改为蜘蛛音效
    }
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SPIDER_HURT; // 改为蜘蛛音效
    }
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH; // 改为蜘蛛音效
    }
    protected SoundEvent getStepSound() {
        return SoundEvents.SPIDER_STEP; // 改为蜘蛛音效
    }
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15f, 1.0f);
    }
    public MobType getMobType() {
        return MobType.ARTHROPOD; // 改为节肢动物类型
    }
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.65f; // 蜘蛛的标准眼睛高度
    }
    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0D : -0.45D;
    }
    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.5f;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return ERO_Spider.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D) //
                .add(Attributes.MOVEMENT_SPEED, 0.3D) // 使用蜘蛛的标准速度
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                //.add(Attributes.ARMOR, 3.0D)
				.add(Attributes.FOLLOW_RANGE, 80.0D);
    }
	public EntityDimensions getDimensions(Pose p_213305_1_) {
		float i = this.getMaxHealth()/50F;
		EntityDimensions entitysize = super.getDimensions(p_213305_1_);
		float f = (entitysize.width + 0.2F * (float)i) / entitysize.width;
		return entitysize.scale(f);
	}
	
    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, 
                                       @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        RandomSource random = level.getRandom();
		this.getAttribute(Attributes.ARMOR).setBaseValue(random.nextInt(11));
        // 有1%几率生成骷髅骑士
        if (random.nextInt(5) == 0) {
            ERO_Pillager skeleton = ModEntities.ENTITY_PI.get().create(this.level());
            if (skeleton != null) {
                skeleton.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0f);
                skeleton.finalizeSpawn(level, difficulty, reason, null, null);
                skeleton.startRiding(this);
            }
        }
        return spawnData;
    }
}