package advancearmy.entity.mob;

import java.util.EnumSet;
import java.util.Random;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.PathfinderMob;
import wmlib.common.bullet.EntityShell;
import advancearmy.AdvanceArmy;
import wmlib.api.IEnemy;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.server.level.ServerLevel;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PlayMessages;
import advancearmy.entity.EntitySA_LandBase;
import advancearmy.init.ModEntities;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.levelgen.Heightmap;
import wmlib.api.ITool;
import net.minecraft.world.entity.monster.Monster;
import advancearmy.util.TargetSelect;
public class ERO_Ghast extends Monster implements IEnemy{
   //private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(ERO_Ghast.class, EntityDataSerializers.BOOLEAN);
   private int explosionPower = 5;
   private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(ERO_Ghast.class, EntityDataSerializers.INT);
   public ERO_Ghast(EntityType<? extends ERO_Ghast> p_i50206_1_, Level p_i50206_2_) {
      super(p_i50206_1_, p_i50206_2_);
      this.xpReward = 10;
      this.moveControl = new ERO_Ghast.MoveHelperController(this);
   }
   
	public ERO_Ghast(PlayMessages.SpawnEntity packet, Level worldIn) {
		super(ModEntities.ENTITY_GST.get(), worldIn);
	}
   
	public void updateSwingTime(){
	}
   
