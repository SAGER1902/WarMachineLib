package advancearmy.entity.mob;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import advancearmy.AdvanceArmy;
import net.minecraft.server.level.ServerLevel;
import wmlib.common.bullet.EntityBullet;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.PathfinderMob;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import advancearmy.entity.EntitySA_LandBase;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.MoverType;
import wmlib.api.ITool;
import net.minecraft.world.Difficulty;
import advancearmy.util.TargetSelect;

import net.minecraft.world.entity.monster.Monster;
public class ERO_Phantom extends Monster implements IEnemy{
   private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(ERO_Phantom.class, EntityDataSerializers.INT);
   private Vec3 moveTargetPoint = Vec3.ZERO;
   private BlockPos anchorPoint = BlockPos.ZERO;
   private ERO_Phantom.AttackPhase attackPhase = ERO_Phantom.AttackPhase.CIRCLE;

   public ERO_Phantom(EntityType<? extends ERO_Phantom> p_i50200_1_, Level p_i50200_2_) {
      super(p_i50200_1_, p_i50200_2_);
      this.xpReward = 5;
        this.moveControl = new PhantomMoveControl(this);
        this.lookControl = new PhantomLookControl(this);
   }
   public ERO_Phantom(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_PHA.get(), worldIn);
	}
   
   public void updateSwingTime(){
	   
   }
   
   
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02f, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8f));
            } else if (this.isInLava()) {
                this.moveRelative(0.02f, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            } else {
                float $$1 = 0.91f;
                if (this.onGround()) {
                    $$1 = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91f;
                }
                float $$2 = 0.16277137f / ($$1 * $$1 * $$1);
                $$1 = 0.91f;
                if (this.onGround()) {
                    $$1 = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91f;
                }
                this.moveRelative(this.onGround() ? 0.1f * $$2 : 0.02f, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale($$1));
            }
        }
        this.calculateEntityAnimation(false);
    }
   
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource damageSource) {
	  return false;
	}
   
	public static final int TICKS_PER_FLAP = Mth.ceil(24.166098f);
    @Override
    public boolean isFlapping() {
        return (this.getUniqueFlapTickOffset() + this.tickCount) % TICKS_PER_FLAP == 0;
    }
    public int getUniqueFlapTickOffset() {
        return this.getEvilPhantomSize() * 3;
    }
   protected BodyRotationControl createBodyControl() {
      return new ERO_Phantom.BodyHelperController(this);
   }
	public float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
   protected void registerGoals() {
	this.goalSelector.addGoal(1, new ERO_Phantom.PickAttackGoal());
	this.goalSelector.addGoal(2, new ERO_Phantom.SweepAttackGoal());
	this.goalSelector.addGoal(3, new ERO_Phantom.OrbitPointGoal());
	this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, Mob.class, 10, 60F, false, false, (attackentity) -> {
		return this.CanAttack(attackentity);
	}));
	this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, Player.class, 10, 60F, false, false, (attackentity) -> {
		return true;
	}));
   }
   
    public boolean CanAttack(Entity entity){
		return TargetSelect.mobCanAttack(this,entity,this.getTarget());
    }
   
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof IEnemy){
				return false;
			}else{
				return super.hurt(source, par2);
			}
		}else{
			return super.hurt(source, par2);
		}
	}
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ID_SIZE, 0);
   }

   public void setEvilPhantomSize(int p_203034_1_) {
      this.entityData.set(ID_SIZE, Mth.clamp(p_203034_1_, 0, 64));
   }
   
   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.FOLLOW_RANGE, 100.0D);
   }
   
   private void updateEvilPhantomSizeInfo() {
      this.refreshDimensions();
	  if(this.getEvilPhantomSize()<3){
		  this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)(40 + this.getEvilPhantomSize()));
	  }else{
		  this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)(6 + this.getEvilPhantomSize()));
	  }
   }

   public int getEvilPhantomSize() {
      return this.entityData.get(ID_SIZE);
   }

   protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
      return p_213348_2_.height * 0.35F;
   }

   public void onSyncedDataUpdated(EntityDataAccessor<?> p_184206_1_) {
      if (ID_SIZE.equals(p_184206_1_)) {
         this.updateEvilPhantomSizeInfo();
      }
      super.onSyncedDataUpdated(p_184206_1_);
   }
	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }
	}
   /*protected boolean shouldDespawnInPeaceful() {
      return true;
   }*/
	public void AIWeapon2(double w, double h, double z){
		if(this.getTarget()!=null){
			double d5 = this.getTarget().getX() - this.getX();
			double d7 = this.getTarget().getZ() - this.getZ();
			double d6 = this.getTarget().getY() - this.getY();
			double d1 = this.getEyeY() - (this.getTarget().getEyeY());
			double d3 = (double) Math.sqrt(d5 * d5 + d7 * d7);
			float f11 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			this.setXRot(-f11);//
		}
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= Mth.sin(this.getYRot() * 0.01745329252F) * z;
		zz11 += Mth.cos(this.getYRot() * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.getYRot() * 0.01745329252F + 1) * w;
		zz11 += Mth.cos(this.getYRot() * 0.01745329252F + 1) * w;
		EntityBullet bullet2 = new EntityBullet(this.level(), this);
		bullet2.power = 4;
		bullet2.setGravity(0.025F);
		bullet2.setBulletType(5);
		bullet2.moveTo(this.getX() + xx11, this.getY()+h, this.getZ() + zz11, this.getYRot(), this.getXRot());
		bullet2.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, 3F, 2);
		if (!this.level().isClientSide) this.level().addFreshEntity(bullet2);
	}
	int cooltime = 0;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(this.getEvilPhantomSize()>5){
				if(this.cooltime<20)++this.cooltime;
				if(this.attackPhase == AttackPhase.SWOOP && this.cooltime>10){
					this.AIWeapon2(0,0,1);
					this.cooltime = 0;
				}
			}
		}
   }

	/*public boolean canAttack(Entity ent){
		//return !(ent instanceof Enemy);
		return true;
	}*/
	/*public void aiStep() {
	  if (this.isAlive() && this.isSunBurnTick()) {
		 this.setSecondsOnFire(8);
	  }
	  super.aiStep();
	}*/
	/*protected void customServerAiStep() {
	  super.customServerAiStep();
	}*/

	
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		  this.anchorPoint = this.blockPosition().above(5);
		  this.setEvilPhantomSize(this.random.nextInt(16));
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

	public void readAdditionalSaveData(CompoundTag p_70037_1_) {
	  super.readAdditionalSaveData(p_70037_1_);
	  if (p_70037_1_.contains("AX")) {
		 this.anchorPoint = new BlockPos(p_70037_1_.getInt("AX"), p_70037_1_.getInt("AY"), p_70037_1_.getInt("AZ"));
	  }
	  this.setEvilPhantomSize(p_70037_1_.getInt("Size"));
	}

	public void addAdditionalSaveData(CompoundTag p_213281_1_) {
	  super.addAdditionalSaveData(p_213281_1_);
	  p_213281_1_.putInt("AX", this.anchorPoint.getX());
	  p_213281_1_.putInt("AY", this.anchorPoint.getY());
	  p_213281_1_.putInt("AZ", this.anchorPoint.getZ());
	  p_213281_1_.putInt("Size", this.getEvilPhantomSize());
	}

	@OnlyIn(Dist.CLIENT)
	public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
	  return true;
	}

	public SoundSource getSoundSource() {
	  return SoundSource.HOSTILE;
	}

	protected SoundEvent getAmbientSound() {
	  return SoundEvents.PHANTOM_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
	  return SoundEvents.PHANTOM_HURT;
	}

	protected SoundEvent getDeathSound() {
	  return SoundEvents.PHANTOM_DEATH;
	}

	public MobType getMobType() {
	  return MobType.UNDEAD;
	}

	protected float getSoundVolume() {
	  return 1.0F;
	}

	public boolean canAttackType(EntityType<?> p_213358_1_) {
	  return true;
	}

	public EntityDimensions getDimensions(Pose p_213305_1_) {
	  int i = this.getEvilPhantomSize();
	  EntityDimensions entitysize = super.getDimensions(p_213305_1_);
	  float f = (entitysize.width + 0.2F * (float)i) / entitysize.width;
	  return entitysize.scale(f);
	}

	static enum AttackPhase {
	  CIRCLE,
	  SWOOP;
	}

	class BodyHelperController extends BodyRotationControl {
	  public BodyHelperController(Mob p_i49925_2_) {
		 super(p_i49925_2_);
	  }
	  public void clientTick() {
		 ERO_Phantom.this.yHeadRot = ERO_Phantom.this.yBodyRot;
		 ERO_Phantom.this.yBodyRot = ERO_Phantom.this.getYRot();
	  }
	}

	class PhantomLookControl extends LookControl {
	  public PhantomLookControl(Mob p_i48802_2_) {
		 super(p_i48802_2_);
	  }

	  public void tick() {
	  }
	}

	abstract class MoveGoal extends Goal {
	  public MoveGoal() {
		 this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	  }
	  protected boolean touchingTarget() {
		 return ERO_Phantom.this.moveTargetPoint.distanceToSqr(ERO_Phantom.this.getX(), ERO_Phantom.this.getY(), ERO_Phantom.this.getZ()) < 8.0D;
	  }
	}



	class PhantomMoveControl extends MoveControl {
	  private float speed = 0.2F;
	  public PhantomMoveControl(Mob p_i48801_2_) {
		 super(p_i48801_2_);
	  }
	  public void tick() {
            if (ERO_Phantom.this.horizontalCollision) {
                ERO_Phantom.this.setYRot(ERO_Phantom.this.getYRot() + 180.0f);
                this.speed = 0.2f;
            }
            double $$0 = ERO_Phantom.this.moveTargetPoint.x - ERO_Phantom.this.getX();
            double $$1 = ERO_Phantom.this.moveTargetPoint.y - ERO_Phantom.this.getY();
            double $$2 = ERO_Phantom.this.moveTargetPoint.z - ERO_Phantom.this.getZ();
            double $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
            if (Math.abs($$3) > (double)1.0E-5f) {
                double $$4 = 1.0 - Math.abs($$1 * (double)0.7f) / $$3;
                $$3 = Math.sqrt(($$0 *= $$4) * $$0 + ($$2 *= $$4) * $$2);
                double $$5 = Math.sqrt($$0 * $$0 + $$2 * $$2 + $$1 * $$1);
                float $$6 = ERO_Phantom.this.getYRot();
                float $$7 = (float)Mth.atan2($$2, $$0);
                float $$8 = Mth.wrapDegrees(ERO_Phantom.this.getYRot() + 90.0f);
                float $$9 = Mth.wrapDegrees($$7 * 57.295776f);
                ERO_Phantom.this.setYRot(Mth.approachDegrees($$8, $$9, 4.0f) - 90.0f);
                ERO_Phantom.this.yBodyRot = ERO_Phantom.this.getYRot();
                this.speed = Mth.degreesDifferenceAbs($$6, ERO_Phantom.this.getYRot()) < 3.0f ? Mth.approach(this.speed, 1.8f, 0.005f * (1.8f / this.speed)) : Mth.approach(this.speed, 0.2f, 0.025f);
                float $$10 = (float)(-(Mth.atan2(-$$1, $$3) * 57.2957763671875));
                ERO_Phantom.this.setXRot($$10);
                float $$11 = ERO_Phantom.this.getYRot() + 90.0f;
                double $$12 = (double)(this.speed * Mth.cos($$11 * ((float)Math.PI / 180))) * Math.abs($$0 / $$5);
                double $$13 = (double)(this.speed * Mth.sin($$11 * ((float)Math.PI / 180))) * Math.abs($$2 / $$5);
                double $$14 = (double)(this.speed * Mth.sin($$10 * ((float)Math.PI / 180))) * Math.abs($$1 / $$5);
                Vec3 $$15 = ERO_Phantom.this.getDeltaMovement();
                ERO_Phantom.this.setDeltaMovement($$15.add(new Vec3($$12, $$14, $$13).subtract($$15).scale(0.2)));
            }
	  }
	}

	class OrbitPointGoal extends ERO_Phantom.MoveGoal {
	  private float angle;
	  private float distance;
	  private float height;
	  private float clockwise;

	  private OrbitPointGoal() {
	  }

	  public boolean canUse() {
		 return ERO_Phantom.this.getTarget() == null || ERO_Phantom.this.attackPhase == ERO_Phantom.AttackPhase.CIRCLE;
	  }

	  public void start() {
		 this.distance = 5.0F + ERO_Phantom.this.random.nextFloat() * 10.0F;
		 this.height = -4.0F + ERO_Phantom.this.random.nextFloat() * 9.0F;
		 this.clockwise = ERO_Phantom.this.random.nextBoolean() ? 1.0F : -1.0F;
		 this.selectNext();
	  }

	  public void tick() {
		 if (ERO_Phantom.this.random.nextInt(350) == 0) {
			this.height = -4.0F + ERO_Phantom.this.random.nextFloat() * 9.0F;
		 }

		 if (ERO_Phantom.this.random.nextInt(250) == 0) {
			++this.distance;
			if (this.distance > 15.0F) {
			   this.distance = 5.0F;
			   this.clockwise = -this.clockwise;
			}
		 }

		 if (ERO_Phantom.this.random.nextInt(450) == 0) {
			this.angle = ERO_Phantom.this.random.nextFloat() * 2.0F * (float)Math.PI;
			this.selectNext();
		 }

		 if (this.touchingTarget()) {
			this.selectNext();
		 }

		 if (ERO_Phantom.this.moveTargetPoint.y < ERO_Phantom.this.getY() && !ERO_Phantom.this.level().isEmptyBlock(ERO_Phantom.this.blockPosition().below(1))) {
			this.height = Math.max(1.0F, this.height);
			this.selectNext();
		 }

		 if (ERO_Phantom.this.moveTargetPoint.y > ERO_Phantom.this.getY() && !ERO_Phantom.this.level().isEmptyBlock(ERO_Phantom.this.blockPosition().above(1))) {
			this.height = Math.min(-1.0F, this.height);
			this.selectNext();
		 }

	  }

	  private void selectNext() {
		 if (BlockPos.ZERO.equals(ERO_Phantom.this.anchorPoint)) {
			ERO_Phantom.this.anchorPoint = ERO_Phantom.this.blockPosition();
		 }

		 this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180);
		 ERO_Phantom.this.moveTargetPoint = Vec3.atLowerCornerOf(ERO_Phantom.this.anchorPoint).add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)));
	  }
	}

	class PickAttackGoal extends Goal {
	  private int nextSweepTick;

	  private PickAttackGoal() {
	  }

	  public boolean canUse() {
		 LivingEntity livingentity = ERO_Phantom.this.getTarget();
		 return livingentity != null ? /*ERO_Phantom.this.canAttack(ERO_Phantom.this.getTarget(), EntityPredicate.DEFAULT)*/true : false;
	  }

	  public void start() {
		 this.nextSweepTick = 10;
		 ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.CIRCLE;
		 this.setAnchorAboveTarget();
	  }

	  public void stop() {
		 ERO_Phantom.this.anchorPoint = ERO_Phantom.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, ERO_Phantom.this.anchorPoint).above(10 + ERO_Phantom.this.random.nextInt(20));
	  }

	  public void tick() {
		 if (ERO_Phantom.this.attackPhase == ERO_Phantom.AttackPhase.CIRCLE) {
			--this.nextSweepTick;
			if (this.nextSweepTick <= 0) {
			   ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.SWOOP;
			   this.setAnchorAboveTarget();
			   this.nextSweepTick = (8 + ERO_Phantom.this.random.nextInt(4)) * 20;
			   ERO_Phantom.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + ERO_Phantom.this.random.nextFloat() * 0.1F);
			}
		 }

	  }

	  private void setAnchorAboveTarget() {
		 ERO_Phantom.this.anchorPoint = ERO_Phantom.this.getTarget().blockPosition().above(20 + ERO_Phantom.this.random.nextInt(20));
		 if (ERO_Phantom.this.anchorPoint.getY() < ERO_Phantom.this.level().getSeaLevel()) {
			ERO_Phantom.this.anchorPoint = new BlockPos(ERO_Phantom.this.anchorPoint.getX(), ERO_Phantom.this.level().getSeaLevel() + 1, ERO_Phantom.this.anchorPoint.getZ());
		 }
	  }
	}

	class SweepAttackGoal extends ERO_Phantom.MoveGoal {
	  private SweepAttackGoal() {
	  }

	  public boolean canUse() {
		 return ERO_Phantom.this.getTarget() != null && ERO_Phantom.this.attackPhase == ERO_Phantom.AttackPhase.SWOOP;
	  }

	  public boolean canContinueToUse() {
		 LivingEntity livingentity = ERO_Phantom.this.getTarget();
		 if (livingentity == null) {
			return false;
		 } else if (!livingentity.isAlive()) {
			return false;
		 } else if (!(livingentity instanceof Player) || !((Player)livingentity).isSpectator() && !((Player)livingentity).isCreative()) {
			if (!this.canUse()) {
			   return false;
			} else {
			   return true;
			}
		 } else {
			return false;
		 }
	  }

	  public void start() {
	  }

	  public void stop() {
		 ERO_Phantom.this.setTarget((LivingEntity)null);
		 ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.CIRCLE;
	  }

	  public void tick() {
		 LivingEntity livingentity = ERO_Phantom.this.getTarget();
		 ERO_Phantom.this.moveTargetPoint = new Vec3(livingentity.getX(), livingentity.getY(0.5D), livingentity.getZ());
		 if (ERO_Phantom.this.getBoundingBox().inflate((double)0.2F).intersects(livingentity.getBoundingBox())) {
			if(ERO_Phantom.this.getEvilPhantomSize()<3){
				ERO_Phantom.this.dead = true;
				ERO_Phantom.this.discard();
				ERO_Phantom.this.level().explode(ERO_Phantom.this, ERO_Phantom.this.getX(), ERO_Phantom.this.getY(), ERO_Phantom.this.getZ(), 2+ERO_Phantom.this.getEvilPhantomSize(), false, Level.ExplosionInteraction.MOB);
			}
			ERO_Phantom.this.doHurtTarget(livingentity);

			if (!( ERO_Phantom.this.level() instanceof ServerLevel)) {
				//return false;
			} else {
				 ServerLevel serverworld = (ServerLevel)ERO_Phantom.this.level();
				if(ERO_Phantom.this.level().random.nextInt(4)==1){
					ERO_Phantom pha = new ERO_Phantom(ModEntities.ENTITY_PHA.get(), serverworld);
					pha.setPos((double)ERO_Phantom.this.getX(), (double)ERO_Phantom.this.getY()+50, (double)ERO_Phantom.this.getZ());
					pha.finalizeSpawn(serverworld, ERO_Phantom.this.level().getCurrentDifficultyAt(pha.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
					serverworld.addFreshEntityWithPassengers(pha);
					if (livingentity != null)pha.setTarget(livingentity);
				}
				if(ERO_Phantom.this.level().random.nextInt(6)==1){
					ERO_Ghast gst = new ERO_Ghast(ModEntities.ENTITY_GST.get(), serverworld);
					gst.setPos((double)ERO_Phantom.this.getX(), (double)ERO_Phantom.this.getY()+50, (double)ERO_Phantom.this.getZ());
					gst.finalizeSpawn(serverworld, ERO_Phantom.this.level().getCurrentDifficultyAt(gst.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
					serverworld.addFreshEntityWithPassengers(gst);
					if (livingentity != null)gst.setTarget(livingentity);
				}
			}
			
			ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.CIRCLE;
			if (!ERO_Phantom.this.isSilent()) {
			   ERO_Phantom.this.level().levelEvent(1039, ERO_Phantom.this.blockPosition(), 0);
			}
		 } else if (ERO_Phantom.this.horizontalCollision || ERO_Phantom.this.hurtTime > 20 || ERO_Phantom.this.hurtTime > 5 && ERO_Phantom.this.getEvilPhantomSize()<3) {
			if(ERO_Phantom.this.getEvilPhantomSize()<3){
				ERO_Phantom.this.dead = true;
				ERO_Phantom.this.discard();
				ERO_Phantom.this.level().explode(ERO_Phantom.this, ERO_Phantom.this.getX(), ERO_Phantom.this.getY(), ERO_Phantom.this.getZ(), 2+ERO_Phantom.this.getEvilPhantomSize(), false, Level.ExplosionInteraction.MOB);
			}
			ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.CIRCLE;
		 }
	  }
	}
}