	public float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
	protected void registerGoals() {
	  this.goalSelector.addGoal(5, new ERO_Ghast.RandomFlyGoal(this));
	  this.goalSelector.addGoal(7, new ERO_Ghast.LookAroundGoal(this));
	  this.goalSelector.addGoal(7, new ERO_Ghast.FireballAttackGoal(this));
	  /*this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (p_213812_1_) -> {
		 return Math.abs(p_213812_1_.getY() - this.getY()) <= 4.0D;
	  }));*/
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

	boolean ischarge = false;
   @OnlyIn(Dist.CLIENT)
   public boolean isCharging() {
      //return this.entityData.get(DATA_IS_CHARGING);
	  return this.isAggressive();
   }

   public void setCharging(boolean p_175454_1_) {
      //this.entityData.set(DATA_IS_CHARGING, p_175454_1_);
	  this.ischarge=true;
   }

   /*protected boolean shouldDespawnInPeaceful() {
      return true;
   }*/

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
      //this.entityData.define(DATA_IS_CHARGING, false);
   }
   public void setAIType(int p_203034_1_) {
      this.entityData.set(ID_SIZE, Mth.clamp(p_203034_1_, 0, 64));
   }
   public int getAIType() {
      return this.entityData.get(ID_SIZE);
   }
   
   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200.0D).add(Attributes.FOLLOW_RANGE, 100.0D);
   }

   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.GHAST_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.GHAST_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.GHAST_DEATH;
   }

   protected float getSoundVolume() {
      return 5.0F;
   }

	public void checkDespawn(){
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }
	}
    public static boolean checkGhastSpawnRules(EntityType<ERO_Ghast> $$0, LevelAccessor $$1, MobSpawnType $$2, BlockPos $$3, RandomSource $$4) {
        //return $$1.getDifficulty() != Difficulty.PEACEFUL && $$4.nextInt(20) == 0 && ERO_Ghast.checkMobSpawnRules($$0, $$1, $$2, $$3, $$4);
		return true;
    }

	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource damageSource) {
	  return false;
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
   
   public int getMaxSpawnClusterSize() {
      return 1;
   }
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		this.setAIType(this.random.nextInt(10));
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

   protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
      return 2.6F;
   }

   static class FireballAttackGoal extends Goal {
      private final ERO_Ghast ghast;
      public int chargeTime;

      public FireballAttackGoal(ERO_Ghast p_i45837_1_) {
         this.ghast = p_i45837_1_;
      }

      public boolean canUse() {
         return this.ghast.getTarget() != null;
      }

      public void start() {
         this.chargeTime = 0;
      }

      public void stop() {
         this.ghast.setCharging(false);
      }

	public void AIWeapon2(double w, double h, double z){
		if(this.ghast.getTarget()!=null){
			double d5 = this.ghast.getTarget().getX() - this.ghast.getX();
			double d7 = this.ghast.getTarget().getZ() - this.ghast.getZ();
			double d6 = this.ghast.getTarget().getY() - this.ghast.getY();
			double d1 = this.ghast.getEyeY() - (this.ghast.getTarget().getEyeY());
			double d3 = (double) Math.sqrt(d5 * d5 + d7 * d7);
			float f11 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			this.ghast.setXRot(-f11);//
		}
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= Mth.sin(this.ghast.getYRot() * 0.01745329252F) * z;
		zz11 += Mth.cos(this.ghast.getYRot() * 0.01745329252F) * z;
		xx11 -= Mth.sin(this.ghast.getYRot() * 0.01745329252F + 1) * w;
		zz11 += Mth.cos(this.ghast.getYRot() * 0.01745329252F + 1) * w;
		EntityShell bullet = new EntityShell(this.ghast.level(), this.ghast);
		bullet.power = 50;
		bullet.setGravity(0.02F);
		bullet.setExLevel(3);
		bullet.flame = true;
		bullet.moveTo(this.ghast.getX() + xx11, this.ghast.getY()+h, this.ghast.getZ() + zz11, this.ghast.getYRot(), this.ghast.getXRot());
		bullet.shootFromRotation(this.ghast, this.ghast.getXRot(), this.ghast.getYRot(), 0.0F, 1F, 2);
		bullet.setFX("FlamethrowerTrail");
		bullet.setModel("wmlib:textures/entity/flare.obj");
		bullet.setTex("wmlib:textures/entity/flare.png");
		if (!this.ghast.level().isClientSide) this.ghast.level().addFreshEntity(bullet);
	}

      public void tick() {
         LivingEntity livingentity = this.ghast.getTarget();
         double d0 = 64.0D;
         if (livingentity.distanceToSqr(this.ghast) < 4096.0D && this.ghast.hasLineOfSight(livingentity)) {
            Level world = this.ghast.level();
            ++this.chargeTime;
            if (this.chargeTime == 10 && !this.ghast.isSilent()) {
               world.levelEvent((Player)null, 1015, this.ghast.blockPosition(), 0);
            }

            if (this.chargeTime == 20) {
               double d1 = 4.0D;
               Vec3 vector3d = this.ghast.getViewVector(1);
               double d2 = livingentity.getX() - (this.ghast.getX() + vector3d.x * 4.0D);
               double d3 = livingentity.getY(0.5D) - (0.5D + this.ghast.getY(0.5D));
               double d4 = livingentity.getZ() - (this.ghast.getZ() + vector3d.z * 4.0D);
               if (!this.ghast.isSilent()) {
					world.levelEvent((Player)null, 1016, this.ghast.blockPosition(), 0);
               }
			   
			   if(this.ghast.getAIType()>5){
				   if (!( this.ghast.level() instanceof ServerLevel)) {
						//return false;
					} else {
						ServerLevel serverworld = (ServerLevel)this.ghast.level();
						ERO_Phantom pha = new ERO_Phantom(ModEntities.ENTITY_PHA.get(), serverworld);
						pha.setPos((double)this.ghast.getX(), (double)this.ghast.getY()-5, (double)this.ghast.getZ());
						pha.setEvilPhantomSize(2);
						//pha.finalizeSpawn(serverworld, this.ghast.level().getCurrentDifficultyAt(pha.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
						serverworld.addFreshEntityWithPassengers(pha);
						if (livingentity != null)pha.setTarget(livingentity); 
					}
			   }else{
					this.AIWeapon2(0,0,1);
			   }
			   this.ghast.setAggressive(false);
               this.chargeTime = -40;
            }
         } else if (this.chargeTime > 0) {
            --this.chargeTime;
         }

		if(this.chargeTime > 10)this.ghast.setAggressive(true);
		//this.ghast.setCharging(this.ghast.isAggressive());
      }
   }

   static class LookAroundGoal extends Goal {
      private final ERO_Ghast ghast;

      public LookAroundGoal(ERO_Ghast p_i45839_1_) {
         this.ghast = p_i45839_1_;
         this.setFlags(EnumSet.of(Goal.Flag.LOOK));
      }

      public boolean canUse() {
         return true;
      }

	@Override
	public void tick() {
		if (this.ghast.getTarget() == null) {
			Vec3 $$0 = this.ghast.getDeltaMovement();
			this.ghast.setYRot(-((float)Mth.atan2($$0.x, $$0.z)) * 57.295776f);
			this.ghast.yBodyRot = this.ghast.getYRot();
		} else {
			LivingEntity $$1 = this.ghast.getTarget();
			double $$2 = 64.0;
			if ($$1.distanceToSqr(this.ghast) < 4096.0) {
				double $$3 = $$1.getX() - this.ghast.getX();
				double $$4 = $$1.getZ() - this.ghast.getZ();
				this.ghast.setYRot(-((float)Mth.atan2($$3, $$4)) * 57.295776f);
				this.ghast.yBodyRot = this.ghast.getYRot();
			}
		}
	}
   }

   static class MoveHelperController extends MoveControl {
      private final ERO_Ghast ghast;
      private int floatDuration;

      public MoveHelperController(ERO_Ghast p_i45838_1_) {
         super(p_i45838_1_);
         this.ghast = p_i45838_1_;
      }

      public void tick() {
         if (this.operation == MoveControl.Operation.MOVE_TO) {
            if (this.floatDuration-- <= 0) {
               this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
               Vec3 vector3d = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
               double d0 = vector3d.length();
               vector3d = vector3d.normalize();
               if (this.canReach(vector3d, Mth.ceil(d0))) {
                  this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vector3d.scale(0.1D)));
               } else {
                  this.operation = MoveControl.Operation.WAIT;
               }
            }

         }
      }

      private boolean canReach(Vec3 p_220673_1_, int p_220673_2_) {
         AABB axisalignedbb = this.ghast.getBoundingBox();

         for(int i = 1; i < p_220673_2_; ++i) {
            axisalignedbb = axisalignedbb.move(p_220673_1_);
            if (!this.ghast.level().noCollision(this.ghast, axisalignedbb)) {
               return false;
            }
         }

         return true;
      }
   }

   static class RandomFlyGoal extends Goal {
      private final ERO_Ghast ghast;

      public RandomFlyGoal(ERO_Ghast p_i45836_1_) {
         this.ghast = p_i45836_1_;
         this.setFlags(EnumSet.of(Goal.Flag.MOVE));
      }

      public boolean canUse() {
         MoveControl movementcontroller = this.ghast.getMoveControl();
         if (!movementcontroller.hasWanted()) {
            return true;
         } else {
            double d0 = movementcontroller.getWantedX() - this.ghast.getX();
            double d1 = movementcontroller.getWantedY() - this.ghast.getY();
            double d2 = movementcontroller.getWantedZ() - this.ghast.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            return d3 < 1.0D || d3 > 3600.0D;
         }
      }

      public boolean canContinueToUse() {
         return false;
      }
		int block_height = 0;
      public void start() {
         RandomSource random = this.ghast.getRandom();
         double d0 = this.ghast.getX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0);
         double d1 = this.ghast.getY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0);
		 block_height = 80+this.ghast.level().getHeight(
			Heightmap.Types.WORLD_SURFACE, 
			(int)Math.floor(this.ghast.getX()), 
			(int)Math.floor(this.ghast.getZ())
		);
		 if(d1>block_height)d1=block_height;
         double d2 = this.ghast.getZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0);
         this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
      }
   }
}